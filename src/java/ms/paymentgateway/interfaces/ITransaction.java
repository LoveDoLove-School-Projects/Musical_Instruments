package ms.paymentgateway.interfaces;

public interface ITransaction {

    int PAGE_TIMEOUT = 30000;

    enum TransactionType {
        DEPOSIT(1),
        WITHDRAW(2),
        TRANSFER(3),
        PAYMENT(4),
        REFUND(5);
        public final int code;

        TransactionType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    enum TransactionStatus {
        NOT_DONE(0),
        APPROVED(1),
        DECLINED(2),
        CANCELLED(3),
        USER_CANCELLED(4);
        public final int code;

        TransactionStatus(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
