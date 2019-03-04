import com.google.common.collect.Range;
import io.vavr.PartialFunction;

import java.util.Random;

/**
 * Created by mtumilowicz on 2019-03-05.
 */
public class RandomIdentityAnswer implements PartialFunction<Integer, Integer> {
    
    private final Random random = new Random();
    private final Range<Integer> range;

    RandomIdentityAnswer(Range<Integer> range) {
        this.range = range;
    }

    @Override
    public Integer apply(Integer o) {
        return isDefinedAt(o) ? o : random.nextInt();
    }

    @Override
    public boolean isDefinedAt(Integer value) {
        return range.contains(value);
    }
}
