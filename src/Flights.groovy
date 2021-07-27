import groovy.transform.ToString

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Flights {
    static airports = [Sofia: "SOF", Munich: "MUC", London: "LON"]

    static void main(String[] args) {
        def flights = []
        //createFlights(flights)
        flights << generateFlight("sofmuc1", airports['Sofia'], airports['Munich'],
                    LocalDateTime.of(2021, 07, 05, 10, 45),
                    LocalDateTime.of(2021, 07, 05, 15, 45),
                140, 160, false)
                << generateFlight("sofmuc2", airports['Sofia'], airports['Munich'],
                    LocalDateTime.of(2021, 07, 03, 16, 45),
                    LocalDateTime.of(2021, 07, 03, 18, 45),
                160, 160, false)
                << generateFlight("sofmuc3", airports['Sofia'], airports['Munich'],
                    LocalDateTime.of(2021, 07, 10, 20, 45),
                    LocalDateTime.of(2021, 07, 10, 23, 30),
                90, 150, false)
                << generateFlight("mucsof1", airports['Munich'], airports['Sofia'],
                    LocalDateTime.of(2021, 07, 01, 19, 15),
                    LocalDateTime.of(2021, 07, 01, 21, 35),
                113, 180, false)
                << generateFlight("mucsof2", airports['Munich'], airports['Sofia'],
                    LocalDateTime.of(2021, 07, 10, 9, 00),
                    LocalDateTime.of(2021, 07, 10, 13, 05),
                132, 150, true)
                << generateFlight("mucsof3", airports['Munich'], airports['Sofia'],
                    LocalDateTime.of(2021, 07, 10, 22, 30),
                    LocalDateTime.of(2021, 07, 11, 02, 15),
                140, 180, true)
                << generateFlight("soflon1", airports['Sofia'], airports['London'],
                    LocalDateTime.of(2021, 07, 15, 10, 30),
                    LocalDateTime.of(2021, 07, 15, 13, 00),
                180, 180, false)
                << generateFlight("soflon2", airports['Sofia'], airports['London'],
                    LocalDateTime.of(2021, 07, 11, 19, 50),
                    LocalDateTime.of(2021, 07, 12, 01, 45),
                160, 160,true)
                << generateFlight("lonsof1", airports['London'], airports['Sofia'],
                    LocalDateTime.of(2021, 07, 04, 8, 30),
                    LocalDateTime.of(2021, 07, 04, 12, 05),
                80, 160, false)


        flights.groupBy { it.destination}.each {println("$it.key $it.value.id")}

        int passengersToSof = flights.findAll { it.destination == airports['Sofia']}.collect {it.passengers}.sum()
        println("Passengers to Sofia - ${passengersToSof}")

        boolean isAnyFlightUnderHalfCapacity = flights.every {(double)it.passengers / (double)it.capacity >= 0.5}
        println("Is every flight full at least 50% of its capacity? ${isAnyFlightUnderHalfCapacity ? "Yes" : "No"} ")

        // Is there a flight to London that is running late
        boolean isAnyFlightToLondonLate = flights.findAll {it.destination == airports['London'] }.any { it.runningLate}
        println("Is there a flight to London running late? ${isAnyFlightToLondonLate ? "Yes" : "No"}")
        println("Flights to London running late")
        flights.findAll {it.runningLate && it.destination == airports['London'] }
            .each {println(it.toString())}



        int passengersLate = flights.findAll { it.runningLate == true}.collect { it.passengers }.sum()
        println("Passengers running late ${passengersLate}")

        println("All flights to Sofia that are running late:")
        flights.findAll { it.destination == airports['Sofia'] && it.runningLate == true}.each { println(it)}

        println("The first flight that is running late and more than 95% of the capacity is full")
        println(flights.findAll { it.runningLate == true }
                .find { (double)it.passengers / (double)it.capacity > 0.95 }.toString())

        println("All flights from Sofia")
        flights.findAll { it.from == airports['Sofia']}.each { println(it.toString()) }

        println("Sorted by day of arrival ascending")
        flights.sort { it.arrival }
                .each { println(it.toString()) }

        println("Sorted by day of arrival descending")
        flights.sort {a, b -> b.arrival.isEqual(a.arrival) ? 0 : b.arrival.isBefore(a.arrival) ? -1 : 1}
                .each { println(it.toString())}

    }

    static Flight generateFlight(String id, String from, String destination, LocalDateTime departure,
                                 LocalDateTime arrival, int passengers, int capacity, boolean isLate) {
        return new Flight (id: id, from: from, destination: destination, departure: departure,  arrival: arrival,
                    passengers: passengers, capacity: capacity, runningLate: isLate)
    }
    /*
    static createFlights(def flights) {
        flights << new Flight(
                id: "sofmuc1", from: "SOF", destination: "MUC",
                departure: LocalDateTime.of(2021, 07, 05, 10, 45),
                arrival: LocalDateTime.of(2021, 07, 05, 15, 45),
                passengers: 140, capacity:  160, runningLate: false)

        flights << new Flight(id: "sofmuc2", from: "SOF", destination: "MUC",
                departure: LocalDateTime.of(2021, 07, 03, 16, 45),
                arrival: LocalDateTime.of(2021, 07, 03, 18, 45),
                passengers: 160, capacity:  160, runningLate: false)

        flights << new Flight(id: "sofmuc3", from: "SOF", destination: "MUC",
                departure: LocalDateTime.of(2021, 07, 10, 20, 45),
                arrival: LocalDateTime.of(2021, 07, 10, 23, 30),
                passengers: 90, capacity:  150, runningLate: false)

        flights << new Flight(id: "mucsof1", from: "MUC", destination: "SOF",
                departure: LocalDateTime.of(2021, 07, 01, 19, 15),
                arrival: LocalDateTime.of(2021, 07, 01, 21, 35),
                passengers: 113, capacity:  180, runningLate: false)

        flights << new Flight(id: "mucsof2", from: "MUC", destination: "SOF",
                departure: LocalDateTime.of(2021, 07, 10, 9, 00),
                arrival: LocalDateTime.of(2021, 07, 10, 13, 05),
                passengers: 132, capacity:  150, runningLate: true)

        flights << new Flight(id: "mucsof3", from: "MUC", destination: "SOF",
                departure: LocalDateTime.of(2021, 07, 10, 22, 30),
                arrival: LocalDateTime.of(2021, 07, 11, 02, 15),
                passengers: 140, capacity:  180, runningLate: true)

        flights << new Flight(id: "soflon1", from: "SOF", destination: "LON",
                departure: LocalDateTime.of(2021, 07, 15, 10, 30),
                arrival: LocalDateTime.of(2021, 07, 15, 13, 00),
                passengers: 180, capacity:  180, runningLate: false)
        flights << new Flight(id: "soflon2", from: "SOF", destination: "LON",
                departure: LocalDateTime.of(2021, 07, 11, 19, 50),
                arrival: LocalDateTime.of(2021, 07, 12, 01, 45),
                passengers: 160, capacity:  160, runningLate: true)
        flights << new Flight(id: "lonsof1", from: "LON", destination: "SOF",
                departure: LocalDateTime.of(2021, 07, 04, 8, 30),
                arrival: LocalDateTime.of(2021, 07, 04, 12, 05),
                passengers: 80, capacity:  160, runningLate: false)
    }*/
}
    @ToString(includeNames = true, includeFields = true)
    class Flight {
        String id
        String from
        String destination
        LocalDateTime departure
        LocalDateTime arrival
        int passengers
        int capacity
        boolean runningLate

        def printDescription() {
            println("""
---------------------------------------
Flight ${id} || ${from} - ${destination}
departure: ${departure.format(DateTimeFormatter.ofPattern("hh:mm a   dd.MMMM yyyy"))}
arrival: ${arrival.format(DateTimeFormatter.ofPattern("hh:mm a   dd.MMMM yyyy  "))}
passengers: ${passengers} capacity: ${capacity}
running late: ${runningLate ? "Yes" : "No"}
--------------------------------------""")
    }
}
