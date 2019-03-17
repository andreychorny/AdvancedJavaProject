package BankOne.com.TransactionsHistory;

import BankOne.com.accounts.Account;
import BankOne.com.accounts.RegularAccount;

import java.math.BigDecimal;
import java.util.Date;

public class LocalOutTransaction extends Transaction {

    private final Account accountOfTransaction;

    private final String toWhichAccountNumber;

    public LocalOutTransaction(int id, BigDecimal deliveredAmount, Date dateOfTransaction,
                               Account accountOfTransaction,
                               String toWhichAccountNumber) {
        super(id, deliveredAmount, dateOfTransaction);
        this.accountOfTransaction = accountOfTransaction;
        this.toWhichAccountNumber = toWhichAccountNumber;
    }
}
