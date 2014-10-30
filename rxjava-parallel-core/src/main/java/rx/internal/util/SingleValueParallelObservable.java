package rx.internal.util;

import rx.Observable;
import rx.ParallelObservable;
import rx.Subscriber;

/**
 * Emits a single value immediately upon subscription
 */
public class SingleValueParallelObservable<T> extends ParallelObservable<T> {
    public static final <T> SingleValueParallelObservable<T> create(T t) {
        return new SingleValueParallelObservable<T>(t);
    }

    private final T t;

    protected SingleValueParallelObservable(final T t) {
        super(new Observable.OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> s) {
                /*
                 *  We don't check isUnsubscribed as it is a significant performance impact in the fast-path use cases.
                 *  See PerfBaseline tests and https://github.com/ReactiveX/RxJava/issues/1383 for more information.
                 *  The assumption here is that when asking for a single item we should emit it and not concern ourselves with
                 *  being unsubscribed already. If the Subscriber unsubscribes at 0, they shouldn't have subscribed, or it will
                 *  filter it out (such as take(0)). This prevents us from paying the price on every subscription.
                 */
                s.onNext(t);
                s.onCompleted();
            }

        });
        this.t = t;
    }

    public T get() {
        return t;
    }
}
