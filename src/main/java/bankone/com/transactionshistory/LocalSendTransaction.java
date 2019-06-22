package bankone.com.transactionshistory;

import bankone.com.accounts.Account;

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
        return "Local Send Transaction id= " + super.id + ", at date:" + super.getDateOfTransaction() + ":\n" +
                "AccountFrom: " + super.getAccountOfTransaction().getNumber() + ", amount of money sent: " +
                super.getDeliveredAmount() + "; to Account:" + toWhichAccountNumber;
    }
}
