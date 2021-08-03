import org.junit.Before
import org.junit.Test
import org.junit.Assert
import org.junit.jupiter.api.BeforeAll

import java.nio.channels.FileLock
import java.time.LocalDateTime
import java.time.LocalTime;
//import FlightsReporter

import static  org.junit.jupiter.api.Assertions.*;

class FlightsReporterTests {
    List<Flight> flights = []

    @Test
    void testFlightLengthInMinutes() {
        Flight fl1 = new Flight(id: "SOFLON11", from: FlightsReporter.Airports.SOFIA, destination: FlightsReporter.Airports.LONDON,
            departure: LocalDateTime.of(2021, 8, 3, 10, 30),
                arrival: LocalDateTime.of(2021, 8, 3, 13, 15),
                passengers: 100, capacity: FlightsReporter.CAPACITIES[3], runningLate: false)

        assertEquals(FlightsReporter.flightLengthInMinutes(fl1), 165)

    }

    @Test
    void testGetAllFlightsTo() {
        FlightsReporter.printFlights(flights)
        assert FlightsReporter.getAllFlightsTo(flights, FlightsReporter.Airports.AMSTERDAM)
                == [flights[0], flights[1]]

        assertEquals(FlightsReporter.getAllFlightsTo(flights, FlightsReporter.Airports.SOFIA),
                Arrays.asList(flights[8], flights[9]))

        assertEquals(FlightsReporter.getAllFlightsTo(flights, FlightsReporter.Airports.LONDON),
                Arrays.asList(flights[2], flights[4], flights[7]))

        assertNotEquals(FlightsReporter.getAllFlightsTo(flights, FlightsReporter.Airports.NEW_YORK),
                Arrays.asList(flights[5], flights[6], flights[7]))
    }

    @Test
    void testGetAllFlightsFrom() {
        assert FlightsReporter.getAllFlightsFrom(flights, FlightsReporter.Airports.SOFIA) == [flights[1], flights[2], flights[3]]

        assertEquals(FlightsReporter.getAllFlightsFrom(flights, FlightsReporter.Airports.NEW_YORK), [])

        assertNotEquals(FlightsReporter.getAllFlightsFrom(flights, FlightsReporter.Airports.BERLIN), [])
        assertEquals(FlightsReporter.getAllFlightsFrom(flights, FlightsReporter.Airports.PARIS),
                [flights[0], flights[5], flights[8]])
    }

    @Test
    void testPassengerAmount() {
        def fl1 = FlightsReporter.getAllFlightsFrom(flights, FlightsReporter.Airports.NEW_YORK)
        assert FlightsReporter.passengerAmount(fl1) == 0

        assert FlightsReporter.passengerAmount(flights) == 1430
        assertEquals(FlightsReporter.passengerAmountTo(flights, FlightsReporter.Airports.NEW_YORK), 360)
        assertEquals(FlightsReporter.passengerAmountTo(flights, FlightsReporter.Airports.SOFIA), 230)
        assertEquals(FlightsReporter.passengerAmountFrom(flights, FlightsReporter.Airports.SOFIA), 460)
        assertEquals(FlightsReporter.passengerAmountFrom(flights, FlightsReporter.Airports.NEW_YORK), 0)
    }

    @Test
    void testSortingByDepartureDate() {
        def departureExpected = []
        def fl1 = flights.collect()
        departureExpected << flights[9] << flights[5] << flights[1] << flights[0] << flights[4] << flights[6]
                << flights[2] << flights[3] << flights[8] << flights[7]
        FlightsReporter.sortByDepartureDate(fl1)
        assertEquals(fl1, departureExpected)
    }

    @Test
    void testSortingByArrivalDate() {
        def arrivalExpected = []
        def fl2 = flights.collect()
        arrivalExpected << flights[9] << flights[1] << flights[0] << flights[4] << flights[5] << flights[6]
                << flights[2] << flights[3] << flights[8] << flights[7]
        FlightsReporter.sortByArrivalDate(fl2)
        assertEquals(fl2, arrivalExpected)
    }

    @Test
    void testSortingByFlightLen() {
        def flightLenExpected = []
        def fl1 = flights.collect()
        flightLenExpected << flights[0] << flights[7] << flights[4] << flights[1] << flights[2] << flights[9]
                << flights[3] << flights[6] << flights[8] << flights[5]
        FlightsReporter.sortByFlightLength(fl1)
        assertEquals(fl1, flightLenExpected)
    }

    @Test
    void testGetAllPlanesAboveCapacity() {
        def expected = []
        expected << flights[0] << flights[5] << flights[6]
        assertEquals(FlightsReporter.getAllPlanesAboveCertainCapacityFull(flights, 0.99), expected)
    }

    @Test
    void testRunningLate() {
        assertEquals(true, FlightsReporter.isThereAFlightRunningLate(flights))

        def flightsLate = []
        flightsLate << flights[0] << flights[3] << flights[4] << flights[5] << flights[8] << flights[9]
        assertEquals(flightsLate, FlightsReporter.getFlightsRunningLate(flights))
    }

    @Test
    void testDepartingAfter() {
        assertEquals(
                FlightsReporter.getAllFlightsDepartingBefore(flights,
                        LocalDateTime.of(2021, 7, 31, 10, 0)), [])

        assertEquals(FlightsReporter.getAllFlightsDepartingBefore(flights,
                        LocalDateTime.of(2021, 8, 2, 12, 0)),
                        [flights[0], flights[1], flights[2], flights[4], flights[5], flights[6], flights[9]])
    }



    @Before
    void initializeTestData() {
        flights << new Flight(id: "1",
                from: FlightsReporter.Airports.PARIS,
                destination: FlightsReporter.Airports.AMSTERDAM,
                departure: LocalDateTime.of(2021, 8, 1, 10, 0),
                arrival: LocalDateTime.of(2021, 8, 1, 11, 10),
                passengers: 150, capacity: 150, runningLate: true)

                << new Flight(id: "2",
                from: FlightsReporter.Airports.SOFIA,
                destination: FlightsReporter.Airports.AMSTERDAM,
                departure: LocalDateTime.of(2021, 8, 1, 8, 35),
                arrival: LocalDateTime.of(2021, 8, 1, 11, 05),
                passengers: 160, capacity: 180, runningLate: false)

                << new Flight(id: "3",
                from: FlightsReporter.Airports.SOFIA,
                destination: FlightsReporter.Airports.LONDON,
                departure: LocalDateTime.of(2021, 8, 2, 11, 10),
                arrival: LocalDateTime.of(2021, 8, 2, 14, 10),
                passengers: 170, capacity: 180, runningLate: false)

                << new Flight(id: "4",
                from: FlightsReporter.Airports.SOFIA,
                destination: FlightsReporter.Airports.BERLIN,
                departure: LocalDateTime.of(2021, 8, 2, 15, 30),
                arrival: LocalDateTime.of(2021, 8, 2, 21, 10),
                passengers: 130, capacity: 150, runningLate: true)

                << new Flight(id: "5",
                from: FlightsReporter.Airports.AMSTERDAM,
                destination: FlightsReporter.Airports.LONDON,
                departure: LocalDateTime.of(2021, 8, 1, 14, 45),
                arrival: LocalDateTime.of(2021, 8, 1, 16, 52),
                passengers: 90, capacity: 120, runningLate: true)

                << new Flight(id: "6",
                from: FlightsReporter.Airports.PARIS,
                destination: FlightsReporter.Airports.NEW_YORK,
                departure: LocalDateTime.of(2021, 8, 1, 7, 5),
                arrival: LocalDateTime.of(2021, 8, 2, 1, 10),
                passengers: 180, capacity: 180, runningLate: true)

                << new Flight(id: "7",
                from: FlightsReporter.Airports.LONDON,
                destination: FlightsReporter.Airports.NEW_YORK,
                departure: LocalDateTime.of(2021, 8, 1, 19, 35),
                arrival: LocalDateTime.of(2021, 8, 2, 3, 10),
                passengers: 180, capacity: 180, runningLate: false)

                << new Flight(id: "8",
                from: FlightsReporter.Airports.BERLIN,
                destination: FlightsReporter.Airports.LONDON,
                departure: LocalDateTime.of(2021, 8, 3, 20, 0),
                arrival: LocalDateTime.of(2021, 8, 3, 21, 20),
                passengers: 140, capacity: 150, runningLate: false)

                << new Flight(id: "9",
                from: FlightsReporter.Airports.PARIS,
                destination: FlightsReporter.Airports.SOFIA,
                departure: LocalDateTime.of(2021, 8, 2, 18, 0),
                arrival: LocalDateTime.of(2021, 8, 3, 2, 10),
                passengers: 90, capacity: 150, runningLate: true)

                << new Flight(id: "10",
                from: FlightsReporter.Airports.BERLIN,
                destination: FlightsReporter.Airports.SOFIA,
                departure: LocalDateTime.of(2021, 8, 1, 6, 5),
                arrival: LocalDateTime.of(2021, 8, 1, 10, 10),
                passengers: 140, capacity: 150, runningLate: true)
    }
}
