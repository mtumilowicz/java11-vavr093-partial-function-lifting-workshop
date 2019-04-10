import io.vavr.PartialFunction;

import java.util.function.Predicate;

/**
 * Created by mtumilowicz on 2019-03-04.
 */
class ValidatorAnswer implements PartialFunction<String, Boolean> {

    private final Predicate<String> PATTERN;

    ValidatorAnswer(Predicate<String> pattern) {
        PATTERN = pattern;
    }
    
    @Override
    public Boolean apply(String s) {
        if (isDefinedAt(s)) {
            return true;
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public boolean isDefinedAt(String value) {
        return PATTERN.test(value);
    }
}

class ValidationException extends RuntimeException {

}