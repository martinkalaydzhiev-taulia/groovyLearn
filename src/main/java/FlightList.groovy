import java.time.LocalDateTime

interface FlightList {
    Flights getAllFlightsTo(Airports destination)

    Flights getAllFlightsFrom(Airports from)

    void printFlights()

    void printFlightsTo(Airports destination)

    void printFlightsFrom(Airports to)

    int getPassengerCount()

    int getPassengerCountTo(Airports to)

    int passengerAmountFrom(Airports from)

    Flights getAllPlanesAboveCertainCapacityFull(float percentageFull)

    boolean isThereAFlightRunningLate()

    Flights getFlightsRunningLate()

    Flights getAllFlightsDepartingAfter(LocalDateTime date)

    Flights getAllFlightsDepartingBefore(LocalDateTime date)

    Flights getAllFlightsArrivingBefore(LocalDateTime date)

    Flights getAllFlightsArrivingAfter(LocalDateTime date)

    Flights getAllFlightsShorterThan(int amountInMinutes)

    Flights getAllFlightsLongerThan(int amountInMinutes)

    Flights getAllFlightWithDuration(int duration)

    Flights getAllFlightsWithPassengersAbove(int passengerCount)

    Flights getAllFlightsWithPassengersBelow(int passengerCount)

    Flight getFlightWithMostPassengers()

    Flight getFlightWithLeastPassengers()

    Flights changeDestinationForAllTo(Airports from, Airports to)

    Flights changeStartingAirport(Airports from, Airports to)

    Flights changeDepartureDate(int flightID, LocalDateTime newDate)

    Flights changeArrivalDate(int flightID, LocalDateTime newDate)

    Flights setRunningLateToAllFlightsTo(Airports destination)
}