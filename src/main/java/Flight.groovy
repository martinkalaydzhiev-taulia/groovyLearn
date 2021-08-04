import groovy.transform.ToString

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@ToString(includeNames = true, includeFields = true)
class Flight {
    String id
    Airports from
    Airports destination
    LocalDateTime departure
    LocalDateTime arrival
    int passengers
    int capacity
    boolean runningLate

    int flightLengthInMinutes() {
        ChronoUnit.MINUTES.between(departure, arrival)
    }
}
