package BankOne.com.TransactionsHistory;

import BankOne.com.accounts.Account;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Transaction {

    protected final int id;

    protected final Account accountOfTransaction;

    protected final BigDecimal deliveredAmount;

    protected final LocalDate dateOfTransaction;

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


}
