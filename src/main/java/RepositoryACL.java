import io.vavr.control.Option;

/**
 * Created by mtumilowicz on 2019-03-04.
 */
class RepositoryACL {
    final Repository repository = new Repository();
    
    // implement using function lifting
    Option<User> findById(int id) {
        return Option.none();
    }
}
