import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class ObservableTest {
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
}
