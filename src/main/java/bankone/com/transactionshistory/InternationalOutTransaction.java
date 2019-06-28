package bankone.com.transactionshistory;

import bankone.com.accounts.Account;

import java.math.BigDecimal;

public class InternationalOutTransaction extends Transaction {

    private final String toWhichAccountNumber;

    private final String iban;

    public InternationalOutTransaction(int id, BigDecimal deliveredAmount,
                                       Account accountOfTransaction, String toWhichAccountNumber, String iban) {
        super(id, accountOfTransaction, deliveredAmount);
        this.toWhichAccountNumber = toWhichAccountNumber;
        this.iban = iban;
    }

    @Override
    public String toString() {
        return "International Out Transaction id= " + super.id + ", at date:" +
                super.getLocalDateOfTransaction() + ":\n" + "AccountFrom: " +
                super.getAccountOfTransaction().getNumber() + ", amount of money sent: " +
                super.getDeliveredAmount() + "; to Account:" + toWhichAccountNumber + "; IBAN code: " + iban;
    }
}
