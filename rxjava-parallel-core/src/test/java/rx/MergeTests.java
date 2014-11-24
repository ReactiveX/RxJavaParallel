package rx;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import rx.CovarianceTest.HorrorMovie;
import rx.CovarianceTest.Media;
import rx.CovarianceTest.Movie;

/**
 * Created by kbahr on 10/20/14.
 */
public class MergeTests {
    /**
     * This won't compile if super/extends isn't done correctly on generics
     */
    @Test
    public void testCovarianceOfMerge() {
        ParallelObservable<HorrorMovie> horrors = ParallelObservable.from(new HorrorMovie());
        ParallelObservable<ParallelObservable<HorrorMovie>> metaHorrors = ParallelObservable.just(horrors);
        ParallelObservable.<Media> merge(metaHorrors);
    }

    @Test
    public void testMergeCovariance() {
        ParallelObservable<Media> o1 = ParallelObservable.<Media> from(new HorrorMovie(), new Movie());
        ParallelObservable<Media> o2 = ParallelObservable.from(new Media(), new HorrorMovie());

        ParallelObservable<ParallelObservable<Media>> os = ParallelObservable.from(o1, o2);

        final List<Media> values = new ArrayList();
        ParallelObservable.merge(os).toList().unsafeSubscribe(new Subscriber<List<Media>>() {
            @Override
            public void onCompleted() {
                //nothing
            }

            @Override
            public void onError(Throwable e) {
                //nothing
            }

            @Override
            public void onNext(List<Media> medias) {
                values.addAll(medias);
            }
        });

        assertEquals(4, values.size());
    }

    @Test
    public void testMergeCovariance2() {
        ParallelObservable<Media> o1 = ParallelObservable.from(new HorrorMovie(), new Movie(), new Media());
        ParallelObservable<Media> o2 = ParallelObservable.from(new Media(), new HorrorMovie());

        ParallelObservable<ParallelObservable<Media>> os = ParallelObservable.from(o1, o2);

        final List<Media> values = new ArrayList();
        ParallelObservable.merge(os).toList().unsafeSubscribe(new Subscriber<List<Media>>() {
            @Override
            public void onCompleted() {
                //nothing
            }

            @Override
            public void onError(Throwable e) {
                //nothing
            }

            @Override
            public void onNext(List<Media> medias) {
                values.addAll(medias);
            }
        });

        assertEquals(5, values.size());
    }

    @Test
    public void testMergeCovariance3() {
        ParallelObservable<Movie> o1 = ParallelObservable.from(new HorrorMovie(), new Movie());
        ParallelObservable<Media> o2 = ParallelObservable.from(new Media(), new HorrorMovie());

        final List<Media> values = new ArrayList();
        ParallelObservable.merge(o1, o2).toList().unsafeSubscribe(new Subscriber<List<Media>>() {
            @Override
            public void onCompleted() {
                //nothing
            }

            @Override
            public void onError(Throwable e) {
                //nothing
            }

            @Override
            public void onNext(List<Media> medias) {
                values.addAll(medias);
            }
        });

        assertTrue(values.get(0) instanceof HorrorMovie);
        assertTrue(values.get(1) instanceof Movie);
        assertTrue(values.get(2) instanceof Media);
        assertTrue(values.get(3) instanceof HorrorMovie);
    }

    @Test
    public void testMergeCovariance4() {

        ParallelObservable<Movie> o1 = ParallelObservable.create(new Observable.OnSubscribe<Movie>() {

            @Override
            public void call(Subscriber<? super Movie> o) {
                o.onNext(new HorrorMovie());
                o.onNext(new Movie());
                //                o.onNext(new Media()); // correctly doesn't compile
                o.onCompleted();
            }
        });

        ParallelObservable<Media> o2 = ParallelObservable.from(new Media(), new HorrorMovie());

        final List<Media> values = new ArrayList();
        ParallelObservable.merge(o1, o2).toList().unsafeSubscribe(new Subscriber<List<Media>>() {
            @Override
            public void onCompleted() {
                //nothing
            }

            @Override
            public void onError(Throwable e) {
                //nothing
            }

            @Override
            public void onNext(List<Media> medias) {
                values.addAll(medias);
            }
        });

        assertTrue(values.get(0) instanceof HorrorMovie);
        assertTrue(values.get(1) instanceof Movie);
        assertTrue(values.get(2) instanceof Media);
        assertTrue(values.get(3) instanceof HorrorMovie);
    }
}
