package rx.plugins;

import rx.Observable;
import rx.ParallelObservable;
import rx.Subscription;

/**
 * Abstract ExecutionHook with invocations at different lifecycle points of {@link ParallelObservable} execution with a
 * default no-op implementation.
 * <p>
 * See {@link RxJavaParallelPlugins} or the RxJava GitHub Wiki for information on configuring plugins:
 * <a href="https://github.com/Netflix/RxJava/wiki/Plugins">https://github.com/Netflix/RxJava/wiki/Plugins</a>.
 * <p>
 * <b>Note on thread-safety and performance:</b>
 * <p>
 * A single implementation of this class will be used globally so methods on this class will be invoked
 * concurrently from multiple threads so all functionality must be thread-safe.
 * <p>
 * Methods are also invoked synchronously and will add to execution time of the observable so all behavior
 * should be fast. If anything time-consuming is to be done it should be spawned asynchronously onto separate
 * worker threads.
 *
 */
public abstract class RxJavaParallelObservableExecutionHook {
    /**
     * Invoked during the construction by {@link rx.Observable#create(rx.Observable.OnSubscribe)}
     * <p>
     * This can be used to decorate or replace the <code>onSubscribe</code> function or just perform extra
     * logging, metrics and other such things and pass-thru the function.
     *
     * @param f
     *            original {@link rx.Observable.OnSubscribe}<{@code T}> to be executed
     * @return {@link rx.Observable.OnSubscribe}<{@code T}> function that can be modified, decorated, replaced or just
     *         returned as a pass-thru
     */
    public <T> Observable.OnSubscribe<T> onCreate(Observable.OnSubscribe<T> f) {
        return f;
    }

    /**
     * Invoked before {@link Observable#subscribe(rx.Subscriber)} is about to be executed.
     * <p>
     * This can be used to decorate or replace the <code>onSubscribe</code> function or just perform extra
     * logging, metrics and other such things and pass-thru the function.
     *
     * @param onSubscribe
     *            original {@link rx.Observable.OnSubscribe}<{@code T}> to be executed
     * @return {@link rx.Observable.OnSubscribe}<{@code T}> function that can be modified, decorated, replaced or just
     *         returned as a pass-thru
     */
    public <T> Observable.OnSubscribe<T> onSubscribeStart(ParallelObservable<? extends T> observableInstance, final Observable.OnSubscribe<T> onSubscribe) {
        // pass-thru by default
        return onSubscribe;
    }

    /**
     * Invoked after successful execution of {@link Observable#subscribe(rx.Subscriber)} with returned
     * {@link rx.Subscription}.
     * <p>
     * This can be used to decorate or replace the {@link rx.Subscription} instance or just perform extra logging,
     * metrics and other such things and pass-thru the subscription.
     *
     * @param subscription
     *            original {@link rx.Subscription}
     * @return {@link rx.Subscription} subscription that can be modified, decorated, replaced or just returned as a
     *         pass-thru
     */
    public <T> Subscription onSubscribeReturn(Subscription subscription) {
        // pass-thru by default
        return subscription;
    }

    /**
     * Invoked after failed execution of {@link Observable#subscribe(rx.Subscriber)} with thrown Throwable.
     * <p>
     * This is <em>not</em> errors emitted via {@link rx.Subscriber#onError(Throwable)} but exceptions thrown when
     * attempting to subscribe to a {@link rx.functions.Func1}<{@link rx.Subscriber}{@code <T>}, {@link Subscription}>.
     *
     * @param e
     *            Throwable thrown by {@link Observable#subscribe(rx.Subscriber)}
     * @return Throwable that can be decorated, replaced or just returned as a pass-thru
     */
    public <T> Throwable onSubscribeError(Throwable e) {
        // pass-thru by default
        return e;
    }

    /**
     * Invoked just as the operator functions is called to bind two operations together into a new
     * {@link Observable} and the return value is used as the lifted function
     * <p>
     * This can be used to decorate or replace the {@link rx.Observable.Operator} instance or just perform extra
     * logging, metrics and other such things and pass-thru the onSubscribe.
     *
     * @param lift
     *            original {@link rx.Observable.Operator}{@code <R, T>}
     * @return {@link rx.Observable.Operator}{@code <R, T>} function that can be modified, decorated, replaced or just
     *         returned as a pass-thru
     */
    public <T, R> Observable.Operator<? extends R, ? super T> onLift(final Observable.Operator<? extends R, ? super T> lift) {
        return lift;
    }
}
