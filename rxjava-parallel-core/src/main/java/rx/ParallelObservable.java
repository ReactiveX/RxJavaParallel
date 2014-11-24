package rx;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Func1;
import rx.internal.operators.*;
import rx.internal.util.ScalarSynchronousObservable;
import rx.internal.util.SingleValueParallelObservable;
import rx.observers.SafeSubscriber;
import rx.plugins.RxJavaObservableExecutionHook;
import rx.plugins.RxJavaParallelObservableExecutionHook;
import rx.plugins.RxJavaParallelPlugins;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.Subscriptions;

import java.lang.Boolean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParallelObservable<T> {

    final Observable.OnSubscribe<T> onSubscribe;

    /**
     * Creates an Observable with a Function to execute when it is subscribed to.
     * <p>
     * <em>Note:</em> Use {@link #create(Observable.OnSubscribe)} to create an Observable, instead of this constructor,
     * unless you specifically have a need for inheritance.
     *
     * @param f
     *            {@link Observable.OnSubscribe} to be executed when {@link #subscribe(Subscriber)} is called
     */
    protected ParallelObservable(Observable.OnSubscribe<T> f) {
        this.onSubscribe = f;
    }

    private static final RxJavaParallelObservableExecutionHook hook = RxJavaParallelPlugins.getInstance().getObservableExecutionHook();


    //TODO: I don't think this can actually be done...
    public static <T> ParallelObservable<T> from(Observable<? extends T> observable){
        return null;
    }

    /**
     * Returns an Observable that will execute the specified function when a {@link Subscriber} subscribes to
     * it.
     * <p>
     * <img width="640" height="200" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/create.png" alt="">
     * <p>
     * Write the function you pass to {@code create} so that it behaves as an Observable: It should invoke the
     * Subscriber's {@link Subscriber#onNext onNext}, {@link Subscriber#onError onError}, and
     * {@link Subscriber#onCompleted onCompleted} methods appropriately.
     * <p>
     * A well-formed Observable must invoke either the Subscriber's {@code onCompleted} method exactly once or
     * its {@code onError} method exactly once.
     * <p>
     * See <a href="http://go.microsoft.com/fwlink/?LinkID=205219">Rx Design Guidelines (PDF)</a> for detailed
     * information.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T>
     *            the type of the items that this Observable emits
     * @param f
     *            a function that accepts an {@code Subscriber<T>}, and invokes its {@code onNext},
     *            {@code onError}, and {@code onCompleted} methods as appropriate
     * @return an Observable that, when a {@link Subscriber} subscribes to it, will execute the specified
     *         function
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#create">RxJava wiki: create</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/system.reactive.linq.observable.create.aspx">MSDN: Observable.Create</a>
     */
    public final static <T> ParallelObservable<T> create(Observable.OnSubscribe<T> f) {
        return new ParallelObservable<T>(hook.onCreate(f));
    }

    /**
     * Converts an Array into an Observable that emits the items in the Array.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param t1
     *            the source Array
     * @param <T>
     *            the type of items in the Array and the type of items to be emitted by the resulting Observable
     * @return an Observable that emits each item in the source Array
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212140.aspx">MSDN: Observable.ToObservable</a>
     */
    public final static <T> ParallelObservable<T> from(T... t1) {
        return from(Arrays.asList(t1));
    }

    /**
     * Converts an {@link Iterable} sequence into an Observable that emits the items in the sequence.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code from} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param iterable
     *            the source {@link Iterable} sequence
     * @param <T>
     *            the type of items in the {@link Iterable} sequence and the type of items to be emitted by the
     *            resulting Observable
     * @return an Observable that emits each item in the source {@link Iterable} sequence
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212140.aspx">MSDN: Observable.ToObservable</a>
     */
    public final static <T> ParallelObservable<T> from(Iterable<? extends T> iterable) {
        return create(new OnSubscribeFromIterable<T>(iterable));
    }

    /**
     * Converts an {@link Iterable} sequence into an Observable that operates on the specified Scheduler,
     * emitting each item from the sequence.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/from.s.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param iterable
     *            the source {@link Iterable} sequence
     * @param scheduler
     *            the Scheduler on which the Observable is to emit the items of the Iterable
     * @param <T>
     *            the type of items in the {@link Iterable} sequence and the type of items to be emitted by the
     *            resulting Observable
     * @return an Observable that emits each item in the source {@link Iterable} sequence, on the specified
     *         Scheduler
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#from">RxJava wiki: from</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh212140.aspx">MSDN: Observable.ToObservable</a>
     */
    public final static <T> ParallelObservable<T> from(Iterable<? extends T> iterable, Scheduler scheduler) {
        return from(iterable).subscribeOn(scheduler);
    }

    /**
     * Returns an Observable that emits a sequence of Integers within a specified range.
     * <p>
     * <img width="640" height="195" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/range.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code range} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param start
     *            the value of the first Integer in the sequence
     * @param count
     *            the number of sequential Integers to generate
     * @return an Observable that emits a range of sequential Integers
     * @throws IllegalArgumentException
     *             if {@code count} is less than zero, or if {@code start} + {@code count} &minus; 1 exceeds
     *             {@code Integer.MAX_VALUE}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#range">RxJava wiki: range</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229460.aspx">MSDN: Observable.Range</a>
     */
    public final static ParallelObservable<Integer> range(int start, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count can not be negative");
        }
        if (count == 0) {
            return ParallelObservable.empty();
        }
        if (start > Integer.MAX_VALUE - count + 1) {
            throw new IllegalArgumentException("start + count can not exceed Integer.MAX_VALUE");
        }
        if(count == 1) {
            return ParallelObservable.just(start);
        }
        return ParallelObservable.create(new OnSubscribeRange(start, start + (count - 1)));
    }

    /**
     * Returns an Observable that emits no items to the {@link Observer} and immediately invokes its
     * {@link Observer#onCompleted onCompleted} method.
     * <p>
     * <img width="640" height="190" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/empty.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code empty} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T>
     *            the type of the items (ostensibly) emitted by the Observable
     * @return an Observable that emits no items to the {@link Observer} but immediately invokes the
     *         {@link Observer}'s {@link Observer#onCompleted() onCompleted} method
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#empty-error-and-never">RxJava wiki: empty</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229670.aspx">MSDN: Observable.Empty</a>
     */
    public final static <T> ParallelObservable<T> empty() {
        return from(new ArrayList<T>());
    }

    /**
     * Returns an Observable that emits a single item and then completes.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/just.png" alt="">
     * <p>
     * To convert any object into an Observable that emits that object, pass that object into the {@code just}
     * method.
     * <p>
     * This is similar to the {@link #from(java.lang.Object[])} method, except that {@code from} will convert
     * an {@link Iterable} object into an Observable that emits each of the items in the Iterable, one at a
     * time, while the {@code just} method converts an Iterable into an Observable that emits the entire
     * Iterable as a single item.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code just} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param value
     *            the item to emit
     * @param <T>
     *            the type of that item
     * @return an Observable that emits {@code value} as a single item and then completes
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Creating-Observables#just">RxJava wiki: just</a>
     */
    public final static <T> ParallelObservable<T> just(final T value) {
        return SingleValueParallelObservable.create(value);
    }

    /**
     * Returns an Observable that emits a single item, a list composed of all the items emitted by the source
     * Observable.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/toList.png" alt="">
     * <p>
     * Normally, an Observable that returns multiple items will do so by invoking its {@link Observer}'s
     * {@link Observer#onNext onNext} method for each such item. You can change this behavior, instructing the
     * Observable to compose a list of all of these items and then to invoke the Observer's {@code onNext}
     * function once, passing it the entire list, by calling the Observable's {@code toList} method prior to
     * calling its {@link #subscribe} method.
     * <p>
     * Be careful not to use this operator on Observables that emit infinite or very large numbers of items, as
     * you do not have the option to unsubscribe.
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator does not support backpressure as by intent it is requesting and buffering everything.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code toList} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @return an Observable that emits a single item: a List containing all of the items emitted by the source
     *         Observable
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Mathematical-and-Aggregate-Operators#tolist">RxJava wiki: toList</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh211848.aspx">MSDN: Observable.ToList</a>
     */
    public final ParallelObservable<List<T>> toList() {
        return lift(new OperatorToObservableList<T>());
    }

    /**
     * Flattens two Observables into a single Observable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/merge.png" alt="">
     * <p>
     * You can combine items emitted by multiple Observables so that they appear as a single Observable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param t1
     *            an Observable to be merged
     * @param t2
     *            an Observable to be merged
     * @return an Observable that emits all of the items emitted by the source Observables
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge">RxJava wiki: merge</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/hh229099.aspx">MSDN: Observable.Merge</a>
     */
    @SuppressWarnings("unchecked")
    public final static <T> ParallelObservable<T> merge(ParallelObservable<? extends T> t1, ParallelObservable<? extends T> t2) {
        return merge(from(Arrays.asList(t1, t2)));
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
        return merge(from(observables));
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
        return merge(from(observables), maxConcurrent);
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
        return merge(from(observables, scheduler), maxConcurrent);
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
        return merge(from(observables, scheduler));
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
        return source.lift(new OperatorParallelMerge<T>());
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
        return source.lift(new OperatorParallelMergeMaxConcurrent<T>(maxConcurrent));
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
        return lift(new OperatorParallelMap<T, R>(func));
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
     * Asynchronously subscribes Observers to this Observable on the specified {@link Scheduler}.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/subscribeOn.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param scheduler
     *            the {@link Scheduler} to perform subscription actions on
     * @return the source Observable modified so that its subscriptions happen on the
     *         specified {@link Scheduler}
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable-Utility-Operators#subscribeon">RxJava wiki: subscribeOn</a>
     * @see <a href="http://www.grahamlea.com/2014/07/rxjava-threading-examples/">RxJava Threading Examples</a>
     * @see #observeOn
     */
    public final ParallelObservable<T> subscribeOn(Scheduler scheduler) {
        return nest().lift(new OperatorSubscribeOn<T>(scheduler));
    }


    /**
     * Converts the source {@code Observable<T>} into an {@code Observable<Observable<T>>} that emits the
     * source Observable as its single emission.
     * <p>
     * <img width="640" height="350" src="https://raw.github.com/wiki/Netflix/RxJava/images/rx-operators/nest.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code nest} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @return an Observable that emits a single item: the source Observable
     * @since 0.17
     */
    public final ParallelObservable<ParallelObservable<T>> nest() {
        return just(this);
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
    public final <R> ParallelObservable<R> lift(final Observable.Operator<? extends R, ? super T> lift) {
        return new ParallelObservable<R>(new Observable.OnSubscribe<R>() {
            @Override
            public void call(Subscriber<? super R> o) {
                try {
                    Subscriber<? super T> st = hook.onLift(lift).call(o);
                    // new Subscriber created and being subscribed with so 'onStart' it
                    st.onStart();
                    onSubscribe.call(st);
                } catch (Throwable e) {
                    // localized capture of errors rather than it skipping all operators
                    // and ending up in the try/catch of the subscribe method which then
                    // prevents onErrorResumeNext and other similar approaches to error handling
                    if (e instanceof OnErrorNotImplementedException) {
                        throw (OnErrorNotImplementedException) e;
                    }
                    o.onError(e);
                }
            }
        });
    }

    /**
     * Subscribes to an Observable and invokes {@link OnSubscribe} function without any contract protection,
     * error handling, unsubscribe, or execution hooks.
     * <p>
     * Use this only for implementing an {@link Operator} that requires nested subscriptions. For other
     * purposes, use {@link #subscribe(Subscriber)} which ensures the Rx contract and other functionality.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code unsafeSubscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param subscriber
     *              the Subscriber that will handle emissions and notifications from the Observable
     * @return a {@link Subscription} reference with which the {@link Subscriber} can stop receiving items
     *         before the Observable has completed
     * @since 0.17
     */
    public final Subscription unsafeSubscribe(Subscriber<? super T> subscriber) {
        try {
            // new Subscriber so onStart it
            subscriber.onStart();
            // allow the hook to intercept and/or decorate
            hook.onSubscribeStart(this, onSubscribe).call(subscriber);
            return hook.onSubscribeReturn(subscriber);
        } catch (Throwable e) {
            // special handling for certain Throwable/Error/Exception types
            Exceptions.throwIfFatal(e);
            // if an unhandled error occurs executing the onSubscribe we will propagate it
            try {
                subscriber.onError(hook.onSubscribeError(e));
            } catch (OnErrorNotImplementedException e2) {
                // special handling when onError is not implemented ... we just rethrow
                throw e2;
            } catch (Throwable e2) {
                // if this happens it means the onError itself failed (perhaps an invalid function implementation)
                // so we are unable to propagate the error correctly and will just throw
                RuntimeException r = new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e2);
                // TODO could the hook be the cause of the error in the on error handling.
                hook.onSubscribeError(r);
                // TODO why aren't we throwing the hook's return value.
                throw r;
            }
            return Subscriptions.empty();
        }
    }

    /**
     * Subscribes to an Observable and provides an Observer that implements functions to handle the items the
     * Observable emits and any error or completion notification it issues.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code subscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param observer
     *             the Observer that will handle emissions and notifications from the Observable
     * @return a {@link Subscription} reference with which the {@link Observer} can stop receiving items before
     *         the Observable has completed
     * @see <a href="https://github.com/Netflix/RxJava/wiki/Observable#onnext-oncompleted-and-onerror">RxJava wiki: onNext, onCompleted, and onError</a>
     */
    public final Subscription subscribe(final Observer<? super T> observer) {
        return subscribe(new Subscriber<T>() {

            @Override
            public void onCompleted() {
                observer.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                observer.onError(e);
            }

            @Override
            public void onNext(T t) {
                observer.onNext(t);
            }

        });
    }

    /**
     * Subscribes to an Observable and provides a Subscriber that implements functions to handle the items the
     * Observable emits and any error or completion notification it issues.
     * <p>
     * A typical implementation of {@code subscribe} does the following:
     * <ol>
     * <li>It stores a reference to the Subscriber in a collection object, such as a {@code List<T>} object.</li>
     * <li>It returns a reference to the {@link Subscription} interface. This enables Subscribers to
     * unsubscribe, that is, to stop receiving items and notifications before the Observable completes, which
     * also invokes the Subscriber's {@link Subscriber#onCompleted onCompleted} method.</li>
     * </ol><p>
     * An {@code Observable<T>} instance is responsible for accepting all subscriptions and notifying all
     * Subscribers. Unless the documentation for a particular {@code Observable<T>} implementation indicates
     * otherwise, Subscriber should make no assumptions about the order in which multiple Subscribers will
     * receive their notifications.
     * <p>
     * For more information see the
     * <a href="https://github.com/Netflix/RxJava/wiki/Observable">RxJava wiki</a>.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code subscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param subscriber
     *            the {@link Subscriber} that will handle emissions and notifications from the Observable
     * @return a {@link Subscription} reference with which Subscribers that are {@link Observer}s can
     *         unsubscribe from the Observable
     * @throws IllegalStateException
     *             if {@code subscribe} is unable to obtain an {@code OnSubscribe<>} function
     * @throws IllegalArgumentException
     *             if the {@link Subscriber} provided as the argument to {@code subscribe} is {@code null}
     * @throws OnErrorNotImplementedException
     *             if the {@link Subscriber}'s {@code onError} method is null
     * @throws RuntimeException
     *             if the {@link Subscriber}'s {@code onError} method itself threw a {@code Throwable}
     */
    public final Subscription subscribe(Subscriber<? super T> subscriber) {
        // validate and proceed
        if (subscriber == null) {
            throw new IllegalArgumentException("observer can not be null");
        }
        if (onSubscribe == null) {
            throw new IllegalStateException("onSubscribe function can not be null.");
            /*
             * the subscribe function can also be overridden but generally that's not the appropriate approach
             * so I won't mention that in the exception
             */
        }

        // new Subscriber so onStart it
        subscriber.onStart();

        /*
         * See https://github.com/Netflix/RxJava/issues/216 for discussion on "Guideline 6.4: Protect calls
         * to user code from within an Observer"
         */
        // if not already wrapped
        if (!(subscriber instanceof SafeSubscriber)) {
            // assign to `observer` so we return the protected version
            subscriber = new SafeSubscriber<T>(subscriber);
        }

        // The code below is exactly the same an unsafeSubscribe but not used because it would add a sigificent depth to alreay huge call stacks.
        try {
            // allow the hook to intercept and/or decorate
            hook.onSubscribeStart(this, onSubscribe).call(subscriber);
            return hook.onSubscribeReturn(subscriber);
        } catch (Throwable e) {
            // special handling for certain Throwable/Error/Exception types
            Exceptions.throwIfFatal(e);
            // if an unhandled error occurs executing the onSubscribe we will propagate it
            try {
                subscriber.onError(hook.onSubscribeError(e));
            } catch (OnErrorNotImplementedException e2) {
                // special handling when onError is not implemented ... we just rethrow
                throw e2;
            } catch (Throwable e2) {
                // if this happens it means the onError itself failed (perhaps an invalid function implementation)
                // so we are unable to propagate the error correctly and will just throw
                RuntimeException r = new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e2);
                // TODO could the hook be the cause of the error in the on error handling.
                hook.onSubscribeError(r);
                // TODO why aren't we throwing the hook's return value.
                throw r;
            }
            return Subscriptions.empty();
        }
    }
}