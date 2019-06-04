package BankOne.com.TransactionsHistory;

import BankOne.com.accounts.InternationalAccount;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InternationalOutTransaction extends Transaction {

    private final InternationalAccount internationalAccountOfTransaction;

    private final String toWhichAccountNumber;

    public InternationalOutTransaction(int id, BigDecimal deliveredAmount,
                                       InternationalAccount internationalAccountOfTransaction,
                                       String toWhichAccountNumber) {
        super(id, deliveredAmount);
        this.internationalAccountOfTransaction = internationalAccountOfTransaction;
        this.toWhichAccountNumber = toWhichAccountNumber;
    }

    @Override
    public String toString() {
        return "International Out Transaction id= " + super.id + ", at date:" +
                super.dateOfTransaction + ":\n" +"AccountFrom: "+
                internationalAccountOfTransaction.getNumber() + ", amount of money sent: " +
                deliveredAmount + "; to Account:" + toWhichAccountNumber;
    }
}
