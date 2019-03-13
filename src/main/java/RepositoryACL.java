import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Created by mtumilowicz on 2019-03-04.
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class RepositoryACL {
    Repository repository = new Repository();
    
    // implement using function lifting
    Try<User> findById(int id) {
        return Try.failure(new RuntimeException());
    }
}
