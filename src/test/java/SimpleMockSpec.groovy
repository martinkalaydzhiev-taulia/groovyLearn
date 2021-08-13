import spock.lang.Specification
import spock.lang.Unroll

class SimpleMockSpec extends Specification {
    @Unroll
    def "testing email service mock from class Flights"() {
        given:
        def emailService = Mock(EmailService)
        def flights = new Flights([FlightGenerator.generateFlight(["destination": Airports.PARIS]),
                                   FlightGenerator.generateFlight(["destination": Airports.PARIS])], emailService)

        when:
        FlightsReporter.getAllFlightsToAndSendEmail(flights, Airports.PARIS)

        then:
        1 * emailService.sendEmail() >> println("ok")
    }

    @Unroll
    def "testing email service mock on empty flight list"() {
        given:
        def emailService = Mock(EmailService)
        def flights = new Flights([], emailService)

        when:
        FlightsReporter.getAllFlightsToAndSendEmail(flights, Airports.SOFIA)

        then:
        0 * emailService.sendEmail()
    }

    @Unroll
    def "testing email service for destinations without access"() {
        given:
        def emailService = Mock(EmailService)
        def flights = new Flights([FlightGenerator.generateFlight(["destination": Airports.NEW_YORK]),
                                   FlightGenerator.generateFlight(["destination": Airports.PARIS])], emailService)

        when:
        FlightsReporter.getAllFlightsToAndSendEmail(flights, Airports.NEW_YORK)

        then:
        thrown(DeniedAccessException)
    }

    @Unroll
    def "testing email service mock from static method inside FlightsReporter"() {
        given:
        def emailService = Mock(EmailService)
        def flights = new Flights([FlightGenerator.generateFlight(["destination": Airports.PARIS]),
                                   FlightGenerator.generateFlight(["destination": Airports.PARIS])], emailService)

        when:
        FlightsReporter.getFlightsToAndSendEmail(flights, Airports.PARIS, emailService)

        then:
        1 * emailService.sendEmail()
    }

    @Unroll
    def "test static email service mock"() {
        given:
        def flights = new Flights([FlightGenerator.generateFlight(["destination": Airports.PARIS]),
                                   FlightGenerator.generateFlight(["destination": Airports.PARIS])])
        def reporter = GroovySpy(FlightsReporter, global: true)

        when:
        FlightsReporter.getFlightsToAndSendEmail(flights, Airports.PARIS)

        then:
        1 * FlightsReporter.sendEmail()
        0 * FlightsReporter.passengerAmountFrom()
    }
}
