package rx.plugins;

import rx.Scheduler;

/**
 * Define alternate Scheduler implementations to be returned by the {@code Schedulers} factory methods.
 * <p>
 * See {@link RxJavaPlugins} or the RxJava GitHub Wiki for information on configuring plugins:
 * <a href="https://github.com/Netflix/RxJava/wiki/Plugins">https://github.com/Netflix/RxJava/wiki/Plugins</a>.
 */
public abstract class RxJavaDefaultSchedulers {

    /**
     * Scheduler to return from {@link rx.schedulers.Schedulers#computation()} or null if default should be
     * used.
     *
     * This instance should be or behave like a stateless singleton;
     */
    public abstract Scheduler getComputationScheduler();

    /**
     * Scheduler to return from {@link rx.schedulers.Schedulers#io()} or null if default should be used.
     *
     * This instance should be or behave like a stateless singleton;
     */
    public abstract Scheduler getIOScheduler();

    /**
     * Scheduler to return from {@link rx.schedulers.Schedulers#newThread()} or null if default should be used.
     *
     * This instance should be or behave like a stateless singleton;
     */
    public abstract Scheduler getNewThreadScheduler();
}
