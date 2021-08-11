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
    final LocalDateTime NOW = LocalDateTime.now()

    @Unroll
    def "all #flights departing before this moment are #expected"() {
        expect:
        FlightsReporter.getAllFlightsDepartingBefore(new Flights(flights), NOW).flights == expected

        where:
        flights                                       | expected
        []                                            | []
        [after1, after2]                              | []
        [after1, before1, after2]                     | [before1]
        [fromBeforeToAfter1, after1, after2, before1] | [fromBeforeToAfter1, before1]
    }

    @Unroll
    def "all #flights departing after this moment are #expected"() {
        expect:
        FlightsReporter.getAllFlightsDepartingAfter(new Flights(flights), NOW).flights == expected

        where:
        flights                                       | expected
        []                                            | []
        [fromBeforeToAfter1, before1, after1]         | [after1]
        [after1, fromBeforeToAfter1, before1, after2] | [after1, after2]
    }

    @Unroll
    def "all #flights arriving before this moment are #expected"() {
        expect:
        FlightsReporter.getAllFlightsArrivingBefore(new Flights(flights), NOW).flights == expected

        where:
        flights                                       | expected
        []                                            | []
        [after1, after2]                              | []
        [after1, after2, fromBeforeToAfter1]          | []
        [after1, fromBeforeToAfter1, after2, before1] | [before1]
    }

    @Unroll
    def "all #flights arriving after this moment are #expected"() {
        expect:
        FlightsReporter.getAllFlightsArrivingAfter(new Flights(flights), NOW).flights == expected

        where:
        flights                                       | expected
        []                                            | []
        [before1]                                     | []
        [before1, after1, after2, fromBeforeToAfter1] | [after1, after2, fromBeforeToAfter1]
    }
}






