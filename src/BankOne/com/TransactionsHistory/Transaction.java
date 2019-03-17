package BankOne.com.TransactionsHistory;

import java.math.BigDecimal;
import java.util.Date;

public abstract class Transaction {

    private final int id;

    private final BigDecimal deliveredAmount;

    private final Date dateOfTransaction;

    Transaction(int id, BigDecimal deliveredAmount, Date dateOfTransaction) {
        this.id = id;
        this.deliveredAmount = deliveredAmount;
        this.dateOfTransaction = dateOfTransaction;
    }
}
