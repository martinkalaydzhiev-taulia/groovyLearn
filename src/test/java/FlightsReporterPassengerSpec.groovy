import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class FlightsReporterPassengerTest extends Specification {
    @Shared
    Flight empty = FlightGenerator.generateFlight("passengers": 0)
    @Shared
    Flight small = FlightGenerator.generateFlight(["passengers": 50])
    @Shared
    Flight medium = FlightGenerator.generateFlight(["passengers": 150])
    @Shared
    Flight big = FlightGenerator.generateFlight(["passengers": 250])

    @Unroll
    def "first flight with most passengers is #expected"() {
        expect:
        FlightsReporter.getFlightWithMostPassengers(new Flights(flights)) == expected


        where:
        flights                     | expected
        []                          | null
        [empty]                     | empty
        [empty, small, medium, big] | big
    }

    @Unroll
    def "first flight with least passengers is #expected"() {
        expect:
        FlightsReporter.getFlightWithLeastPassengers(new Flights(flights)) == expected

        where:
        flights                     | expected
        []                          | null
        [medium, big]               | medium
        [empty, small, medium, big] | empty
    }

    @Unroll
    def "#flights with passengers above #count are #expected"() {
        expect:
        FlightsReporter.getAllFlightsWithPassengersAbove(new Flights(flights), count).flights == expected

        where:
        flights                     | count | expected
        []                          | 0     | []
        [empty]                     | -1    | [empty]
        [empty]                     | 0     | [empty]
        [empty]                     | 1     | []
        [empty, small, medium, big] | 100   | [medium, big]
    }

    @Unroll
    def "get #flights with passengers below #count"() {
        expect:
        FlightsReporter.getAllFlightsWithPassengersBelow(new Flights(flights), count).flights == expected

        where:
        flights                     | count | expected
        []                          | 0     | []
        [empty]                     | -1    | []
        [empty]                     | 0     | []
        [empty]                     | 1     | [empty]
        [empty, small, medium, big] | 200   | [empty, small, medium]
        [empty, small, medium, big] | 300   | [empty, small, medium, big]
    }
}