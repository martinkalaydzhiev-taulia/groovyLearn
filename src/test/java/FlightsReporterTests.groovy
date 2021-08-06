import jdk.jfr.FlightRecorder
import org.junit.Before
import org.junit.Test

import java.time.LocalDateTime
import static org.junit.jupiter.api.Assertions.*
import spock.lang.*

class FlightsReporterTests {

    @Test
    void testFlightLengthInMinutes() {
        Flight longFlight = FlightGenerator.generateFlight(
                ["departure": LocalDateTime.now().withSecond(0).withNano(0),
                 "arrival"  : LocalDateTime.now().withSecond(0).withNano(0).
                         plusDays(1).plusMinutes(10)]
        )

        assertEquals(longFlight.flightLengthInMinutes(), 24 * 60 + 10)
    }

    @Test
    void getAllFlightsTo() {
        Flights emptyListOfFlights = new Flights([])
        assertEquals(FlightsReporter.getAllFlightsTo(emptyListOfFlights, Airports.AMSTERDAM).flights, [])

        List<Flight> flightsToAmsterdam = []
        1.upto(3) { flightsToAmsterdam << FlightGenerator.generateFlight("destination": Airports.AMSTERDAM) }
        assertEquals(FlightsReporter.getAllFlightsTo(new Flights(flightsToAmsterdam), Airports.AMSTERDAM).flights, flightsToAmsterdam)

        def flightsToWorld = flightsToAmsterdam.collect()
        1.upto(2) { flightsToWorld << FlightGenerator.generateFlight("destination": Airports.NEW_YORK) }
        assertEquals(FlightsReporter.getAllFlightsTo(new Flights(flightsToWorld), Airports.AMSTERDAM).flights, flightsToAmsterdam)
    }

    @Test
    void testGetAllFlightsFrom() {
        Flights emptyListOfFlights = new Flights([])
        assertEquals(FlightsReporter.getAllFlightsFrom(emptyListOfFlights, Airports.NEW_YORK).flights, [])

        List<Flight> flightsFromNY = []
        1.upto(2) { flightsFromNY << FlightGenerator.generateFlight(["from": Airports.NEW_YORK]) }

        Flights flightsNY = new Flights(flightsFromNY)
        assertEquals(FlightsReporter.getAllFlightsFrom(flightsNY, Airports.NEW_YORK).flights.size(), 2)
        assertEquals(FlightsReporter.getAllFlightsFrom(flightsNY, Airports.NEW_YORK).flights, flightsFromNY)

        List<Flight> flightsFromWorld = flightsFromNY.collect()
        1.upto(2) { flightsFromWorld << FlightGenerator.generateFlight(["from": Airports.LONDON]) }
        assertEquals(FlightsReporter.getAllFlightsFrom(new Flights(flightsFromWorld), Airports.NEW_YORK).flights, flightsFromNY)
        flightsFromWorld.shuffle()
        assertEquals(FlightsReporter.getAllFlightsFrom(new Flights(flightsFromWorld), Airports.NEW_YORK).flights.size(), 2)
    }

    @Test
    void testPassengerCount() {
        assertEquals(FlightsReporter.getPassengerCount(new Flights([])), 0)
        def listOfFlights = [FlightGenerator.generateFlight(["destination": Airports.BERLIN, "passengers": 40]),
                             FlightGenerator.generateFlight(["destination": Airports.PARIS, "passengers": 55])]
        assertEquals(FlightsReporter.getPassengerCount(new Flights(listOfFlights)), 95)
        assertEquals(FlightsReporter.getPassengerCountTo(new Flights(listOfFlights), Airports.BERLIN), 40)

        listOfFlights << FlightGenerator.generateFlight("destination": Airports.PARIS, "passengers": 60)
        assertEquals(FlightsReporter.getPassengerCountTo(new Flights(listOfFlights), Airports.PARIS), 115)
    }

    @Test
    void testSortingByDepartureDate() {
        def first = FlightGenerator
                .generateFlight(["departure": LocalDateTime.of(2021, 8, 1, 10, 30),
                                 "arrival"  : LocalDateTime.of(2021, 8, 1, 17, 25)])

        def second = FlightGenerator
                .generateFlight(["departure": LocalDateTime.of(2021, 8, 1, 12, 15),
                                 "arrival"  : LocalDateTime.of(2021, 8, 1, 15, 35)])

        def third = FlightGenerator
                .generateFlight(["departure": LocalDateTime.of(2021, 8, 1, 15, 20),
                                 "arrival"  : LocalDateTime.of(2021, 8, 1, 19, 5)])

        def listOfFlights = [first, second, third]
        listOfFlights.shuffle()
        FlightsReporter.sortByDepartureDate(listOfFlights)
        assertEquals(listOfFlights, [first, second, third])
    }

    @Test
    void testSortingByArrivalDate() {
        def second = FlightGenerator
                .generateFlight(["departure": LocalDateTime.of(2021, 8, 1, 10, 30),
                                 "arrival"  : LocalDateTime.of(2021, 8, 1, 17, 25)])

        def first = FlightGenerator
                .generateFlight(["departure": LocalDateTime.of(2021, 8, 1, 12, 15),
                                 "arrival"  : LocalDateTime.of(2021, 8, 1, 15, 35)])

        def third = FlightGenerator
                .generateFlight(["departure": LocalDateTime.of(2021, 8, 1, 15, 20),
                                 "arrival"  : LocalDateTime.of(2021, 8, 1, 19, 5)])
        def listOfFlights = [first, second, third]
        listOfFlights.shuffle()
        FlightsReporter.sortByArrivalDate(listOfFlights)
        assertEquals(listOfFlights, [first, second, third])
    }

    @Test
    void testSortingByFlightLen() {
        def shortest = FlightGenerator
                .generateFlight(["departure": LocalDateTime.of(2021, 8, 1, 9, 20),
                                 "arrival"  : LocalDateTime.of(2021, 8, 1, 12, 20)])
        def mid = FlightGenerator
                .generateFlight(["departure": LocalDateTime.of(2021, 8, 2, 15, 30),
                                 "arrival"  : LocalDateTime.of(2021, 8, 2, 20, 40)])
        def longest = FlightGenerator
                .generateFlight(["departure": LocalDateTime.of(2021, 8, 2, 11, 5),
                                 "arrival"  : LocalDateTime.of(2021, 8, 3, 3, 10)])

        def listOfFlights = [shortest, mid, longest]
        listOfFlights.shuffle()
        FlightsReporter.sortByFlightLength(listOfFlights)
        assertEquals(listOfFlights, [shortest, mid, longest])
    }

    @Test
    void testGetAllPlanesAboveCapacity() {
    }

    @Test
    void testRunningLate() {
        def late1 = FlightGenerator.generateFlight("runningLate": true)
        def onTime1 = FlightGenerator.generateFlight("runningLate": false)

        assertEquals(FlightsReporter.getFlightsRunningLate(new Flights([onTime1])).flights, [])
        assertEquals(FlightsReporter.getFlightsRunningLate(new Flights([late1, onTime1])).flights, [late1])
    }

    @Test
    void testDepartingAfter() {
        def first = FlightGenerator
                .generateFlight(["departure": LocalDateTime.of(2021, 8, 1, 10, 30),
                                 "arrival"  : LocalDateTime.of(2021, 8, 1, 17, 25)])

        def second = FlightGenerator
                .generateFlight(["departure": LocalDateTime.of(2021, 8, 1, 12, 15),
                                 "arrival"  : LocalDateTime.of(2021, 8, 1, 15, 35)])

        def third = FlightGenerator
                .generateFlight(["departure": LocalDateTime.of(2021, 8, 1, 15, 20),
                                 "arrival"  : LocalDateTime.of(2021, 8, 1, 19, 5)])
        assertEquals(FlightsReporter.getAllFlightsDepartingAfter(
                new Flights([]), LocalDateTime.of(2021, 8, 1, 10, 5)).flights, [])
        assertEquals(FlightsReporter
                .getAllFlightsDepartingAfter(new Flights([first, second, third]),
                        LocalDateTime.of(2021, 8, 1, 12, 0)).flights, [second, third])

    }
}
