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
        1 * emailService.sentEmail() >> println("ok")

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
        1 * emailService.sentEmail()
    }
}
