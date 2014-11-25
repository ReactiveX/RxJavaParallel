package rx.internal.operators;

import rx.Observable;
import rx.ParallelObservable;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.CompositeSubscription;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Created by kbahr on 11/16/14.
 */
public class OperatorParallelMergeMaxConcurrent<T> implements Observable.Operator<T, ParallelObservable<? extends T>> {
    final int maxConcurrency;

    public OperatorParallelMergeMaxConcurrent(int maxConcurrency) {
        this.maxConcurrency = maxConcurrency;
    }

    @Override
    public Subscriber<? super ParallelObservable<? extends T>> call(Subscriber<? super T> child) {
        final SerializedSubscriber<T> s = new SerializedSubscriber<T>(child);
        final CompositeSubscription csub = new CompositeSubscription();
        child.add(csub);

        return new SourceSubscriber<T>(maxConcurrency, s, csub);
    }
    static final class SourceSubscriber<T> extends Subscriber<ParallelObservable<? extends T>> {
        final int maxConcurrency;
        final Subscriber<T> s;
        final CompositeSubscription csub;
        final Object guard;

        volatile int wip;
        @SuppressWarnings("rawtypes")
        static final AtomicIntegerFieldUpdater<SourceSubscriber> WIP_UPDATER
                = AtomicIntegerFieldUpdater.newUpdater(SourceSubscriber.class, "wip");

        /** Guarded by guard. */
        int active;
        /** Guarded by guard. */
        final Queue<ParallelObservable<? extends T>> queue;

        public SourceSubscriber(int maxConcurrency, Subscriber<T> s, CompositeSubscription csub) {
            super(s);
            this.maxConcurrency = maxConcurrency;
            this.s = s;
            this.csub = csub;
            this.guard = new Object();
            this.queue = new LinkedList<ParallelObservable<? extends T>>();
            this.wip = 1;
        }

        @Override
        public void onNext(ParallelObservable<? extends T> t) {
            synchronized (guard) {
                queue.add(t);
            }
            subscribeNext();
        }

        void subscribeNext() {
            ParallelObservable<? extends T> t;
            synchronized (guard) {
                t = queue.peek();
                if (t == null || active >= maxConcurrency) {
                    return;
                }
                active++;
                queue.poll();
            }

            Subscriber<T> itemSub = new Subscriber<T>() {
                boolean once = true;
                @Override
                public void onNext(T t) {
                    s.onNext(t);
                }

                @Override
                public void onError(Throwable e) {
                    SourceSubscriber.this.onError(e);
                }

                @Override
                public void onCompleted() {
                    if (once) {
                        once = false;
                        synchronized (guard) {
                            active--;
                        }
                        csub.remove(this);

                        subscribeNext();

                        SourceSubscriber.this.onCompleted();
                    }
                }

            };
            csub.add(itemSub);
            WIP_UPDATER.incrementAndGet(this);

            t.unsafeSubscribe(itemSub);
        }

        @Override
        public void onError(Throwable e) {
            s.onError(e);
            unsubscribe();
        }

        @Override
        public void onCompleted() {
            if (WIP_UPDATER.decrementAndGet(this) == 0) {
                s.onCompleted();
            }
        }
    }
}
