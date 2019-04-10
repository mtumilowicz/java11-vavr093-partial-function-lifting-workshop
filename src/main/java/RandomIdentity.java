import io.vavr.PartialFunction;

/**
 * Created by mtumilowicz on 2019-03-05.
 */
class RandomIdentity implements PartialFunction<Integer, Integer> {

    @Override
    public Integer apply(Integer o) {
        //if defined: x -> x, otherwise random
        return null;
    }

    @Override
    public boolean isDefinedAt(Integer value) {
        // defined only on 0..3
        return false;
    }
}