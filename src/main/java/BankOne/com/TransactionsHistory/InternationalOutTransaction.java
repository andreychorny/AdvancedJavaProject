package BankOne.com.TransactionsHistory;

import BankOne.com.accounts.Account;

import java.math.BigDecimal;

public class InternationalOutTransaction extends Transaction {

    private final String toWhichAccountNumber;

    private final String IBAN;

    public InternationalOutTransaction(int id, BigDecimal deliveredAmount,
                                       Account accountOfTransaction, String toWhichAccountNumber, String IBAN) {
        super(id, accountOfTransaction, deliveredAmount);
        this.toWhichAccountNumber = toWhichAccountNumber;
        this.IBAN = IBAN;
    }

    @Override
    public String toString() {
        return "International Out Transaction id= " + super.id + ", at date:" +
                super.getDateOfTransaction() + ":\n" + "AccountFrom: " +
                super.getAccountOfTransaction().getNumber() + ", amount of money sent: " +
                super.getDeliveredAmount() + "; to Account:" + toWhichAccountNumber + "; IBAN code: " + IBAN;
    }
}
