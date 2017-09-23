import io.reactivex.Completable;
import io.reactivex.Observable;

public interface UserManagerReactive {
    Observable<User> getUser(); // no need to manage, an observable let us know that there was a change

    // they don't return data so Completable
    Completable setName(String name);

    // they don't return data so Completable
    Completable setAge(int age);
}
