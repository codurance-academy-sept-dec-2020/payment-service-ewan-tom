public class PaymentService {
    private PaymentGateway paymentGateway;

    public PaymentService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public void processPayment(User user, PaymentDetails paymentDetails) throws NoUserException {
        if (!user.exists()) {
            throw new NoUserException();
        }

        paymentGateway.processPayment(paymentDetails);
    }
}
