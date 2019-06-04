package BankOne.com.TransactionsHistory;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Transaction {

    protected final int id;

    protected final BigDecimal deliveredAmount;

    protected final LocalDate dateOfTransaction;

    Transaction(int id, BigDecimal deliveredAmount) {
        this.id = id;
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
}
