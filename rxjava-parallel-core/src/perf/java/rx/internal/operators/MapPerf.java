package rx.internal.operators;

import org.openjdk.jmh.annotations.*;
import rx.Observable;
import rx.functions.Func1;
import rx.jmh.InputWithIncrementingInteger;

import java.util.concurrent.TimeUnit;

/**
 * Created by kbahr on 10/20/14.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class MapPerf {
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
    public void mapPassThruViaLift(Input input) throws InterruptedException {
        input.observable.lift(MAP_OPERATOR).subscribe(input.observer);
    }

    @Benchmark
    public void mapPassThru(Input input) throws InterruptedException {
        input.observable.map(IDENTITY_FUNCTION).subscribe(input.observer);
    }

    private static final Func1<Integer, Integer> IDENTITY_FUNCTION = new Func1<Integer, Integer>() {
        @Override
        public Integer call(Integer value) {
            return value;
        }
    };

    private static final Observable.Operator<Integer, Integer> MAP_OPERATOR = new OperatorParallelMap<Integer, Integer>(IDENTITY_FUNCTION);

}
