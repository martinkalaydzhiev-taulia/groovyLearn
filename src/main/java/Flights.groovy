import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Flights implements FlightList {
    List<Flight> flights

    List<Flight> getAllFlightsTo(Airports destination) {
        flights.findAll { (it.destination == destination) }
    }

    List<Flight> getAllFlightsFrom(Airports from) {
        flights.findAll { it.from == from }
    }

    static void printFlights(List<Flight> flights) {
        flights.each { println(it) }
    }

    void printFlights() {
        flights.each { println(it) }
    }

    void printFlightsTo(Airports destination) {
        flights.findAll { it.destination == destination }.each { println(it) }
    }

    void printFlightsFrom(Airports from) {
        flights.findAll { it.from == from }.each {println(it)}
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


    List<Flight> getAllPlanesAboveCertainCapacityFull(float percentageFull) {
        flights.findAll { (double) it.passengers / (double) it.capacity > percentageFull }
    }

    boolean isThereAFlightRunningLate() {
        flights.any { it.runningLate }
    }

    List<Flight> getFlightsRunningLate() {
        flights.findAll { it.runningLate }
    }

    List<Flight> getAllFlightsDepartingAfter(LocalDateTime date) {
        flights.findAll { it.departure >= date }
    }

    List<Flight> getAllFlightsDepartingBefore(LocalDateTime date) {
        flights.findAll { it.departure < date }
    }

    List<Flight> getAllFlightsArrivingBefore(LocalDateTime date) {
        flights.findAll { it.arrival < date }
    }

    List<Flight> getAllFlightsArrivingAfter(LocalDateTime date) {
        flights.findAll { it.arrival >= date }
    }

    List<Flight> getAllFlightsShorterThan(int amountInMinutes) {
        flights.findAll() { it.flightLengthInMinutes() < amountInMinutes }
    }

    List<Flight> getAllFlightsLongerThan(int amountInMinutes) {
        flights.findAll { it.flightLengthInMinutes() > amountInMinutes }
    }

    List<Flight> getAllFlightWithDuration(int duration) {
        flights.findAll { it.flightLengthInMinutes() == duration }
    }

    List<Flight> getAllFlightsWithPassengersAbove(int passengerAmount) {
        flights.findAll { it.passengers >= passengerAmount }
    }

    List<Flight> getAllFlightsWithPassengersBelow(int passengerAmount) {
        flights.findAll { it.passengers < passengerAmount }
    }

    Flight getFlightWithMostPassengers() {
        flights.max { it.passengers }
    }

    Flight getFlightWithLeastPassengers() {
        flights.min { it.passengers }
    }

    Flight getFlightByID(String id) {
        flights.each {
            if (it.id == id)
                return it
        } as Flight
        return null
    }

    void changeDestinationForAllTo(Airports from, Airports to) {
        flights.each {
            if (it.destination == from)
                it.destination = to
        }
    }

    void changeStartingAirport(Airports from, Airports to) {
        flights.each {
            if (it.from == from)
                it.from == to
        }
    }

    void changeDepartureDate(String flightID, LocalDateTime newDate) {
        flights.find { it.id == flightID }.departure = newDate
    }

    void changeArrivalDate(String flightID, LocalDateTime newDate) {
        flights.find { it.id = flightID }.arrival = newDate
    }

    void setRunningLateToAllFlightsTo(Airports destination) {
        flights.each {
            if (it.destination == destination)
                it.runningLate = true
        }
    }
}
