package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func1;

/**
 * Created by kbahr on 10/29/14.
 */
public class OperatorParallelMap<T, R> implements Observable.Operator<R, T> {
    private final Func1<? super T, ? extends R> transformer;

    public OperatorParallelMap(Func1<? super T, ? extends R> transformer) {
        this.transformer = transformer;
    }

    @Override
    public Subscriber<? super T> call(final Subscriber<? super R> o) {
        return new Subscriber<T>(o) {

            @Override
            public void onCompleted() {
                o.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                o.onError(e);
            }

            @Override
            public void onNext(T t) {
                try {
                    o.onNext(transformer.call(t));
                } catch (Throwable e) {
                    onError(OnErrorThrowable.addValueAsLastCause(e, t));
                }
            }

        };
    }
}
