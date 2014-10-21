package main.java.rx;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;

import java.lang.Boolean;

public class ParallelObservable<T> {

    public static <T> ParallelObservable<T> from(Observable<? extends T> observable){
        return null;
    }

    /**
     * Flattens an Iterable of ParallelObservables into one ParallelObservable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine the items emitted by multiple ParallelObservables so that they appear as a single ParallelObservable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param observables
     *            the Iterable of ParallelObservables
     * @return an ParallelObservable that emits items that are the result of flattening the items emitted by the
     *         Observables in the Iterable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229590.aspx">MSDN: Observable.Merge</a>
     */
    public static <T> ParallelObservable<T> merge(Iterable<? extends ParallelObservable<? extends T>> observables){
        return null;
    }

    /**
     * Flattens an Iterable of Observables into one Observable, without any transformation, while limiting the
     * number of concurrent subscriptions to these Observables.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine the items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param observables
     *            the Iterable of ParallelObservables
     * @param maxConcurrent
     *            the maximum number of ParallelObservables that may be subscribed to concurrently
     * @return an ParallelObservable that emits items that are the result of flattening the items emitted by the
     *         ParallelObservables in the Iterable
     * @throws IllegalArgumentException
     *             if {@code maxConcurrent} is less than or equal to 0
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229923.aspx">MSDN: Observable.Merge</a>
     */
    public static <T> ParallelObservable<T> merge(Iterable<? extends ParallelObservable<? extends T>> observables, int maxConcurrent){
        return null;
    }

    /**
     * Flattens an Iterable of Observables into one Observable, without any transformation, while limiting the
     * number of concurrent subscriptions to these Observables, and subscribing to these Observables on a
     * specified Scheduler.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine the items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param observables
     *            the Iterable of Observables
     * @param maxConcurrent
     *            the maximum number of Observables that may be subscribed to concurrently
     * @param scheduler
     *            the Scheduler on which to traverse the Iterable of Observables
     * @return an Observable that emits items that are the result of flattening the items emitted by the
     *         Observables in the Iterable
     * @throws IllegalArgumentException
     *             if {@code maxConcurrent} is less than or equal to 0
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh244329.aspx">MSDN: Observable.Merge</a>
     */
    public static <T> ParallelObservable<T> merge(Iterable<? extends ParallelObservable<? extends T>> observables, int maxConcurrent, Scheduler scheduler){
        return null;
    }

    /**
     * Flattens an Iterable of Observables into one Observable, without any transformation, subscribing to these
     * Observables on a specified Scheduler.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine the items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param observables
     *            the Iterable of Observables
     * @param scheduler
     *            the Scheduler on which to traverse the Iterable of Observables
     * @return an Observable that emits items that are the result of flattening the items emitted by the
     *         Observables in the Iterable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh244336.aspx">MSDN: Observable.Merge</a>
     */
    public static <T> ParallelObservable<T> merge(Iterable<? extends ParallelObservable<? extends T>> observables, Scheduler scheduler){
        return null;
    }

    /**
     * Flattens an Observable that emits Observables into a single Observable that emits the items emitted by
     * those Observables, without any transformation.
     * <p>
     * <img width="640" height="370" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.oo.png" alt="">
     * <p>
     * You can combine the items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param source
     *            an Observable that emits Observables
     * @return an Observable that emits items that are the result of flattening the Observables emitted by the
     *         {@code source} Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    public static <T> ParallelObservable<T> merge(ParallelObservable<? extends ParallelObservable<? extends T>> source){
        return null;
    }

    /**
     * Flattens an Observable that emits Observables into a single Observable that emits the items emitted by
     * those Observables, without any transformation, while limiting the maximum number of concurrent
     * subscriptions to these Observables.
     * <p>
     * <img width="640" height="370" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.oo.png" alt="">
     * <p>
     * You can combine the items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param source
     *            an Observable that emits Observables
     * @param maxConcurrent
     *            the maximum number of Observables that may be subscribed to concurrently
     * @return an Observable that emits items that are the result of flattening the Observables emitted by the
     *         {@code source} Observable
     * @throws IllegalArgumentException
     *             if {@code maxConcurrent} is less than or equal to 0
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211914.aspx">MSDN: Observable.Merge</a>
     */
    public static <T> ParallelObservable<T> merge(ParallelObservable<? extends ParallelObservable<? extends T>> source, int maxConcurrent){
        return null;
    }

    /**
     * Returns an Observable that applies a specified function to each item emitted by the source Observable and
     * emits the results of these function applications.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/map.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code map} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param func
     *            a function to apply to each item emitted by the Observable
     * @return an Observable that emits the items from the source Observable, transformed by the specified
     *         function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#map">RxJava wiki: map</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh244306.aspx">MSDN: Observable.Select</a>
     */
    public final <R> ParallelObservable<R> map(Func1<? super T, ? extends R> func) {
        return null;
    }

    /**
     * Filters items emitted by an Observable by only emitting those that satisfy a specified predicate.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/filter.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code filter} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param predicate
     *            a function that evaluates each item emitted by the source Observable, returning {@code true}
     *            if it passes the filter
     * @return an Observable that emits only those items emitted by the source Observable that the filter
     *         evaluates as {@code true}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#filter-or-where">RxJava wiki: filter</a>
     */
    public final ParallelObservable<T> filter(Func1<? super T, Boolean> predicate){
        return null;
    }

    /**
     * Returns an Observable that emits items based on applying a function that you supply to each item emitted
     * by the source Observable, where that function returns an Observable, and then merging those resulting
     * Observables and emitting the results of this merger.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/flatMap.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code flatMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param selector
     *            a function that, when applied to an item emitted by the source Observable, returns an
     *            Observable
     * @return an Observable that emits the result of applying the transformation function to each item emitted
     *         by the source Observable and merging the results of the Observables obtained from this
     *         transformation
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#flatmap-and-concatmap">RxJava wiki: flatMap</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.selectmany.aspx">MSDN: Observable.SelectMany</a>
     */
    public final <R> ParallelObservable<R> flatMap(Func1<? super T, ? extends ParallelObservable<? extends R>> selector) {
        return null;
    }

    /**
     * Returns an Observable that skips the first {@code num} items emitted by the source Observable and emits
     * the remainder.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/skip.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code skip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param num
     *            the number of items to skip
     * @return an Observable that is identical to the source Observable except that it does not emit the first
     *         {@code num} items that the source Observable emits
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#skip">RxJava wiki: skip</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229847.aspx">MSDN: Observable.skip</a>
     */
    public final ParallelObservable<T> skip(int num) {
        return null;
    }

    /**
     * Returns an Observable that skips all items emitted by the source Observable as long as a specified
     * condition holds true, but emits all further source items as soon as the condition becomes false.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/skipWhile.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code skipWhile} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param predicate
     *            a function to test each item emitted from the source Observable
     * @return an Observable that begins emitting items emitted by the source Observable when the specified
     *         predicate becomes false
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#skipwhile-and-skipwhilewithindex">RxJava wiki: skipWhile</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229685.aspx">MSDN: Observable.SkipWhile</a>
     */
    public final ParallelObservable<T> skipWhile(Func1<? super T, Boolean> predicate) {
        return null;
    }

    /**
     * Returns an Observable that emits only the first {@code num} items emitted by the source Observable.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/take.png" alt="">
     * <p>
     * This method returns an Observable that will invoke a subscribing {@link rx.Observer}'s
     * {@link rx.Observer#onNext onNext} function a maximum of {@code num} times before invoking
     * {@link rx.Observer#onCompleted onCompleted}.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code take} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param num
     *            the maximum number of items to emit
     * @return an Observable that emits only the first {@code num} items emitted by the source Observable, or
     *         all of the items from the source Observable if that Observable emits fewer than {@code num} items
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Filtering-Observables#take">RxJava wiki: take</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229852.aspx">MSDN: Observable.Take</a>
     */
    public final ParallelObservable<T> take(int num) {
        return null;
    }

    /**
     * Returns an Observable that emits items emitted by the source Observable so long as each item satisfied a
     * specified condition, and then completes as soon as this condition is not satisfied.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/takeWhile.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code takeWhile} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param predicate
     *            a function that evaluates an item emitted by the source Observable and returns a Boolean
     * @return an Observable that emits the items from the source Observable so long as each item satisfies the
     *         condition defined by {@code predicate}, then completes
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Conditional-and-Boolean-Operators#takewhile-and-takewhilewithindex">RxJava wiki: takeWhile</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh244231.aspx">MSDN: Observable.TakeWhile</a>
     */
    public final ParallelObservable<T> takeWhile(Func1<? super T, Boolean> predicate) {
        return null;
    }

    /**
     * Lifts a function to the current Observable and returns a new Observable that when subscribed to will pass
     * the values of the current Observable through the Operator function.
     * <p>
     * In other words, this allows chaining Observers together on an Observable for acting on the values within
     * the Observable.
     * <p> {@code
     * observable.map(...).filter(...).take(5).lift(new OperatorA()).lift(new OperatorB(...)).subscribe()
     * }
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code lift} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param lift the Operator that implements the Observable-operating function to be applied to the source
     *             Observable
     * @return an Observable that is the result of applying the lifted Operator to the source Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Implementing-Your-Own-Operators">RxJava wiki: Implementing Your Own Operators</a>
     * @since 0.17
     */
    public final <R> ParallelObservable<R> lift(Observable.Operator<? extends R, ? extends T> lift){
        return null;
    }

}