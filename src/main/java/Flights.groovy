import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Flights implements FlightList {
    List<Flight> flights

    void setFlights(List<Flight> fl) {
        flights = fl
    }

    boolean equals(Object flights1) {
        if (Object instanceof Flights)
            return flights == ((Flights) flights1).flights
    }

    Flights(List<Flight> flights) {
        this.flights = flights
    }

    Flights getAllFlightsTo(Airports destination) {
        new Flights(flights.findAll { (it.destination == destination) })
    }

    Flights getAllFlightsFrom(Airports from) {
        new Flights(flights.findAll { it.from == from })
    }

    void printFlights() {
        flights.each { println(it) }
    }

    void printFlightsTo(Airports destination) {
        flights.findAll { it.destination == destination }.each { println(it) }
    }

    void printFlightsFrom(Airports from) {
        flights.findAll { it.from == from }.each { println(it) }
    }

    int getPassengerCount() {
        flights.collect { it.passengers }.sum() as BigDecimal ?: 0 as BigDecimal
    }

    int getPassengerCountTo(Airports to) {
        flights.findAll { it.destination == to }.collect { it.passengers }.sum() as BigDecimal ?: 0 as BigDecimal
    }

    int passengerAmountFrom(Airports from) {
        flights.findAll { it.from == from }.collect { it.passengers }.sum() as BigDecimal ?: 0 as BigDecimal
    }

    static void sortByFlightLength(List<Flight> flights) {
        flights.sort { a, b ->
            (int) Math.signum(
                    ChronoUnit.MINUTES.between(a.departure, a.arrival) - ChronoUnit.MINUTES.between(b.departure, b.arrival)
            )
        }
    }

    static void sortByDepartureDate(List<Flight> flights) {
        flights.sort { a, b -> (a.departure <=> b.departure) }
    }

    static void sortByArrivalDate(List<Flight> flights) {
        flights.sort { a, b -> a.arrival <=> b.arrival }
    }


    Flights getAllPlanesAboveCertainCapacityFull(float percentageFull) {
        new Flights(flights.findAll { (double) it.passengers / (double) it.capacity > percentageFull })
    }

    boolean isThereAFlightRunningLate() {
        flights.any { it.runningLate }
    }

    Flights getFlightsRunningLate() {
        new Flights(flights.findAll { it.runningLate })
    }

    Flights getAllFlightsDepartingAfter(LocalDateTime date) {
        new Flights(flights.findAll { it.departure >= date })
    }

    Flights getAllFlightsDepartingBefore(LocalDateTime date) {
        new Flights(flights.findAll { it.departure < date })
    }

    Flights getAllFlightsArrivingBefore(LocalDateTime date) {
        new Flights(flights.findAll { it.arrival < date })
    }

    Flights getAllFlightsArrivingAfter(LocalDateTime date) {
        new Flights(flights.findAll { it.arrival >= date })
    }

    Flights getAllFlightsShorterThan(int amountInMinutes) {
        new Flights(flights.findAll() { it.flightLengthInMinutes() < amountInMinutes })
    }

    Flights getAllFlightsLongerThan(int amountInMinutes) {
        new Flights(flights.findAll { it.flightLengthInMinutes() > amountInMinutes })
    }

    Flights getAllFlightWithDuration(int duration) {
        new Flights(flights.findAll { it.flightLengthInMinutes() == duration })
    }

    Flights getAllFlightsWithPassengersAbove(int passengerAmount) {
        new Flights(flights.findAll { it.passengers >= passengerAmount })
    }

    Flights getAllFlightsWithPassengersBelow(int passengerAmount) {
        new Flights(flights.findAll { it.passengers < passengerAmount })
    }

    Flight getFlightWithMostPassengers() {
        flights.max { it.passengers }
    }

    Flight getFlightWithLeastPassengers() {
        flights.min { it.passengers }
    }

    Flight getFlightByID(int id) {
        flights.each {
            if (it.id == id)
                return it
        } as Flight
        return null
    }

    Flights changeDestinationForAllTo(Airports from, Airports to) {
        flights.each {
            if (it.destination == from)
                it.destination = to
        }
        return this
    }

    Flights changeStartingAirport(Airports from, Airports to) {
        flights.each {
            if (it.from == from)
                it.from == to
        }
        return this
    }

    Flights changeDepartureDate(int flightID, LocalDateTime newDate) {
        new Flights(flights.find { it.id == flightID }.departure = newDate)
    }

    Flights changeArrivalDate(int flightID, LocalDateTime newDate) {
        new Flights(flights.find { it.id = flightID }.arrival = newDate)
    }

    Flights setRunningLateToAllFlightsTo(Airports destination) {
        flights.each {
            if (it.destination == destination)
                it.runningLate = true
        }
        return this
    }
}
