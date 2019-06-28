package bankone.com.transactionshistory;

import bankone.com.accounts.Account;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Transaction {

    protected final int id;

    private final Account accountOfTransaction;

    private final BigDecimal deliveredAmount;

    private LocalDate dateOfTransaction;

    public Transaction(int id, Account accountOfTransaction, BigDecimal deliveredAmount) {
        this.id = id;
        this.accountOfTransaction = accountOfTransaction;
        this.deliveredAmount = deliveredAmount;
        this.dateOfTransaction = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public BigDecimal getDeliveredAmount() {
        return deliveredAmount;
    }

    public LocalDate getLocalDateOfTransaction() {
        return dateOfTransaction;
    }

    public Account getAccountOfTransaction() {
        return accountOfTransaction;
    }

    public void setDateOfTransaction(LocalDate dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

}
