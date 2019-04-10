import com.google.common.collect.Range
import io.vavr.Function1
import io.vavr.Function2
import io.vavr.PartialFunction
import io.vavr.control.Option
import io.vavr.control.Try
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.stream.Stream 
/**
 * Created by mtumilowicz on 2019-03-04.
 */
class Workshop extends Specification {
    
    def "define partial function on [0,...,3] in a manner: x -> x + 1 if x e [0,...,3], otherwise -1"() {
        given:
        PartialFunction<Integer, Integer> increment = new Increment() // implement PartialFunction
        
        expect:
        increment.apply(-1) == -1
        increment.apply(0) == 1
        increment.apply(1) == 2
        increment.apply(2) == 3
        increment.apply(3) == 4
        increment.apply(4) == -1
    }

    def "define partial function: identity on [0,...,3], otherwise random"() {
        given:
        PartialFunction<Integer, Integer> randomIdentity = new RandomIdentity() // implement PartialFunction

        expect:
        randomIdentity.apply(0) == 0
        randomIdentity.apply(1) == 1
        randomIdentity.apply(2) == 2
        randomIdentity.apply(3) == 3
    }

    def "lifter - lifting partial function - Increment" () {
        when:
        Function<Integer, Option<Integer>> lifted = Lifter.lift(new Increment()) // implement Lifter.lift

        then:
        lifted.apply(-1) == Option.none()
        lifted.apply(0) == Option.some(1)
        lifted.apply(1) == Option.some(2)
        lifted.apply(2) == Option.some(3)
        lifted.apply(3) == Option.some(4)
        lifted.apply(4) == Option.none()
    }

    def "lifter - lifting partial function - RandomIdentity"() {
        when:
        Function<Integer, Option<Integer>> lifted = Lifter.lift(new RandomIdentity(Range.closed(0, 3)))

        then:
        lifted.apply(-1) == Option.none()
        lifted.apply(0) == Option.some(0)
        lifted.apply(1) == Option.some(1)
        lifted.apply(2) == Option.some(2)
        lifted.apply(3) == Option.some(3)
        lifted.apply(4) == Option.none()
    }

    def "vavr - lifting function with Option: div"() {
        given:
        BinaryOperator<Integer> div = { a, b -> a.intdiv(b)}

        when:
        Function2<Integer, Integer, Option<Integer>> lifted = div // FunctionN.lift

        then:
        lifted.apply(1, 0) == Option.none()
        lifted.apply(null, 2) == Option.none()
        lifted.apply(2, 0) == Option.none()
        lifted.apply(4, 2) == Option.some(2)
    }

    def "vavr - lifting function with Try: Repository.findById"() {
        given:
        def repo = new Repository()
        Function1<Integer, Try<User>> findById = repo.findById(it) // lift with Try, hint: Function1.liftTry

        expect:
        findById.apply(1) == Try.success(new User(1))
        findById.apply(2).failure
        findById.apply(2).cause.class == UserCannotBeFoundException
    }

    def "for a given list of users, activate users that can be active and save them - using function lifting with Option"() {
        given:
        ActiveUserRepository activeUserRepository = new ActiveUserRepository()
        and:
        def cannotBeActive = BlockedUser.builder()
                .id(1)
                .banDate(LocalDate.parse("2014-10-12"))
                .warn(15)
                .build()
        and:
        def canBeActive = BlockedUser.builder()
                .id(2)
                .banDate(LocalDate.parse("2016-10-12"))
                .warn(0)
                .build()
        and:
        def now = Clock.fixed(Instant.parse("2016-12-03T10:15:30Z"), ZoneId.systemDefault())

        when:
        Stream.of(cannotBeActive, canBeActive) // process here, hint: use BlockedUser.activate(now), activeUserRepository.add

        then:
        activeUserRepository.count() == 1
        activeUserRepository.containsAll(List.of(2))
    }

    def "for a given list of users, activate users that can be active and save them - using function lifting with Try"() {
        given:
        ActiveUserRepository activeUserRepository = new ActiveUserRepository()
        and:
        def cannotBeActive = BlockedUser.builder()
                .id(1)
                .banDate(LocalDate.parse("2014-10-12"))
                .warn(15)
                .build()
        and:
        def canBeActive = BlockedUser.builder()
                .id(2)
                .banDate(LocalDate.parse("2016-10-12"))
                .warn(0)
                .build()
        and:
        def now = Clock.fixed(Instant.parse("2016-12-03T10:15:30Z"), ZoneId.systemDefault())
        and:
        def fails = []

        when:
        // process here, hint: use BlockedUser.activate(now), activeUserRepository.add
        // Try.onSuccess, onFailure
        Stream.of(cannotBeActive, canBeActive)
        then:
        activeUserRepository.count() == 1
        activeUserRepository.containsAll([2])
        fails == ["id = 1: warns has to be <= 10"]
    }
}