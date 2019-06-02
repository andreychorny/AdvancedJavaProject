package BankOne.com.TransactionsHistory;

import BankOne.com.accounts.Account;
import BankOne.com.accounts.RegularAccount;

import java.math.BigDecimal;
import java.util.Date;

public class LocalSendTransaction extends Transaction {

    private final Account accountOfTransaction;

    private final String toWhichAccountNumber;

    public LocalSendTransaction(int id, BigDecimal deliveredAmount, Date dateOfTransaction,
                                Account accountOfTransaction,
                                String toWhichAccountNumber) {
        super(id, deliveredAmount, dateOfTransaction);
        this.accountOfTransaction = accountOfTransaction;
        this.toWhichAccountNumber = toWhichAccountNumber;
    }

    @Override
    public String toString() {
        return "Local Send Transaction id= " + super.id + ", at date:" + super.dateOfTransaction + ":\n" +
                "AccountFrom: "+ accountOfTransaction.getNumber() + ", amount of money sent: " +
                deliveredAmount + "; to Account:" + toWhichAccountNumber;
    }
}
