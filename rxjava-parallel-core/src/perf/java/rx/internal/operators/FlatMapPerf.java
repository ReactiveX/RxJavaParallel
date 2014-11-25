package rx.internal.operators;

import org.openjdk.jmh.annotations.*;
import rx.Observable;
import rx.ParallelObservable;
import rx.functions.Func1;
import rx.jmh.InputWithIncrementingInteger;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * Created by kbahr on 10/20/14.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
public class FlatMapPerf {
    @State(Scope.Thread)
    public static class Input extends InputWithIncrementingInteger {

        @Param({ "1", "1000", "1000000" })
        public int size;

        @Override
        public int getSize() {
            return size;
        }

    }

    @Benchmark
    public void flatMapIntPassthruSync(Input input) throws InterruptedException {
        input.observable.flatMap(new Func1<Integer, ParallelObservable<Integer>>() {

            @Override
            public ParallelObservable<Integer> call(Integer i) {
                return ParallelObservable.just(i);
            }

        }).subscribe(input.observer);
    }

    @Benchmark
    public void flatMapIntPassthruAsync(Input input) throws InterruptedException {
        input.observable.flatMap(new Func1<Integer, ParallelObservable<Integer>>() {

            @Override
            public ParallelObservable<Integer> call(Integer i) {
                return ParallelObservable.just(i).subscribeOn(Schedulers.computation());
            }

        }).subscribe(input.observer);
    }

    @Benchmark
    public void flatMapTwoNestedSync(final Input input) throws InterruptedException {
        ParallelObservable.range(1, 2).flatMap(new Func1<Integer, ParallelObservable<Integer>>() {

            @Override
            public ParallelObservable<Integer> call(Integer i) {
                return input.observable;
            }

        }).subscribe(input.observer);
    }
}
