import io.vavr.PartialFunction;

import java.util.function.Predicate;

/**
 * Created by mtumilowicz on 2019-03-05.
 */
class Validator implements PartialFunction<String, Boolean> {

    private final Predicate<String> PATTERN;

    Validator(Predicate<String> pattern) {
        PATTERN = pattern;
    }
    
    @Override
    public Boolean apply(String s) {
        // if defined - true, otherwise ValidationException
        return null;
    }

    @Override
    public boolean isDefinedAt(String value) {
        // true if complied with pattern, otherwise - false
        return PATTERN.test(value);
    }
}
