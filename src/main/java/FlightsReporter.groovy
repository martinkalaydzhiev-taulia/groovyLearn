import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class FlightsReporter {
    final static int[] CAPACITIES = new int[] {80, 120, 150, 180, 240}

    static List<Flight> getAllFlightsTo(List<Flight> flights, Airports destination) {
        flights.findAll { (it.destination == destination) }
    }

    static List<Flight> getAllFlightsFrom(List<Flight> flights, Airports from) {
        flights.findAll {it.from == from }
    }

    static void printFlights(List<Flight> flights) {
        flights.each {println(it)}
    }

    static int passengerAmount(List<Flight> flights) {
        flights.collect {it.passengers}.sum() ?: 0 as int
    }

    static int passengerAmountTo(List<Flight> flights, Airports to) {
        flights.findAll {it.destination == to }.collect {it.passengers}.sum() ?: 0 as int
    }

    static int passengerAmountFrom(List<Flight> flights, Airports from) {
        flights.findAll {it.from == from }.collect {it.passengers }.sum() ?: 0 as int
    }

    static void sortByFlightLength(List<Flight> flights) {
        flights.sort {a, b ->
            (int) Math.signum(
                    ChronoUnit.MINUTES.between(a.departure, a.arrival) - ChronoUnit.MINUTES.between(b.departure, b.arrival)
            )
        }
    }

    static void sortByDepartureDate(List<Flight> flights) {
        flights.sort {a, b -> (a.departure <=> b.departure) }
    }

    static void sortByArrivalDate(List<Flight> flights) {
        flights.sort { a, b -> a.arrival <=> b.arrival}
    }

    static int flightLengthInMinutes(Flight flight) {
        ChronoUnit.MINUTES.between(flight.departure, flight.arrival)
    }

    static List<Flight> getAllPlanesAboveCertainCapacityFull(List<Flight> flights, float percentageFull) {
        flights.findAll { (double)it.passengers / (double)it.capacity > percentageFull}
    }

    static boolean isThereAFlightRunningLate(List<Flight> flights) {
        flights.any {it.runningLate }
    }

    static List<Flight> getFlightsRunningLate(List<Flight> flights) {
        flights.findAll{it.runningLate }
    }

    static List<Flight> getAllFlightsDepartingAfter(List<Flight> flights, LocalDateTime date) {
        flights.findAll {it.departure >= date }
    }

    static List<Flight> getAllFlightsDepartingBefore(List<Flight> flights, LocalDateTime date) {
        flights.findAll{ it.departure < date }
    }
//
    static List<Flight> getAllFlightsArrivingBefore(List<Flight> flights, LocalDateTime date) {
        flights.findAll {it.arrival < date }
    }

    static List<Flight> getAllFlightsArrivingAfter(List<Flight> flights, LocalDateTime date) {
        flights.findAll {it.arrival >= date }
    }

    static List<Flight> getAllFlightsShorterThan(List<Flight> flights, int amountInMinutes) {
        flights.findAll() {flightLengthInMinutes(it) < amountInMinutes }
    }

    static List<Flight> getAllFlightsLongerThan(List<Flight> flights, int amountInMinutes) {
        flights.findAll { flightLengthInMinutes(it) > amountInMinutes}
    }

    static List<Flight> getAllFlightWithDuration(List<Flight> flights, int duration) {
        flights.findAll {flightLengthInMinutes(it) == duration }
    }

    static List<Flight> getAllFlightsWithPassengersAbove(List<Flight> flights, int passengerAmount) {
        flights.findAll{it.passengers >= passengerAmount }
    }

    static List<Flight> getAllFlightsWithPassengersBelow(List<Flight> flights, int passengerAmount) {
        flights.findAll { it.passengers < passengerAmount }
    }

    static Flight getFlightWithMostPassengers(List<Flight> flights) {
        flights.max {it.passengers}
    }

    static Flight getFlightWithLeastPassengers(List<Flight> flights) {
        flights.min {it.passengers}
    }

    static void changeDestinationTo(List<Flight> flights, Airports from, Airports to) {
        flights.each {
            if (it.destination == from)
                it.destination = to
        }
    }

    static void changeStartingAirport(List<Flight> flights, Airports from, Airports to) {
        flights.each {
            if (it.from == from)
                it.from == to
        }
    }

    static void changeDepartureDate(List<Flight> flights, String flightID, LocalDateTime newDate) {
        flights.find {it.id == flightID }.departure = newDate
    }

    static void changeArrivalDate(List<Flight> flights, String flightID, LocalDateTime newDate) {
        flights.find {it.id = flightID }.arrival = newDate
    }

    static void setRunningLateToAirport(List<Flight> flights, Airports destination) {
        flights.each {
            if (it.destination == destination)
                it.runningLate = true
        }
    }



    static void main(String[] args) {
        def flights = []

        //createFlights(flights)
        20.times {flights << generateFlight()}
        println("Flights to London")
        def fl = flights.findAll {it.destination == Airports.LONDON}.each {println(it.toString())}
        println()
        println("All flights")
        flights.each { println(it.toString())}
        println()
        flights.groupBy { it.destination}.each {println("$it.key $it.value.id")}
        println()

        def passengersToSof = flights.findAll { it.destination == Airports.SOFIA }.collect {it.passengers}.sum()
        println("Passengers to Sofia - ${passengersToSof}")
        println()

        boolean isAnyFlightUnderHalfCapacity = flights.every {(double)it.passengers / (double)it.capacity >= 0.5}
        println("Is every flight full at least 50% of its capacity? ${isAnyFlightUnderHalfCapacity ? "Yes" : "No"} ")
        println()

        // Is there a flight to London that is running late
        boolean isAnyFlightToLondonLate = flights.findAll {it.destination == Airports.LONDON }.any { it.runningLate}
        println("Is there a flight to London running late? ${isAnyFlightToLondonLate ? "Yes" : "No"}")
        println()
        println("Flights to London running late")
        flights.findAll {it.runningLate && it.destination == Airports.LONDON }
            .each {println(it.toString())}
        println()

        //assert 1 == 2

        int passengersLate = flights.findAll { it.runningLate == true}.collect { it.passengers }.sum()
        println("Passengers running late ${passengersLate}")
        println()

        println("All flights to Sofia that are running late:")
        flights.findAll { it.destination == Airports.SOFIA && it.runningLate == true}.each { println(it)}
        println()

        println("The first flight that is running late and more than 95% of the capacity is full")
        println(flights.findAll { it.runningLate == true }
                .find { (double)it.passengers / (double)it.capacity > 0.95 }.toString())
        println()

        println("All flights from Sofia")
        flights.findAll { it.from == Airports.SOFIA}.each { println(it.toString()) }

        println()
        println("Flights sorted by their length in minutes")
        flights.sort { a, b ->
            int diff = ChronoUnit.MINUTES.between(a.departure, a.arrival) - ChronoUnit.MINUTES.between(b.departure, b.arrival)
            diff > 0 ? 1 : diff < 0 ? -1 : 0 }
                .collect { "${it.toString()} flight length: ${ChronoUnit.MINUTES.between(it.departure, it.arrival)}"}
                .each {println(it.toString())}

        println("Sorted by day of arrival ascending")
        flights.sort { it.arrival }
                .each { println(it.toString()) }


        println("Sorted by day of arrival descending")
        flights.sort {a, b -> b.arrival.compareTo(a.arrival)}.each {println(it.toString())}

        println()
        def asd = getAllFlightsTo(flights, Airports.AMSTERDAM)
        asd.each {println(it)}

        println()
        println(passengerAmount(asd))

        println()
        sortByFlightLength(flights)
        printFlights(flights)
        flights.each {println(flightLengthInMinutes(it))}

        printFlights(getAllPlanesAboveCertainCapacityFull(flights, 0.95))
        println()
        def fl3 = getFlightsRunningLate(flights)
        printFlights(fl3)

        println()
        fl3 = getAllFlightsFrom(flights, Airports.SOFIA)
        printFlights(fl3)

        println()
        def fl4 = getAllFlightsDepartingAfter(flights, LocalDateTime.now().plusDays(15))
        printFlights(fl4)

        println()
        def fl5 = getFlightWithMostPassengers(flights)
        println(fl5)

        println()
        changeDestinationTo(flights, Airports.SOFIA, Airports.PARIS)
        printFlights(flights)
    }

    static Flight generateFlight() {
        Random r = new Random()
        int numberOfAirports = Airports.getEnumConstants().length
        int airportFromNumber = r.nextInt(numberOfAirports)
        Airports from = Airports.getEnumConstants()[airportFromNumber]
        Airports destination = Airports.getEnumConstants()[(airportFromNumber + 1 + r.nextInt(numberOfAirports - 2)) % numberOfAirports]
        String id = from.toString() + destination.toString() + r.nextInt(100)
        int capacity = CAPACITIES[r.nextInt(CAPACITIES.length)]
        int passengers = capacity - r.nextInt(capacity) / 3
        boolean isLate = r.nextBoolean()

        LocalDateTime departure = LocalDateTime.now().plusDays(r.nextInt(30)).plusHours(r.nextInt(24))
                .plusMinutes(r.nextInt(60)).withSecond(0).withNano(0)
        LocalDateTime arrival = departure.plusDays(r.nextInt(2)).plusHours(r.nextInt(24)).plusMinutes(r.nextInt(60))
                .withSecond(0).withNano(0)

        return new Flight(id: id, from: from, destination: destination, departure: departure, arrival: arrival,
                passengers: passengers, capacity: capacity, runningLate: isLate)
    }

}