package BankOne.com.TransactionsHistory;

import BankOne.com.accounts.Account;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReceiveTransaction extends Transaction {

    private final String fromWhichAccountNumber;

    public ReceiveTransaction(int id, BigDecimal deliveredAmount, Account accountOfTransaction,
                              String fromWhichAccountNumber) {
        super(id, accountOfTransaction, deliveredAmount);
        this.fromWhichAccountNumber = fromWhichAccountNumber;
    }

    @Override
    public String toString() {
        return "Receive Transaction id= " + super.id + ", at date:" + super.dateOfTransaction + ":\n" +
                "ToAccount: "+ super.getAccountOfTransaction().getNumber()  + ", amount of money sent: " +
                deliveredAmount + "; from account:" + fromWhichAccountNumber;
    }
}
