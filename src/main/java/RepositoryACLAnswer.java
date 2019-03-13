import io.vavr.Function1;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Created by mtumilowicz on 2019-03-04.
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class RepositoryACLAnswer {

    Repository repository = new Repository();
    
    Function1<Integer, Try<User>> findById = Function1.liftTry(repository::findById);
    
    Try<User> findById(int id) {
        return findById.apply(id);
    }
}
