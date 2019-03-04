import spock.lang.Specification 
/**
 * Created by mtumilowicz on 2019-03-04.
 */
class Workshop extends Specification {
    def "define partial function on 0..3: x -> x + 1, otherwise -1"() {
        given:
        def increment = -1 // implement PartialFunction, use Range<Integer> from guava or 1..3 groovy range syntax
        
        expect:
        increment.apply(-1) == -1
        increment.apply(0) == 1
        increment.apply(1) == 2
        increment.apply(2) == 3
        increment.apply(3) == 4
        increment.apply(4) == -1
    }
}