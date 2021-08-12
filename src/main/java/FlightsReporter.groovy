import groovyx.gpars.dataflow.stream.FList

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class FlightsReporter {
    EmailService emailService

    static void printFlights(Flights flights) {
        flights.printFlights()
    }

    static void printFlightsTo(Flights flights, Airports destination) {
        flights.printFlightsTo(destination)
    }

    static void printFlightsFrom(Flights flights, Airports from) {
        flights.printFlightsFrom(from)
    }

    static int getPassengerCount(Flights flights) {
        flights.getPassengerCount()
    }

    static int getPassengerCountTo(Flights flights, Airports to) {
        flights.getPassengerCountTo(to)
    }

    static int passengerAmountFrom(Flights flights, Airports from) {
        flights.passengerAmountFrom(from)
    }

    static Flights getAllFlightsFrom(Flights flights, Airports from) {
        flights.getAllFlightsFrom(from)
    }

    static Flights getAllFlightsToAndSendEmail(flights, Airports to) {
        flights.getAllFlightsToAndSendEmail(to)
    }
    static Flights getAllFlightsTo(Flights flights, Airports to) {
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

    static int flightLengthInMinutes(Flights flights, String flightID) {
        getFlightByID(flights, flightID).flightLengthInMinutes()
    }

    static Flight getFlightByID(Flights flights, String flightID) {
        flights.getFlightByID(flightID)
    }

    static List<Flight> getAllPlanesAboveCertainCapacityFull(Flights flights, float percentageFull) {
        flights.getAllPlanesAboveCertainCapacityFull(percentageFull)
    }

    static boolean isThereAFlightRunningLate(Flights flights) {
        flights.isThereAFlightRunningLate()
    }

    static Flights getFlightsRunningLate(Flights flights) {
        flights.getFlightsRunningLate()
    }

    static Flights getAllFlightsDepartingAfter(Flights flights, LocalDateTime date) {
        flights.getAllFlightsDepartingAfter(date)
    }

    static Flights getAllFlightsDepartingBefore(Flights flights, LocalDateTime date) {
        flights.getAllFlightsDepartingBefore(date)
    }

    static Flights getAllFlightsArrivingBefore(Flights flights, LocalDateTime date) {
        flights.getAllFlightsArrivingBefore(date)
    }

    static Flights getAllFlightsArrivingAfter(Flights flights, LocalDateTime date) {
        flights.getAllFlightsArrivingAfter(date)
    }

    static Flights getAllFlightsShorterThan(Flights flights, int amountInMinutes) {
        flights.getAllFlightsShorterThan(amountInMinutes)
    }

    static Flights getAllFlightsLongerThan(Flights flights, int amountInMinutes) {
        flights.getAllFlightsLongerThan(amountInMinutes)
    }

    static Flights getAllFlightWithDuration(Flights flights, int duration) {
        flights.getAllFlightWithDuration(duration)
    }

    static Flights getAllFlightsWithPassengersAbove(Flights flights, int passengerCount) {
        flights.getAllFlightsWithPassengersAbove(passengerCount)
    }

    static Flights getAllFlightsWithPassengersBelow(Flights flights, int passengerAmount) {
        flights.getAllFlightsWithPassengersBelow(passengerAmount)
    }

    static Flight getFlightWithMostPassengers(Flights flights) {
        flights.getFlightWithMostPassengers()
    }

    static Flight getFlightWithLeastPassengers(Flights flights) {
        flights.getFlightWithLeastPassengers()
    }

    static void changeDestinationForAllTo(Flights flights, Airports from, Airports to) {
        flights.changeDestinationForAllTo(from, to)
    }

    static void changeStartingAirport(Flights flights, Airports from, Airports to) {
        flights.changeStartingAirport(from, to)
    }

    static void changeDepartureDate(Flights flights, String flightID, LocalDateTime newDate) {
        flights.changeDepartureDate(flightID, newDate)
    }

    static void changeArrivalDate(Flights flights, String flightID, LocalDateTime newDate) {
        flights.changeArrivalDate(flightID, newDate)
    }

    static void setRunningLateToAllFlightsToAirport(Flights flights, Airports destination) {
        flights.setRunningLateToAllFlightsTo(destination)
    }

}