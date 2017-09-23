import io.reactivex.Observable;

// problem; if there are multiple threads changing the data.
// managing state becomes a problem
// Reactive allows us to not worry about this
public interface UserManager {
    User getUser(); // manage when is the appropriate time to display those users
    void setName(String name);
    void setAge(int age);
}
