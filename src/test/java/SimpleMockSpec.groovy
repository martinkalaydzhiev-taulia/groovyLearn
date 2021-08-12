import spock.lang.Specification

class SimpleMockSpec extends Specification {
    def "testing email service mock"() {
        given:
        def emailService = Mock(EmailService)
        def flights = new Flights([FlightGenerator.generateFlight(["destination": Airports.PARIS]),
                                   FlightGenerator.generateFlight(["destination": Airports.PARIS])], emailService)

        when:
        FlightsReporter.getAllFlightsToAndSendEmail(flights, Airports.PARIS)

        then:
        1 * emailService.sentEmail()
    }

}
