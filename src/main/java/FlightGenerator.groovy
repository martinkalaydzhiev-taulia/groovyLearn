import java.time.LocalDateTime

class FlightGenerator {
    final static int[] CAPACITIES = new int[]{80, 120, 150, 180, 240}

    static Flight generateFlight() {
        Random r = new Random()
        int numberOfAirports = Airports.getEnumConstants().length
        int airportFromNumber = r.nextInt(numberOfAirports)
        Airports from = Airports.getEnumConstants()[airportFromNumber]
        Airports destination = Airports.getEnumConstants()[(airportFromNumber + 1 + r.nextInt(numberOfAirports - 2)) % numberOfAirports]
        String id = from.toString() + destination.toString() + r.nextInt(100)
        int capacity = CAPACITIES[r.nextInt(CAPACITIES.length)]
        BigDecimal passengers = capacity - r.nextInt(capacity) / 3
        boolean isLate = r.nextBoolean()

        LocalDateTime departure = LocalDateTime.now().plusDays(r.nextInt(30)).plusHours(r.nextInt(24))
                .plusMinutes(r.nextInt(60)).withSecond(0).withNano(0)
        LocalDateTime arrival = departure.plusDays(r.nextInt(2)).plusHours(r.nextInt(24)).plusMinutes(r.nextInt(60))
                .withSecond(0).withNano(0)

        return new Flight(id: id, from: from, destination: destination, departure: departure, arrival: arrival,
                passengers: passengers, capacity: capacity, runningLate: isLate)
    }
}
