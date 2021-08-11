import org.testng.IExpectedExceptionsHolder
import spock.lang.*

import java.time.LocalDateTime

class FlightsReporterArrivalDepartureTest extends Specification {
    @Shared
    def after1 = FlightGenerator.generateFlight(
            ["departure": LocalDateTime.now().plusMinutes(10),
             "arrival"  : LocalDateTime.now().plusMinutes(310)])
    @Shared
    def after2 = FlightGenerator.generateFlight(
            ["departure": LocalDateTime.now().plusMinutes(90),
             "arrival"  : LocalDateTime.now().plusMinutes(270)])
    @Shared
    def fromBeforeToAfter1 = FlightGenerator.generateFlight(
            ["departure": LocalDateTime.now().minusMinutes(100),
             "arrival"  : LocalDateTime.now().plusMinutes(100)])
    @Shared
    def before1 = FlightGenerator.generateFlight(
            ["departure": LocalDateTime.now().minusMinutes(500),
             "arrival"  : LocalDateTime.now().minusMinutes(70)])
    @Shared
    LocalDateTime now = LocalDateTime.now()

    @Unroll
    def "get all #flights departing before #date"() {
        expect:
        FlightsReporter.getAllFlightsDepartingBefore(flights, date).flights == expected

        where:
        flights                                                    | date | expected
        new Flights([])                                            | now  | []
        new Flights([after1, after2])                              | now  | []
        new Flights([after1, before1, after2])                     | now  | [before1]
        new Flights([fromBeforeToAfter1, after1, after2, before1]) | now  | [fromBeforeToAfter1, before1]
    }

    @Unroll
    def "get all #flights departing after #date"() {
        expect:
        FlightsReporter.getAllFlightsDepartingAfter(flights, date).flights == expected

        where:
        flights                                                    | date | expected
        new Flights([])                                            | now  | []
        new Flights([fromBeforeToAfter1, before1, after1])         | now  | [after1]
        new Flights([after1, fromBeforeToAfter1, before1, after2]) | now  | [after1, after2]
    }

    @Unroll
    def "get all #flights arriving before #date"() {
        expect:
        FlightsReporter.getAllFlightsArrivingBefore(flights, date).flights == expected

        where:
        flights                                                    | date | expected
        new Flights([])                                            | now  | []
        new Flights([after1, after2])                              | now  | []
        new Flights([after1, after2, fromBeforeToAfter1])          | now  | []
        new Flights([after1, fromBeforeToAfter1, after2, before1]) | now  | [before1]
    }

    @Unroll
    def "get all #flights arriving after #date"() {
        expect:
        FlightsReporter.getAllFlightsArrivingAfter(flights, date).flights == expected

        where:
        flights                                                    | date | expected
        new Flights([])                                            | now  | []
        new Flights([before1])                                     | now  | []
        new Flights([before1, after1, after2, fromBeforeToAfter1]) | now  | [after1, after2, fromBeforeToAfter1]
    }
}

class FlightsReporterFlightLengthTest extends Specification {
    @Shared
    Flight shortFlight = FlightGenerator.generateFlight(
            ["departure": LocalDateTime.now(), "arrival": LocalDateTime.now().plusMinutes(60)]
    )
    @Shared
    Flight shortFlight2 = FlightGenerator.generateFlight(
            ["departure": LocalDateTime.now(), "arrival": LocalDateTime.now().plusMinutes(60)]
    )
    @Shared
    Flight mediumLenFlight = FlightGenerator.generateFlight(
            ["departure": LocalDateTime.now(), "arrival": LocalDateTime.now().plusMinutes(120)]
    )
    @Shared
    Flight longFlight = FlightGenerator.generateFlight(
            ["departure": LocalDateTime.now(), "arrival": LocalDateTime.now().plusMinutes(320)]
    )

    @Unroll
    def "get all #flights shorter than #duration"() {
        expect:
        FlightsReporter.getAllFlightsShorterThan(flights, duration).flights == expected

        where:
        flights                                                               | duration | expected
        new Flights([])                                                       | 30       | []
        new Flights([shortFlight])                                            | 30       | []
        new Flights([shortFlight, mediumLenFlight, longFlight, shortFlight2]) | 70       | [shortFlight, shortFlight2]
        new Flights([longFlight, mediumLenFlight, shortFlight])               | 200      | [mediumLenFlight, shortFlight]
        new Flights([shortFlight, mediumLenFlight, longFlight])               | 1000     | [shortFlight, mediumLenFlight, longFlight]
    }

    @Unroll
    def "get all #flights longer than #duration"() {
        expect:
        FlightsReporter.getAllFlightsLongerThan(flights, duration).flights == expected

        where:
        flights                                                               | duration | expected
        new Flights([])                                                       | 30       | []
        new Flights([shortFlight, mediumLenFlight, longFlight])               | 30       | [shortFlight, mediumLenFlight, longFlight]
        new Flights([shortFlight, mediumLenFlight, longFlight])               | 80       | [mediumLenFlight, longFlight]
        new Flights([shortFlight, shortFlight2, mediumLenFlight, longFlight]) | 300      | [longFlight]
    }

    @Unroll
    def "get all #flights with #duration"() {
        expect:
        FlightsReporter.getAllFlightWithDuration(flights, duration).flights == expected

        where:
        flights                                                               | duration | expected
        new Flights([])                                                       | 0        | []
        new Flights([shortFlight, shortFlight2])                              | 0        | []
        new Flights([shortFlight, shortFlight2])                              | 60       | [shortFlight, shortFlight2]
        new Flights([shortFlight, shortFlight2, mediumLenFlight])             | 60       | [shortFlight, shortFlight2]
        new Flights([shortFlight, shortFlight2, mediumLenFlight, longFlight]) | 120      | [mediumLenFlight]
    }
}

class FlightsReporterPassengerTest extends Specification {
    @Shared
    Flight empty = FlightGenerator.generateFlight("passengers": 0)
    @Shared
    Flight small = FlightGenerator.generateFlight(["passengers": 50])
    @Shared
    Flight medium = FlightGenerator.generateFlight(["passengers": 150])
    @Shared
    Flight big = FlightGenerator.generateFlight(["passengers": 250])

    def "get flight with most passengers"() {
        expect:
        FlightsReporter.getFlightWithMostPassengers(flights) == expected

        where:
        flights                                  | expected
        new Flights([])                          | null
        new Flights([empty])                     | empty
        new Flights([empty, small, medium, big]) | big
    }

    def "get flight with least passengers"() {
        expect:
        FlightsReporter.getFlightWithLeastPassengers(flights) == expected

        where:
        flights                                  | expected
        new Flights([])                          | null
        new Flights([medium, big])               | medium
        new Flights([empty, small, medium, big]) | empty
    }

    def "get #flights with passengers above #count"() {
        expect:
        FlightsReporter.getAllFlightsWithPassengersAbove(flights, count).flights == expected

        where:
        flights                                  | count | expected
        new Flights([])                          | 0     | []
        new Flights([empty])                     | -1    | [empty]
        new Flights([empty])                     | 0     | [empty]
        new Flights([empty])                     | 1     | []
        new Flights([empty, small, medium, big]) | 100   | [medium, big]
    }

    def "get #flights with passengers below #count"() {
        expect:
        FlightsReporter.getAllFlightsWithPassengersBelow(flights, count).flights == expected

        where:
        flights                                  | count | expected
        new Flights([])                          | 0     | []
        new Flights([empty])                     | -1    | []
        new Flights([empty])                     | 0     | []
        new Flights([empty])                     | 1     | [empty]
        new Flights([empty, small, medium, big]) | 200   | [empty, small, medium]
        new Flights([empty, small, medium, big]) | 300   | [empty, small, medium, big]
    }
}


