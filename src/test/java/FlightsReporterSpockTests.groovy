import spock.lang.Specification
import spock.lang.*

import java.time.LocalDateTime

class FlightsReporterSpockTests extends Specification {
    @Shared
    def flightBefore1 = FlightGenerator.generateFlight(
            ["departure": LocalDateTime.of(2021, 8, 8, 10, 30),
             "arrival"  : LocalDateTime.of(2021, 8, 8, 13, 10)]
    )

    @Shared
    def flightAfter1 = FlightGenerator.generateFlight(
            ["departure": LocalDateTime.of(2021, 8, 20, 10, 0),
             "arrival"  : LocalDateTime.of(2021, 8, 20, 15, 10)
            ]
    )

    @Shared
    def flightAfter2 = FlightGenerator.generateFlight(
            ["departure": LocalDateTime.of(2021, 8, 15, 15, 0),
             "arrival"  : LocalDateTime.of(2021, 8, 16, 8, 0)]
    )

    def "get all flights departing before"(Flights flights, LocalDateTime date) {
        expect:
        FlightsReporter.getAllFlightsDepartingBefore(flights, date).flights == expectedFlights

        where:
        flights << [
                new Flights([]),
                new Flights([flightAfter1]),
                new Flights([flightAfter1, flightBefore1, flightAfter2])
        ]
        date << [
                LocalDateTime.of(2021, 8, 8, 21, 0),
                LocalDateTime.of(2021, 8, 8, 21, 0),
                LocalDateTime.of(2021, 8, 8, 21, 0)
        ]
        expectedFlights << [[], [], [flightBefore1]]
    }
}
