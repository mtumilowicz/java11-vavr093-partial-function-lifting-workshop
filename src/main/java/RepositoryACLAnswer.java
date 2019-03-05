import io.vavr.Function1;
import io.vavr.control.Try;

/**
 * Created by mtumilowicz on 2019-03-04.
 */
class RepositoryACLAnswer extends RepositoryACL {

    Function1<Integer, Try<User>> findById = Function1.liftTry(repository::findById);

    @Override
    Try<User> findById(int id) {
        return findById.apply(id);
    }
}
