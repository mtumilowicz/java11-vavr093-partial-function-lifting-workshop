import io.vavr.PartialFunction;

/**
 * Created by mtumilowicz on 2019-03-05.
 */
class Increment implements PartialFunction<Integer, Integer> {
    @Override
    public Integer apply(Integer integer) {
        //if defined: x -> x + 1, otherwise -1
        return null;
    }

    @Override
    public boolean isDefinedAt(Integer value) {
        // defined only on 0..3
        // use Range<Integer> from guava or 1..3 groovy range syntax
        return false;
    }
}
