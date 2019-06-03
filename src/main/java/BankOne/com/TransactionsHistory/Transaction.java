package BankOne.com.TransactionsHistory;

import java.math.BigDecimal;
import java.util.Date;

public abstract class Transaction {

    protected final int id;

    protected final BigDecimal deliveredAmount;

    protected final Date dateOfTransaction;

    Transaction(int id, BigDecimal deliveredAmount, Date dateOfTransaction) {
        this.id = id;
        this.deliveredAmount = deliveredAmount;
        this.dateOfTransaction = dateOfTransaction;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getDeliveredAmount() {
        return deliveredAmount;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }
}
