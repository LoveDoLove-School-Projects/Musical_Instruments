package entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Receipt {

    private int receiptId;
    private int userId;
    private String userName;
    private int accountId;
    private String cardHolderName;
    private String cardNumber;
    private BigDecimal amount;
    private String currency;
    private String transactionType;
    private String transactionStatus;
    private String description;
    private Timestamp transactionDate;

    public Receipt() {
    }

    public Receipt(int receiptId, int userId, String userName, int accountId, String cardHolderName, String cardNumber, BigDecimal amount, String currency, String transactionType, String transactionStatus, String description, Timestamp transactionDate) {
        this.receiptId = receiptId;
        this.userId = userId;
        this.userName = userName;
        this.accountId = accountId;
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }
}
