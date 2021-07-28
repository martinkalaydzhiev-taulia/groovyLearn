import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FlightsAndPorts {
    static void main(String[] args) {
        def flights = []
        createFlights(flights)
        def passengersToSof = flights.each { it.destination == "SOF"}.collect {it.passengers}.sum()
        println("Passengers to Sofia - ${passengersToSof}")

        def cap = flights.every {(double)it.passengers / (double)it.capacity >= 0.5}
        println("Is every flight full at least 50% of its capacity? ${cap ? "Yes" : "No"} ")

        // Is there a flight to London that is running late
        def londonLate = flights.findAll {it.destination == "LON" }.any { it.runningLate}
        println("Is there a flight to London running late? ${londonLate ? "Yes" : "No"}")


        def pas = flights.findAll { it.runningLate == true}.collect { it.passengers }.sum()
        println("Passengers running late ${pas}")

        println("All flights to Sofia that are running late:")
        flights.findAll { it.destination == "SOF" && it.runningLate == true}.each { it.description() }

        println("The first flight that is running late and more than 95% of the capacity is full")
        flights.findAll { it.runningLate == true }
                .find { (double)it.passengers / (double)it.capacity > 0.95 }
                .each { it.description() }

        println("All flights from Sofia")
        flights.findAll { it.startingLocation == "SOF"}.each { it.description() }

        println("Sorted by day of arrival ascending")
        flights.sort {a, b -> a.arrivalAt.getDayOfMonth() - b.arrivalAt.getDayOfMonth()}.each {it.description()}

        println("Sorted by day of arrival descending")
        flights.sort {a, b -> b.arrivalAt.getDayOfMonth() - a.arrivalAt.getDayOfMonth()}.each {it.description()}

    }

    static createFlights(def flights) {
        flights << new Flight(
                id: "sofmuc1", startingLocation: "SOF", destination: "MUC",
                from: LocalDateTime.of(2021, 07, 05, 10, 45),
                arrivalAt: LocalDateTime.of(2021, 07, 05, 15, 45),
                passengers: 140, capacity:  160, runningLate: false)

        flights << new Flight(id: "sofmuc2", startingLocation: "SOF", destination: "MUC",
                from: LocalDateTime.of(2021, 07, 03, 16, 45),
                arrivalAt: LocalDateTime.of(2021, 07, 03, 18, 45),
                passengers: 160, capacity:  160, runningLate: false)

        flights << new Flight(id: "sofmuc3", startingLocation: "SOF", destination: "MUC",
                from: LocalDateTime.of(2021, 07, 10, 20, 45),
                arrivalAt: LocalDateTime.of(2021, 07, 10, 23, 30),
                passengers: 90, capacity:  150, runningLate: false)

        flights << new Flight(id: "mucsof1", startingLocation: "MUC", destination: "SOF",
                from: LocalDateTime.of(2021, 07, 01, 19, 15),
                arrivalAt: LocalDateTime.of(2021, 07, 01, 21, 35),
                passengers: 113, capacity:  180, runningLate: false)

        flights << new Flight(id: "mucsof2", startingLocation: "MUC", destination: "SOF",
                from: LocalDateTime.of(2021, 07, 10, 9, 00),
                arrivalAt: LocalDateTime.of(2021, 07, 10, 13, 05),
                passengers: 132, capacity:  150, runningLate: true)

        flights << new Flight(id: "mucsof3", startingLocation: "SOF", destination: "MUC",
                from: LocalDateTime.of(2021, 07, 10, 22, 30),
                arrivalAt: LocalDateTime.of(2021, 07, 11, 02, 15),
                passengers: 140, capacity:  180, runningLate: true)

        flights << new Flight(id: "soflon1", startingLocation: "SOF", destination: "LON",
                from: LocalDateTime.of(2021, 07, 15, 10, 30),
                arrivalAt: LocalDateTime.of(2021, 07, 15, 13, 00),
                passengers: 180, capacity:  180, runningLate: false)
        flights << new Flight(id: "soflon2", startingLocation: "SOF", destination: "LON",
                from: LocalDateTime.of(2021, 07, 11, 19, 50),
                arrivalAt: LocalDateTime.of(2021, 07, 12, 01, 45),
                passengers: 160, capacity:  160, runningLate: true)
        flights << new Flight(id: "lonsof1", startingLocation: "LON", destination: "SOF",
                from: LocalDateTime.of(2021, 07, 04, 8, 30),
                arrivalAt: LocalDateTime.of(2021, 07, 04, 12, 05),
                passengers: 80, capacity:  160, runningLate: false)
    }
}

class Flight2 {
    def id
    def startingLocation
    def destination
    def from
    def arrivalAt
    def passengers
    def capacity
    def runningLate

    def description() {
        println("""
---------------------------------------
Flight ${id} || ${startingLocation} - ${destination}
departure: ${from.format(DateTimeFormatter.ofPattern("hh:mm a   dd.MMMM yyyy"))}
arrival: ${arrivalAt.format(DateTimeFormatter.ofPattern("hh:mm a   dd.MMMM yyyy  "))}
passengers: ${passengers} capacity: ${capacity}
running late: ${runningLate ? "Yes" : "No"}
--------------------------------------""")
    }
}

class Port {
    def location
    def flights
    def capacity
}
