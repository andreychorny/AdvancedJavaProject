package BankOne.com.TransactionsHistory;

import BankOne.com.accounts.Account;

import java.math.BigDecimal;

public class LocalSendTransaction extends Transaction {


    private final String toWhichAccountNumber;

    public LocalSendTransaction(int id, BigDecimal deliveredAmount,
                                Account accountOfTransaction, String toWhichAccountNumber) {
        super(id, accountOfTransaction, deliveredAmount);
        this.toWhichAccountNumber = toWhichAccountNumber;
    }

    @Override
    public String toString() {
        return "Local Send Transaction id= " + super.id + ", at date:" + super.dateOfTransaction + ":\n" +
                "AccountFrom: " + super.getAccountOfTransaction().getNumber() + ", amount of money sent: " +
                deliveredAmount + "; to Account:" + toWhichAccountNumber;
    }
}
