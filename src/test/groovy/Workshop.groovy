import com.google.common.collect.Range
import io.vavr.control.Option
import io.vavr.control.Try
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.function.BinaryOperator
import java.util.regex.Pattern
import java.util.stream.Stream 
/**
 * Created by mtumilowicz on 2019-03-04.
 */
class Workshop extends Specification {
    def "define partial function on [0,...,3] in a manner: x -> x + 1 if x e [0,...,3], otherwise -1"() {
        given:
        def increment = new Increment() // implement PartialFunction
        
        expect:
        increment.apply(-1) == -1
        increment.apply(0) == 1
        increment.apply(1) == 2
        increment.apply(2) == 3
        increment.apply(3) == 4
        increment.apply(4) == -1
    }

    def "success case: define partial function that checks if string matches regex only letters, otherwise ValidationException"() {
        given:
        def pattern = Pattern.compile("^[a-z]*\$").asMatchPredicate()
        def validation = new Validator() // implement PartialFunction

        expect:
        validation.apply("a")
        validation.apply("abc")
        validation.apply("z")
        validation.apply("qwerty")
    }

    def "exception case: define partial function that checks if string matches regex only letters, otherwise ValidationException"() {
        given:
        def pattern = Pattern.compile("^[a-z]*\$").asMatchPredicate()
        def validation = new Validator() // implement PartialFunction

        when:
        validation.apply("1")

        then:
        thrown(ValidationException)
    }

    def "define partial function: identity on 0..3, otherwise random"() {
        given:
        def randomIdentity = new RandomIdentity() // implement PartialFunction using Range guava

        expect:
        randomIdentity.apply(0) == 0
        randomIdentity.apply(1) == 1
        randomIdentity.apply(2) == 2
        randomIdentity.apply(3) == 3
    }

    def "lifter - lifting partial function - Increment" () {
        when:
        def lifted = Lifter.lift(new Increment())

        then:
        lifted.apply(-1) == Option.none()
        lifted.apply(0) == Option.some(1)
        lifted.apply(1) == Option.some(2)
        lifted.apply(2) == Option.some(3)
        lifted.apply(3) == Option.some(4)
        lifted.apply(4) == Option.none()
    }

    def "lifter - lifting partial function - Validator" () {
        when:
        def lifted = Lifter.lift(new Validator(Pattern.compile("^[a-z]*\$").asMatchPredicate()))

        then:
        lifted.apply("a") == Option.some(true)
        lifted.apply("abc") == Option.some(true)
        lifted.apply("z") == Option.some(true)
        lifted.apply("qwerty") == Option.some(true)
        lifted.apply("1") == Option.none()
        lifted.apply("d2d") == Option.none()
        lifted.apply("%") == Option.none()
    }

    def "lifter - lifting partial function - RandomIdentity"() {
        when:
        def lifted = Lifter.lift(new RandomIdentity(Range.closed(0, 3)))

        then:
        lifted.apply(-1) == Option.none()
        lifted.apply(0) == Option.some(0)
        lifted.apply(1) == Option.some(1)
        lifted.apply(2) == Option.some(2)
        lifted.apply(3) == Option.some(3)
        lifted.apply(4) == Option.none()
    }

    def "vavr lifting function with Option: div"() {
        given:
        BinaryOperator<Integer> div = { a, b -> a.intdiv(b)}

        when:
        def lifted = div // vavr lift

        then:
        lifted.apply(1, 0) == Option.none()
        lifted.apply(null, 2) == Option.none()
        lifted.apply(2, 0) == Option.none()
        lifted.apply(4, 2) == Option.some(2)
    }

    def "vavr lifting function with Try: Repository.findById"() {
        given:
        def repo = new RepositoryACL()

        expect:
        repo.findById(1) == Try.success(new User(1))
        repo.findById(2).failure
        repo.findById(2).cause.class == UserCannotBeFoundException
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
        Stream.of(cannotBeActive, canBeActive) // process here

        then:
        activeUserRepository.count() == 1
        activeUserRepository.existsAll(List.of(2))
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
        Stream.of(cannotBeActive, canBeActive) // process here
        then:
        activeUserRepository.count() == 1
        activeUserRepository.existsAll([2])
        fails == ["id = 1: warns has to be <= 10"]
    }
}