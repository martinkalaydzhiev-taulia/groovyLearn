import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

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
        FlightsReporter.getAllFlightsShorterThan(new Flights(flights), duration).flights == expected

        where:
        flights                                                  | duration | expected
        []                                                       | 30       | []
        [shortFlight]                                            | 30       | []
        [shortFlight, mediumLenFlight, longFlight, shortFlight2] | 70       | [shortFlight, shortFlight2]
        [longFlight, mediumLenFlight, shortFlight]               | 200      | [mediumLenFlight, shortFlight]
        [shortFlight, mediumLenFlight, longFlight]               | 1000     | [shortFlight, mediumLenFlight, longFlight]
    }

    @Unroll
    def "get all #flights longer than #duration"() {
        expect:
        FlightsReporter.getAllFlightsLongerThan(new Flights(flights), duration).flights == expected

        where:
        flights                                                  | duration | expected
        []                                                       | 30       | []
        [shortFlight, mediumLenFlight, longFlight]               | 30       | [shortFlight, mediumLenFlight, longFlight]
        [shortFlight, mediumLenFlight, longFlight]               | 80       | [mediumLenFlight, longFlight]
        [shortFlight, shortFlight2, mediumLenFlight, longFlight] | 300      | [longFlight]
    }

    @Unroll
    def "get all #flights with #duration"() {
        expect:
        FlightsReporter.getAllFlightWithDuration(new Flights(flights), duration).flights == expected

        where:
        flights                                                  | duration | expected
        []                                                       | 0        | []
        [shortFlight, shortFlight2]                              | 0        | []
        [shortFlight, shortFlight2]                              | 60       | [shortFlight, shortFlight2]
        [shortFlight, shortFlight2, mediumLenFlight]             | 60       | [shortFlight, shortFlight2]
        [shortFlight, shortFlight2, mediumLenFlight, longFlight] | 120      | [mediumLenFlight]
    }
}