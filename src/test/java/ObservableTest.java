import io.reactivex.Observable;
import org.junit.Test;

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
}
