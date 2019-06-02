package BankOne.com.TransactionsHistory;

import BankOne.com.accounts.Account;

import java.math.BigDecimal;
import java.util.Date;

public class ReceiveTransaction extends Transaction {

    private final Account receiveAccount;

    private final String fromWhichAccountNumber;

    public ReceiveTransaction(int id, BigDecimal deliveredAmount,
                              Date dateOfTransaction, Account receiveAccount,
                              String fromWhichAccountNumber) {
        super(id, deliveredAmount, dateOfTransaction);
        this.receiveAccount = receiveAccount;
        this.fromWhichAccountNumber = fromWhichAccountNumber;
    }
}
