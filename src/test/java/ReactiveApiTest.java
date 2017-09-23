import io.reactivex.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class ReactiveApiTest {
    @Test
    public void testHelloWorld() {
        Observable.just("Hello World").subscribe(System.out::println);
    }

    @Test
    public void testObservablePass() {
        Observable<Integer> numbers = Observable.just(1, 2, 3, 4, 5);
        Observable.just(numbers).subscribe(System.out::println);
    }

    @Test
    public void testMap() {
        Observable.just(1,2,3).map(x -> x*10).subscribe(System.out::println);
    }

    @Test
    public void testRangePrintsEveryHalfASec() {
        Observable.intervalRange(0, 10, 0,500, TimeUnit.MILLISECONDS)
                .map(x -> x*10)
                .blockingSubscribe(System.out::println);    // blocking ensures that we are running on same thread
    }

    @Test
    public void testConcatMap() {
        Observable.just(1,2,3)
            .concatMap(x -> Observable.just(x * 10))    // executes at the same time
            .delay(500, TimeUnit.MILLISECONDS)
            .blockingSubscribe(System.out::println);
    }

    @Test
    public void testCreateSourcesFromJust() {
        // Check README.md for difference between these
        Flowable.just("Hello", "World");
        Observable.just("Hello", "World");
        Maybe.just("Hello");
        Single.just("Hello");

        // Arrays is also possible
        List<String> strings = Arrays.asList("Hello", "World");

        Flowable.fromArray(strings);
        Flowable.fromIterable(strings);

        Observable.fromArray(strings);
        Observable.fromIterable(strings);
    }

    @Test
    public void testCreateSourcesFromCallables() {

        Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception { // throw exception, callable can handle exceptions
                return "a sync behavior that returns a single value";
                // return apiCall(httpRequest).execute();   <-- ie. this throws exception
                // onError is called if it is, otherwise onComplete is
            }
        });

        Flowable.fromCallable(() -> "Hello");
        Observable.fromCallable(() -> "Hello");
        Maybe.fromCallable(() -> "Hello");
        Single.fromCallable(() -> "Hello");
        Completable.fromCallable(() -> "Ignored");  // no result for completable

        // additional methods on Maybe and Completable (Because they don't provide results)
        Maybe.fromAction(() -> System.out.println("Hello"));
        Maybe.fromRunnable(() -> System.out.println("Hello"));

        Completable.fromAction(() -> System.out.println("Hello"));
        Completable.fromRunnable(() -> System.out.println("Hello"));
    }

    @Test
    public void testCreateSourcesFromCreate() {

    }
}
