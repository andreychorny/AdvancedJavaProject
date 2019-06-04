package BankOne.com.TransactionsHistory;

import BankOne.com.accounts.Account;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReceiveTransaction extends Transaction {

    private final Account receiveAccount;

    private final String fromWhichAccountNumber;

    public ReceiveTransaction(int id, BigDecimal deliveredAmount,
                              LocalDate dateOfTransaction, Account receiveAccount,
                              String fromWhichAccountNumber) {
        super(id, deliveredAmount);
        this.receiveAccount = receiveAccount;
        this.fromWhichAccountNumber = fromWhichAccountNumber;
    }

    @Override
    public String toString() {
        return "Receive Transaction id= " + super.id + ", at date:" + super.dateOfTransaction + ":\n" +
                "AccountFrom: "+ fromWhichAccountNumber +
                ", amount of money sent: " + deliveredAmount +
                "; to Account:" + receiveAccount.getNumber();
    }
}
