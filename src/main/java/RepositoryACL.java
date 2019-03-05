import io.vavr.control.Try;

/**
 * Created by mtumilowicz on 2019-03-04.
 */
class RepositoryACL {
    final Repository repository = new Repository();
    
    // implement using function lifting
    Try<User> findById(int id) {
        return Try.failure(new RuntimeException());
    }
}
