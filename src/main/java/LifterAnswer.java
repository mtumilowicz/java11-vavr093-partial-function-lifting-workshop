import io.vavr.PartialFunction;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.function.Function;

/**
 * Created by mtumilowicz on 2019-03-05.
 */
class LifterAnswer {
    static <T, R> Function<T, Option<R>> lift(PartialFunction<T, R> function) {
        return x -> Option.when(function.isDefinedAt(x), () -> function.apply(x));
    }

    static <T, R> Function<T, Option<R>> lift(Function<T, R> function) {
        return x -> Try.of(() -> function.apply(x)).toOption();
    }
}
