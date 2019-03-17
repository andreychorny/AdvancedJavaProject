package BankOne.com.TransactionsHistory;

import BankOne.com.accounts.RegularAccount;

import java.math.BigDecimal;
import java.util.Date;

public class RegularOutTransaction extends Transaction {

    private RegularAccount regularAccountOfTransaction;

    private String toWhichAccountNumber;

    public RegularOutTransaction(int id, BigDecimal deliveredAmount, Date dateOfTransaction,
                                 RegularAccount regularAccountOfTransaction,
                                 String toWhichAccountNumber) {
        super(id, deliveredAmount, dateOfTransaction);
        this.regularAccountOfTransaction = regularAccountOfTransaction;
        this.toWhichAccountNumber = toWhichAccountNumber;
    }
}
