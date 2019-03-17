package BankOne.com.TransactionsHistory;

import BankOne.com.accounts.InternationalAccount;

import java.math.BigDecimal;
import java.util.Date;

public class InternationalOutTransaction extends Transaction {

    private InternationalAccount internationalAccountOfTransaction;

    private String toWhichAccountNumber;

    public InternationalOutTransaction(int id, BigDecimal deliveredAmount, Date dateOfTransaction,
                                       InternationalAccount internationalAccountOfTransaction,
                                       String toWhichAccountNumber) {
        super(id, deliveredAmount, dateOfTransaction);
        this.internationalAccountOfTransaction = internationalAccountOfTransaction;
        this.toWhichAccountNumber = toWhichAccountNumber;
    }
}
