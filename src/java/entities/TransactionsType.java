package entities;

public interface TransactionsType {

    final int PAGE_TIMEOUT = 30000;

    enum PaymentType {
        CreditOrDebitCard("CreditOrDebitCard"),
        Paypal("Paypal"),
        CashOnDelivery("CashOnDelivery");
        private final String type;

        private PaymentType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
