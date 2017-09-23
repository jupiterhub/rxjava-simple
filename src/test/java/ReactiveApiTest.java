import com.sun.deploy.net.HttpResponse;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
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
        Observable.create(new ObservableOnSubscribe<String>() {

            // emmitter is the person listening
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("Hello");
                e.onComplete();
            }
        });

        // Lambda version
        Observable.create(e -> {
            // For callable you cannot call onNext multiple times
            e.onNext("Hello");
            e.onNext("World");
            e.onComplete();
        });
    }

    @Test
    public void testCreateSourcesWithNetworkCall() {
        // something like this
        /*
        Observable.create(e -> {
            // Call call = client.newCall(request);
            call.execute(new Callback() {
                void onResponse(HttpResponse resp) {
                    e.onNext(resp.getResponseBody()); // <------ Call the event after the response
                    e.onComplete();
                }

                void onFailure(HttpResponse resp) {
                    e.onError(); // <------- Call the event after response
                }
            })
        });
        */
    }

    @Test
    public void testDisposableObserver() {
        // by default there is a 4th method called onSubscribe(Disposable d)
        // This class handles that for you

        // applies to all types Single, Maybe, Flowable, Completable
        Observable<String> hello = Observable.just("Hello");

        DisposableObserver observer = new DisposableObserver<String>() {
            @Override public void onNext(String s) { }
            @Override public void onError(Throwable e) { }
            @Override public void onComplete() { }
        };

        // subscribeWith will return the disposable object
        DisposableObserver disposableObserver = hello.subscribeWith(observer);
        disposableObserver.dispose();
    }

    @Test
    public void userReactive() {
        UserManagerReactive userManagerReactive = new UserManagerReactive() {
            @Override
            public Observable<User> getUser() {
                return null;
            }

            @Override
            public Completable setName(String name) {
                return null;
            }

            @Override
            public Completable setAge(int age) {
                return null;
            }
        };

        Observable<User> user = userManagerReactive.getUser();

        // use the background thread good for IO
        // There are more @ http://reactivex.io/RxJava/2.x/javadoc/
        user.observeOn(Schedulers.io());
    }

    @Test
    public void testHttpResponse() {
        // network call is async
        // create an observeOn and wait for the response
        // that way the main thread is not blocked
        // and the data can be rendered once it is ready

        Observable<String> result = Observable.fromCallable(() -> {
            // Pretend we return HTTP RESPONSE ----> client.callApi();
            return new HttpResponse();    // sample we have an api call
        })
        .subscribeOn(Schedulers.io())  // subscribe on backgoround thread
        .map(response -> response.getBody())   // imagine we are calling response body
        .observeOn(Schedulers.single()); // observer the result on the main thread
        // .map(...);       order matters. if placed here, it will execute on main thread
    }



    class HttpResponse {
        public String getBody() {
            return "response content";
        }
    }
}
