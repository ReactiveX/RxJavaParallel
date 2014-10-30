package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by kbahr on 10/29/14.
 */
public class OperatorParallelMap<R, T> implements Observable.Operator<T, R> {
    @Override
    public Subscriber<? super R> call(Subscriber<? super T> subscriber) {
        return null;
    }
}
