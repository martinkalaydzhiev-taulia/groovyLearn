import groovy.transform.ToString

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Flights {
    final static int[] CAPACITIES = new int[] {80, 120, 150, 180, 240}

    enum Airport {
        SOFIA, BERLIN, LONDON, NEW_YORK, AMSTERDAM, PARIS
    }

    static void main(String[] args) {
        def flights = []

        //createFlights(flights)
        20.times {flights << generateFlight()}
        def fl = flights.findAll {it.destination == Airport.LONDON}.each {println(it.toString())}
        println("******")
        println()
        println()
        flights.each { println(it.toString())}

        println()
        flights.groupBy { it.destination}.each {println("$it.key $it.value.id")}

        def passengersToSof = flights.findAll { it.destination == Airport.SOFIA }.collect {it.passengers}.sum()
        println("Passengers to Sofia - ${passengersToSof}")

        boolean isAnyFlightUnderHalfCapacity = flights.every {(double)it.passengers / (double)it.capacity >= 0.5}
        println("Is every flight full at least 50% of its capacity? ${isAnyFlightUnderHalfCapacity ? "Yes" : "No"} ")

        // Is there a flight to London that is running late
        boolean isAnyFlightToLondonLate = flights.findAll {it.destination == Airport.LONDON }.any { it.runningLate}
        println("Is there a flight to London running late? ${isAnyFlightToLondonLate ? "Yes" : "No"}")
        println("Flights to London running late")
        flights.findAll {it.runningLate && it.destination == Airport.LONDON }
            .each {println(it.toString())}



        int passengersLate = flights.findAll { it.runningLate == true}.collect { it.passengers }.sum()
        println("Passengers running late ${passengersLate}")

        println("All flights to Sofia that are running late:")
        flights.findAll { it.destination == Airport.SOFIA && it.runningLate == true}.each { println(it)}

        println("The first flight that is running late and more than 95% of the capacity is full")
        println(flights.findAll { it.runningLate == true }
                .find { (double)it.passengers / (double)it.capacity > 0.95 }.toString())

        println("All flights from Sofia")
        flights.findAll { it.from == Airport.SOFIA}.each { println(it.toString()) }

        println("Sorted by day of arrival ascending")
        flights.sort { it.arrival }
                .each { println(it.toString()) }

        println("Sorted by day of arrival descending")
        flights.sort {a, b -> b.arrival.compareTo(a.arrival)}.each {println(it.toString())}

    }

    static Flight generateFlight() {
        Random r = new Random();
        int numberOfAirports = Airport.getEnumConstants().length
        int airportFromNumber = r.nextInt(numberOfAirports)
        Airport from = Airport.getEnumConstants()[airportFromNumber]
        Airport destination = Airport.getEnumConstants()[(airportFromNumber + 1 + r.nextInt(numberOfAirports - 2)) % numberOfAirports]
        String id = from.toString() + destination.toString() + r.nextInt(100)
        int capacity = CAPACITIES[r.nextInt(CAPACITIES.length)]
        int passengers = capacity - r.nextInt(capacity) / 3
        boolean isLate = r.nextBoolean()

        LocalDateTime departure = LocalDateTime.now().plusMonths(r.nextInt(12)).plusDays(r.nextInt(30)).plusHours(r.nextInt(24))
                .plusMinutes(r.nextInt(60)).withSecond(0).withNano(0)
        LocalDateTime arrival = departure.plusDays(r.nextInt(20)).plusHours(r.nextInt(24)).plusMinutes(r.nextInt(60))
                .withSecond(0).withNano(0)

        return new Flight(id: id, from: from, destination: destination, departure: departure, arrival: arrival,
                passengers: passengers, capacity: capacity, runningLate: isLate)
    }

}

@ToString(includeNames = true, includeFields = true)
class Flight {
    String id
    Flights.Airport from
    Flights.Airport destination
    LocalDateTime departure
    LocalDateTime arrival
    int passengers
    int capacity
    boolean runningLate
}