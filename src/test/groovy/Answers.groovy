import com.google.common.collect.Range
import io.vavr.control.Option
import spock.lang.Specification

import java.util.regex.Pattern 
/**
 * Created by mtumilowicz on 2019-03-04.
 */
class Answers extends Specification {
    def "define partial function on 0..3: x -> x + 1, otherwise -1"() {
        given:
        def increment = new IncrementAnswer(0..3)

        expect:
        increment.apply(-1) == -1
        increment.apply(0) == 1
        increment.apply(1) == 2
        increment.apply(2) == 3
        increment.apply(3) == 4
        increment.apply(4) == -1
    }
    
    def "define partial function that checks if string matches regex only letters, otherwise ValidationException - success"() {
        given:
        def pattern = Pattern.compile("^[a-z]*\$").asMatchPredicate()
        def validation = new ValidatorAnswer(pattern)
        
        expect:
        validation.apply("a")
        validation.apply("abc")
        validation.apply("z")
        validation.apply("qwerty")
    }

    def "define partial function that checks if string matches regex only letters, otherwise ValidationException - exception"() {
        given:
        def pattern = Pattern.compile("^[a-z]*\$").asMatchPredicate()
        def validation = new ValidatorAnswer(pattern)

        when:
        validation.apply("1")
        
        then:
        thrown(ValidationException)
    }

    def "define partial function: identity on 0..3, otherwise random"() {
        given:
        def randomIdentity = new RandomIdentityAnswer(Range.closed(0, 3))
        
        expect:
        randomIdentity.apply(0) == 0
        randomIdentity.apply(1) == 1
        randomIdentity.apply(2) == 2
        randomIdentity.apply(3) == 3
    }
    
    def "lifter - lifting partial function - Increment" () {
        when:
        def lifted = LifterAnswer.lift(new IncrementAnswer())
        
        then:
        lifted.apply(-1) == Option.none()
        lifted.apply(0) == Option.some(1)
        lifted.apply(1) == Option.some(2)
        lifted.apply(2) == Option.some(3)
        lifted.apply(3) == Option.some(4)
        lifted.apply(4) == Option.none()
    }
}