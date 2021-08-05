import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class FlightsReporter {
    Flights flights

    FlightsReporter(Flights flights) {
        this.flights = flights
    }

    void setFlights(List<Flight> fl) {
        flights.setFlights(fl)
    }

    void printFlights() {
        flights.printFlights()
    }

    void printFlightsTo(Airports destination) {
        flights.printFlightsTo(destination)
    }

    void printFlightsFrom(Airports from) {
        flights.printFlightsFrom(from)
    }

    int getPassengerCount() {
        flights.getPassengerCount()
    }

    int getPassengerCountTo(Airports to) {
        flights.getPassengerCountTo(to)
    }

    int passengerAmountFrom(Airports from) {
        flights.passengerAmountFrom(from)
    }

    Flights getAllFlightsFrom(Airports from) {
        flights.getAllFlightsFrom(from)
    }

    FlightList getAllFlightsTo(Airports to) {
        flights.getAllFlightsTo(to)
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

    int flightLengthInMinutes(String flightID) {
        getFlightByID(flightID).flightLengthInMinutes()
    }

    Flight getFlightByID(String flightID) {
        flights.getFlightByID(flightID)
    }

    List<Flight> getAllPlanesAboveCertainCapacityFull(float percentageFull) {
        flights.getAllPlanesAboveCertainCapacityFull(percentageFull)
    }

    boolean isThereAFlightRunningLate() {
        flights.isThereAFlightRunningLate()
    }

    List<Flight> getFlightsRunningLate() {
        flights.getFlightsRunningLate()
    }

    List<Flight> getAllFlightsDepartingAfter(LocalDateTime date) {
        flights.getAllFlightsArrivingAfter(date)
    }

    List<Flight> getAllFlightsDepartingBefore(LocalDateTime date) {
        flights.getAllFlightsDepartingBefore(date)
    }

    List<Flight> getAllFlightsArrivingBefore(LocalDateTime date) {
        flights.getAllFlightsArrivingBefore(date)
    }

    List<Flight> getAllFlightsArrivingAfter(LocalDateTime date) {
        flights.getAllFlightsArrivingAfter(date)
    }

    List<Flight> getAllFlightsShorterThan(int amountInMinutes) {
        flights.getAllFlightsShorterThan(amountInMinutes)
    }

    List<Flight> getAllFlightsLongerThan(int amountInMinutes) {
        flights.getAllFlightsLongerThan(amountInMinutes)
    }

    List<Flight> getAllFlightWithDuration(int duration) {
        flights.getAllFlightWithDuration(duration)
    }

    List<Flight> getAllFlightsWithPassengersAbove(int passengerCount) {
        flights.getAllFlightsWithPassengersAbove(passengerCount)
    }

    List<Flight> getAllFlightsWithPassengersBelow(int passengerAmount) {
        flights.getAllFlightsWithPassengersBelow(passengerAmount)
    }

    Flight getFlightWithMostPassengers() {
        flights.getFlightWithMostPassengers()
    }

    Flight getFlightWithLeastPassengers() {
        flights.getFlightWithLeastPassengers()
    }

    void changeDestinationForAllTo(Airports from, Airports to) {
        flights.changeDestinationForAllTo(from, to)
    }

    void changeStartingAirport(Airports from, Airports to) {
        flights.changeStartingAirport(from, to)
    }

    void changeDepartureDate(String flightID, LocalDateTime newDate) {
        flights.changeDepartureDate(flightID, newDate)
    }

    void changeArrivalDate(String flightID, LocalDateTime newDate) {
        flights.changeArrivalDate(flightID, newDate)
    }

    void setRunningLateToAllFlightsToAirport(Airports destination) {
        flights.setRunningLateToAllFlightsTo(destination)
    }

}