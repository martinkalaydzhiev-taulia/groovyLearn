import org.junit.Before
import org.junit.Test

import java.time.LocalDateTime


import static org.junit.jupiter.api.Assertions.*;

class FlightsReporterTests {
    FlightsReporter flightsReporter

    @Test
    void testFlightLengthInMinutes() {
        Flight fl1 = new Flight(id: 1, from: Airports.SOFIA, destination: Airports.LONDON,
                departure: LocalDateTime.of(2021, 8, 3, 10, 30),
                arrival: LocalDateTime.of(2021, 8, 3, 13, 15),
                passengers: 100, capacity: FlightGenerator.CAPACITIES[3], runningLate: false)

        assertEquals(fl1.flightLengthInMinutes(), 165)

    }

    @Test
    void getAllFlightsTo() {
        List<Flights> flightsToAmsterdam = []
        FlightsReporter reporter = new FlightsReporter(new Flights(flightsToAmsterdam as List<Flight>))
        assertEquals(reporter.getAllFlightsTo(Airports.AMSTERDAM).flights, [])

        flightsToAmsterdam = [new Flight(id: 1,
                from: Airports.PARIS, destination: Airports.AMSTERDAM,
                departure: LocalDateTime.of(2021, 8, 1, 10, 0),
                arrival: LocalDateTime.of(2021, 8, 1, 11, 10),
                passengers: 150, capacity: 150, runningLate: true),
                              new Flight(id: 2,
                                      from: Airports.SOFIA, destination: Airports.AMSTERDAM,
                                      departure: LocalDateTime.of(2021, 8, 1, 8, 35),
                                      arrival: LocalDateTime.of(2021, 8, 1, 11, 05),
                                      passengers: 160, capacity: 180, runningLate: false)] as List<Flights>
        reporter = new FlightsReporter(new Flights(flightsToAmsterdam as List<Flight>))
        assertEquals(reporter.getAllFlightsTo(Airports.AMSTERDAM).flights, [flightsToAmsterdam[0], flightsToAmsterdam[1]])

        flightsToAmsterdam << new Flight(id: 3,
                from: Airports.BERLIN,
                destination: Airports.LONDON,
                departure: LocalDateTime.of(2021, 8, 3, 20, 0),
                arrival: LocalDateTime.of(2021, 8, 3, 21, 20),
                passengers: 140, capacity: 150, runningLate: false)
        reporter.setFlights(flightsToAmsterdam)

        assertNotEquals(reporter.getAllFlightsTo(Airports.AMSTERDAM).flights, flightsToAmsterdam)
        assertEquals(reporter.getAllFlightsTo(Airports.AMSTERDAM).flights, [flightsToAmsterdam[0], flightsToAmsterdam[1]])
    }

    @Test
    void testGetAllFlightsFrom() {
        FlightsReporter reporter = new FlightsReporter(new Flights([]))
        assertEquals(reporter.getAllFlightsFrom(Airports.NEW_YORK).flights, [])
        def newYork1 = new Flight(id: 8, from: Airports.NEW_YORK, destination: Airports.LONDON,
                departure: LocalDateTime.of(2021, 8, 3, 20, 0),
                arrival: LocalDateTime.of(2021, 8, 3, 21, 20),
                passengers: 140, capacity: 150, runningLate: false)

        def flightsFromNY = [newYork1]
        reporter.setFlights(flightsFromNY)
        assertEquals(reporter.getAllFlightsFrom(Airports.NEW_YORK).flights.size(), 1)
        assertEquals(reporter.getAllFlightsFrom(Airports.NEW_YORK).flights, flightsFromNY)

        def newYork2 = new Flight(id: 6, from: Airports.NEW_YORK, destination: Airports.PARIS,
                departure: LocalDateTime.of(2021, 8, 1, 7, 5),
                arrival: LocalDateTime.of(2021, 8, 2, 1, 10),
                passengers: 180, capacity: 180, runningLate: true)
        def sofia1 = new Flight(id: 10, from: Airports.BERLIN, destination: Airports.SOFIA,
                departure: LocalDateTime.of(2021, 8, 1, 6, 5),
                arrival: LocalDateTime.of(2021, 8, 1, 10, 10),
                passengers: 140, capacity: 150, runningLate: true)
        flightsFromNY << sofia1 << newYork2

        flightsFromNY.flatten()
        reporter.setFlights(flightsFromNY)
        assertNotEquals(reporter.getAllFlightsFrom(Airports.NEW_YORK).flights, flightsFromNY)
        assertEquals(reporter.getAllFlightsFrom(Airports.NEW_YORK).flights.size(), 2)
        assertEquals(reporter.getAllFlightsFrom(Airports.NEW_YORK).flights, [newYork1, newYork2])
    }

    @Test
    void testPassengerAmount() {
        FlightsReporter reporter = new FlightsReporter(new Flights([]))
        assertEquals(reporter.getPassengerCount(), 0)
        def fl1 = new Flight(id: 4, from: Airports.SOFIA, destination: Airports.BERLIN,
                departure: LocalDateTime.of(2021, 8, 2, 15, 30),
                arrival: LocalDateTime.of(2021, 8, 2, 21, 10),
                passengers: 130, capacity: 150, runningLate: true)
        def fl2 = new Flight(id: 5, from: Airports.AMSTERDAM, destination: Airports.LONDON,
                departure: LocalDateTime.of(2021, 8, 1, 14, 45),
                arrival: LocalDateTime.of(2021, 8, 1, 16, 52),
                passengers: 90, capacity: 120, runningLate: true)
        def listOfFlights = [fl1, fl2]
        reporter.setFlights(listOfFlights)
        assertEquals(reporter.getPassengerCount(), fl1.passengers + fl2.passengers)
        assertEquals(reporter.getPassengerCountTo(Airports.BERLIN), 130)

        def fl3 = new Flight(id: 3, from: Airports.SOFIA, destination: Airports.LONDON,
                departure: LocalDateTime.of(2021, 8, 2, 11, 10),
                arrival: LocalDateTime.of(2021, 8, 2, 14, 10),
                passengers: 170, capacity: 180, runningLate: false)
        listOfFlights << fl3
        reporter.setFlights(listOfFlights)
        assertEquals(reporter.getPassengerCountTo(Airports.LONDON), fl2.passengers + fl3.passengers)
    }

    @Test
    void testSortingByDepartureDate() {
        def fl1 = new Flight(id: 7, from: Airports.LONDON, destination: Airports.NEW_YORK,
                departure: LocalDateTime.of(2021, 8, 1, 19, 35),
                arrival: LocalDateTime.of(2021, 8, 2, 3, 10),
                passengers: 180, capacity: 180, runningLate: false)
        def fl2 = new Flight(id: 10, from: Airports.BERLIN, destination: Airports.SOFIA,
                departure: LocalDateTime.of(2021, 8, 1, 6, 5),
                arrival: LocalDateTime.of(2021, 8, 1, 10, 10),
                passengers: 140, capacity: 150, runningLate: true)
        def fl3 = new Flight(id: 2, from: Airports.SOFIA, destination: Airports.AMSTERDAM,
                departure: LocalDateTime.of(2021, 8, 1, 8, 35),
                arrival: LocalDateTime.of(2021, 8, 1, 11, 05),
                passengers: 160, capacity: 180, runningLate: false)
        def listOfFlights = [fl1, fl2, fl3]
        def flightsSortedByDeparture = [fl2, fl3, fl1]
        FlightsReporter.sortByDepartureDate(listOfFlights)
        assertEquals(listOfFlights, flightsSortedByDeparture)
    }

    @Test
    void testSortingByArrivalDate() {
        def fl1 = new Flight(id: 7, from: Airports.LONDON, destination: Airports.NEW_YORK,
                departure: LocalDateTime.of(2021, 8, 1, 19, 35),
                arrival: LocalDateTime.of(2021, 8, 2, 3, 10),
                passengers: 180, capacity: 180, runningLate: false)
        def fl2 = new Flight(id: 10, from: Airports.BERLIN, destination: Airports.SOFIA,
                departure: LocalDateTime.of(2021, 8, 1, 6, 5),
                arrival: LocalDateTime.of(2021, 8, 1, 15, 10),
                passengers: 140, capacity: 150, runningLate: true)
        def fl3 = new Flight(id: 2, from: Airports.SOFIA, destination: Airports.AMSTERDAM,
                departure: LocalDateTime.of(2021, 8, 1, 8, 35),
                arrival: LocalDateTime.of(2021, 8, 1, 11, 05),
                passengers: 160, capacity: 180, runningLate: false)
        def listOfFlights = [fl1, fl2, fl3]
        def flightsSortedByArrival = [fl3, fl2, fl1]
        FlightsReporter.sortByArrivalDate(listOfFlights)
        assertEquals(listOfFlights, flightsSortedByArrival)
    }

    @Test
    void testSortingByFlightLen() {
        def fl1 = new Flight(id: 7, from: Airports.LONDON, destination: Airports.NEW_YORK,
                departure: LocalDateTime.of(2021, 8, 1, 19, 35),
                arrival: LocalDateTime.of(2021, 8, 2, 3, 10),
                passengers: 180, capacity: 180, runningLate: false)
        def fl2 = new Flight(id: 10, from: Airports.BERLIN, destination: Airports.SOFIA,
                departure: LocalDateTime.of(2021, 8, 1, 6, 5),
                arrival: LocalDateTime.of(2021, 8, 1, 15, 10),
                passengers: 140, capacity: 150, runningLate: true)
        def fl3 = new Flight(id: 2, from: Airports.SOFIA, destination: Airports.AMSTERDAM,
                departure: LocalDateTime.of(2021, 8, 1, 8, 35),
                arrival: LocalDateTime.of(2021, 8, 1, 11, 05),
                passengers: 160, capacity: 180, runningLate: false)
        def listOfFlights = [fl1, fl2, fl3]
        def flightsSortedByFlightLen = [fl3, fl1, fl2]
        FlightsReporter.sortByFlightLength(listOfFlights)
        assertEquals(listOfFlights, flightsSortedByFlightLen)
    }

    @Test
    void testGetAllPlanesAboveCapacity() {
        //def expected = []
        //expected << flights[0] << flights[5] << flights[6]
        //assertEquals(FlightsReporter.getAllPlanesAboveCertainCapacityFull(flights, 0.99), expected)
    }

    @Test
    void testRunningLate() {
        def fl1 = new Flight(id: 4, from: Airports.SOFIA, destination: Airports.BERLIN,
                departure: LocalDateTime.of(2021, 8, 2, 15, 30),
                arrival: LocalDateTime.of(2021, 8, 2, 21, 10),
                passengers: 130, capacity: 150, runningLate: true)
        def fl2 = new Flight(id: 3, from: Airports.SOFIA, destination: Airports.LONDON,
                departure: LocalDateTime.of(2021, 8, 2, 11, 10),
                arrival: LocalDateTime.of(2021, 8, 2, 14, 10),
                passengers: 170, capacity: 180, runningLate: false)
        def fl3 = new Flight(id: 1, from: Airports.PARIS, destination: Airports.AMSTERDAM,
                departure: LocalDateTime.of(2021, 8, 1, 10, 0),
                arrival: LocalDateTime.of(2021, 8, 1, 11, 10),
                passengers: 150, capacity: 150, runningLate: true)
        def listOfFlights = [fl2]
        FlightsReporter reporter = new FlightsReporter(new Flights(listOfFlights))
        assertEquals(false, reporter.isThereAFlightRunningLate())
        assertEquals(reporter.getFlightsRunningLate().flights, [])
        listOfFlights << fl1 << fl3
        assertEquals(true, reporter.isThereAFlightRunningLate())
        assertEquals(false, reporter.getAllFlightsTo(Airports.LONDON).isThereAFlightRunningLate())
        assertEquals(reporter.getFlightsRunningLate().flights, [fl1, fl3])
    }

    @Test
    void testDepartingAfter() {
        def fl1 = new Flight(id: 4, from: Airports.SOFIA, destination: Airports.BERLIN,
                departure: LocalDateTime.of(2021, 8, 2, 15, 30),
                arrival: LocalDateTime.of(2021, 8, 2, 21, 10),
                passengers: 130, capacity: 150, runningLate: true)
        def fl2 = new Flight(id: 3, from: Airports.SOFIA, destination: Airports.LONDON,
                departure: LocalDateTime.of(2021, 8, 2, 11, 10),
                arrival: LocalDateTime.of(2021, 8, 2, 14, 10),
                passengers: 170, capacity: 180, runningLate: false)
        def fl3 = new Flight(id: 1, from: Airports.PARIS, destination: Airports.AMSTERDAM,
                departure: LocalDateTime.of(2021, 8, 1, 10, 0),
                arrival: LocalDateTime.of(2021, 8, 1, 11, 10),
                passengers: 150, capacity: 150, runningLate: true)
        FlightsReporter reporter = new FlightsReporter(new Flights([]))
        assertEquals(reporter.getAllFlightsDepartingBefore(LocalDateTime.now().plusDays(10)).flights, [])
        def listOfFlights = [fl1, fl2, fl3]
        reporter.setFlights(listOfFlights)
        assertEquals(
                reporter.getAllFlightsDepartingBefore(
                        LocalDateTime.of(2021, 8, 1, 1, 1)).flights, [])
        assertEquals(
                reporter.getAllFlightsDepartingBefore(
                        LocalDateTime.of(2021, 8, 2, 1, 0)).flights, [fl3])
    }


    @Before
    void initializeTestData() {
        Flights fl = new Flights()
        fl.flights = [new Flight(id: 1,
                from: Airports.PARIS,
                destination: Airports.AMSTERDAM,
                departure: LocalDateTime.of(2021, 8, 1, 10, 0),
                arrival: LocalDateTime.of(2021, 8, 1, 11, 10),
                passengers: 150, capacity: 150, runningLate: true),
                      new Flight(id: 2,
                              from: Airports.SOFIA,
                              destination: Airports.AMSTERDAM,
                              departure: LocalDateTime.of(2021, 8, 1, 8, 35),
                              arrival: LocalDateTime.of(2021, 8, 1, 11, 05),
                              passengers: 160, capacity: 180, runningLate: false),
                      new Flight(id: 3,
                              from: Airports.SOFIA,
                              destination: Airports.LONDON,
                              departure: LocalDateTime.of(2021, 8, 2, 11, 10),
                              arrival: LocalDateTime.of(2021, 8, 2, 14, 10),
                              passengers: 170, capacity: 180, runningLate: false),
                      new Flight(id: 4,
                              from: Airports.SOFIA,
                              destination: Airports.BERLIN,
                              departure: LocalDateTime.of(2021, 8, 2, 15, 30),
                              arrival: LocalDateTime.of(2021, 8, 2, 21, 10),
                              passengers: 130, capacity: 150, runningLate: true),
                      new Flight(id: 5,
                              from: Airports.AMSTERDAM,
                              destination: Airports.LONDON,
                              departure: LocalDateTime.of(2021, 8, 1, 14, 45),
                              arrival: LocalDateTime.of(2021, 8, 1, 16, 52),
                              passengers: 90, capacity: 120, runningLate: true),
                      new Flight(id: 6,
                              from: Airports.PARIS,
                              destination: Airports.NEW_YORK,
                              departure: LocalDateTime.of(2021, 8, 1, 7, 5),
                              arrival: LocalDateTime.of(2021, 8, 2, 1, 10),
                              passengers: 180, capacity: 180, runningLate: true),
                      new Flight(id: 7,
                              from: Airports.LONDON,
                              destination: Airports.NEW_YORK,
                              departure: LocalDateTime.of(2021, 8, 1, 19, 35),
                              arrival: LocalDateTime.of(2021, 8, 2, 3, 10),
                              passengers: 180, capacity: 180, runningLate: false),
                      new Flight(id: 8,
                              from: Airports.BERLIN,
                              destination: Airports.LONDON,
                              departure: LocalDateTime.of(2021, 8, 3, 20, 0),
                              arrival: LocalDateTime.of(2021, 8, 3, 21, 20),
                              passengers: 140, capacity: 150, runningLate: false),
                      new Flight(id: 9,
                              from: Airports.PARIS,
                              destination: Airports.SOFIA,
                              departure: LocalDateTime.of(2021, 8, 2, 18, 0),
                              arrival: LocalDateTime.of(2021, 8, 3, 2, 10),
                              passengers: 90, capacity: 150, runningLate: true),
                      new Flight(id: 10,
                              from: Airports.BERLIN,
                              destination: Airports.SOFIA,
                              departure: LocalDateTime.of(2021, 8, 1, 6, 5),
                              arrival: LocalDateTime.of(2021, 8, 1, 10, 10),
                              passengers: 140, capacity: 150, runningLate: true)
        ]

        flightsReporter = new FlightsReporter(fl)
    }
}
