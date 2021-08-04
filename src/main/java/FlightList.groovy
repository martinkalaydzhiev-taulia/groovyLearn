import java.time.LocalDateTime

interface FlightList {
    List<Flight> getAllFlightsTo(Airports destination)

    List<Flight> getAllFlightsFrom(Airports from)

    void printFlights()

    void printFlightsTo(Airports destination)

    void printFlightsFrom(Airports to)

    int getPassengerCount()

    int getPassengerCountTo(Airports to)

    int passengerAmountFrom(Airports from)

    List<Flight> getAllPlanesAboveCertainCapacityFull(float percentageFull)

    boolean isThereAFlightRunningLate()

    List<Flight> getFlightsRunningLate()

    List<Flight> getAllFlightsDepartingAfter(LocalDateTime date)

    List<Flight> getAllFlightsDepartingBefore(LocalDateTime date)

    List<Flight> getAllFlightsArrivingBefore(LocalDateTime date)

    List<Flight> getAllFlightsArrivingAfter(LocalDateTime date)

    List<Flight> getAllFlightsShorterThan(int amountInMinutes)

    List<Flight> getAllFlightsLongerThan(int amountInMinutes)

    List<Flight> getAllFlightWithDuration(int duration)

    List<Flight> getAllFlightsWithPassengersAbove(int passengerCount)

    List<Flight> getAllFlightsWithPassengersBelow(int passengerCount)

    Flight getFlightWithMostPassengers()

    Flight getFlightWithLeastPassengers()

    void changeDestinationForAllTo(Airports from, Airports to)

    void changeStartingAirport(Airports from, Airports to)

    void changeDepartureDate(String flightID, LocalDateTime newDate)

    void changeArrivalDate(String flightID, LocalDateTime newDate)

    void setRunningLateToAllFlightsTo(Airports destination)
}