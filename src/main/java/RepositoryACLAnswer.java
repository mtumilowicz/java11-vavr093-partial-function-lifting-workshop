import io.vavr.Function1;
import io.vavr.control.Option;

import java.util.function.Function;

/**
 * Created by mtumilowicz on 2019-03-04.
 */
class RepositoryACLAnswer extends RepositoryACL {

    Function<Integer, Option<User>> findById = Function1.lift(repository::findById);

    @Override
    Option<User> findById(int id) {
        return findById.apply(id);
    }
}
