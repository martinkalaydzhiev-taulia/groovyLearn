import java.time.LocalDateTime

class FlightGenerator {
    static void main(String[] args) {
        FlightGenerator fg = new FlightGenerator()
        1.upto(10) { println(fg.generateFlightTo(Airports.SOFIA)) }
    }

    final static int[] CAPACITIES = new int[]{80, 120, 150, 180, 240}
    final static int FLIGHT_ID = 1
    final Random rand = new Random()

    Flight generateFlight(int id = getFlightID(),
                          Airports from = getRandomAirport(rand),
                          Airports destination = getRandomAirport(from, rand),
                          LocalDateTime departure = getRandomDepartureDate(rand),
                          LocalDateTime arrival = getRandomArrivalDate(departure, rand),
                          int capacity = getRandomPlaneCapacity(rand),
                          int passengerCount = getRandomPassengerCount(capacity, rand),
                          boolean isRunningLate = getRandomIsLate(rand)) {
        new Flight(id: id, from: from, destination: destination, departure: departure, arrival: arrival,
                passengers: passengerCount, capacity: capacity, runningLate: isRunningLate)
    }

    Flight generateFlightTo(Airports destination) {
        int id = getFlightID()
        Airports from = getRandomAirport(destination, rand)
        LocalDateTime departure = getRandomDepartureDate(rand)
        LocalDateTime arrival = getRandomArrivalDate(departure, rand)
        int capacity = getRandomPlaneCapacity(rand)
        int passengerCount = getRandomPassengerCount(capacity, rand)
        boolean isRunningLate = getRandomIsLate(rand)

        generateFlight(id, from, destination, departure, arrival, capacity, passengerCount, isRunningLate)
    }

    Flight generateFlightFrom(Airports from) {
        int id = getFlightID()
        Airports destination = getRandomAirport(from, rand)
        LocalDateTime departure = getRandomDepartureDate(rand)
        LocalDateTime arrival = getRandomArrivalDate(departure, rand)
        int capacity = getRandomPlaneCapacity(rand)
        int passengerCount = getRandomPassengerCount(capacity, rand)
        boolean isRunningLate = getRandomIsLate(rand)

        generateFlight(id, from, destination, departure, arrival, capacity, passengerCount, isRunningLate)
    }

    private int getFlightID() {
        return FLIGHT_ID++
    }

    private Airports getRandomAirport(Airports known, Random rand) {
        def ports = Airports.values() - known
        return ports[rand.nextInt(ports.size())]
    }

    private Airports getRandomAirport(Random rand) {
        return Airports.values()[rand.nextInt(Airports.values().size())]
    }

    private LocalDateTime getRandomDepartureDate(Random rand) {
        int plusMonths = randomMonth(rand)
        int plusDays = randomDay(rand)
        int plusHours = randomHours(rand)
        int plusMinutes = randomMins(rand)
        LocalDateTime departure = LocalDateTime.now().plusMonths(plusMonths).plusDays(plusDays).plusHours(plusHours)
                .plusMinutes(plusMinutes).withSecond(0).withNano(0)
    }

    private LocalDateTime getRandomArrivalDate(LocalDateTime departure, Random rand) {
        int plusMonths = randomMonth(rand)
        int plusDays = randomDay(rand)
        int plusHours = randomHours(rand)
        int plusMinutes = randomMins(rand)

        departure.plusMonths(plusMonths).plusDays(plusDays).plusHours(plusHours).plusMinutes(plusMinutes)
                .withSecond(0).withNano(0)
    }

    private int randomMonth(int month = 1, Random rand) {
        int months = Math.min(Math.abs(month), 12)
        return rand.nextInt(months) + 1
    }

    private int randomDay(int day = 5, Random rand) {
        int days = Math.min(Math.abs(day), 31)
        return rand.nextInt(days)
    }

    private int randomHours(int hour = 24, Random rand) {
        int hours = Math.min(Math.abs(hour), 24)
        return rand.nextInt(hours)
    }

    private int randomMins(int minute = 60, Random rand) {
        int minutes = Math.min(Math.abs(minute), 60)
        return rand.nextInt(minutes)
    }

    private int getRandomPlaneCapacity(Random rand) {
        return CAPACITIES[rand.nextInt(CAPACITIES.size())]
    }

    private int getRandomPassengerCount(int capacity, Random rand) {
        return capacity - rand.nextInt(capacity)
    }

    private boolean getRandomIsLate(Random rand) {
        return rand.nextBoolean()
    }
}
