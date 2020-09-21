import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentServiceShould {
    @Test
    void throw_an_exception_when_user_does_not_exist() {
        PaymentGateway paymentGateway = new PaymentGatewayDummy();
        PaymentService paymentService = new PaymentService(paymentGateway);
        User blankUser = new UserStub(false);
        PaymentDetails paymentDetails = new PaymentDetails();

        assertThrows(NoUserException.class, () -> paymentService.processPayment(blankUser, paymentDetails));
    }

    @Test
    void sends_to_payment_gateway_if_user_exists() throws NoUserException {
        PaymentGatewayMock paymentGatewayMock = new PaymentGatewayMock();
        PaymentService paymentService = new PaymentService(paymentGatewayMock);
        User existingUser = new UserStub(true);
        PaymentDetails paymentDetails = new PaymentDetails();

        paymentGatewayMock.expectPaymentDetails(paymentDetails);
        paymentService.processPayment(existingUser, paymentDetails);

        paymentGatewayMock.verify();
    }

    private static class UserStub implements User {
        private final boolean exists;

        public UserStub(boolean exists) {
            this.exists = exists;
        }

        public boolean exists() {
            return exists;
        }
    }

    private static class PaymentGatewayDummy implements PaymentGateway {
        public void processPayment(PaymentDetails paymentDetails) {
        }
    }

    private static class PaymentGatewayMock implements PaymentGateway {
        private PaymentDetails expectedPaymentDetails;
        private PaymentDetails receivedPaymentDetails;

        public void expectPaymentDetails(PaymentDetails paymentDetails) {
            expectedPaymentDetails = paymentDetails;
        }

        public void verify() {
            assertEquals(expectedPaymentDetails, receivedPaymentDetails);
        }

        public void processPayment(PaymentDetails paymentDetails) {
            receivedPaymentDetails = paymentDetails;
        }
    }
}
