package rx.plugins;

/**
 * Default no-op implementation of {@link RxJavaParallelObservableExecutionHookDefault}
 */
/*package*/ class RxJavaParallelObservableExecutionHookDefault extends RxJavaParallelObservableExecutionHook {
    private static RxJavaParallelObservableExecutionHookDefault INSTANCE = new RxJavaParallelObservableExecutionHookDefault();

    public static RxJavaParallelObservableExecutionHookDefault getInstance() {
        return INSTANCE;
    }
}
