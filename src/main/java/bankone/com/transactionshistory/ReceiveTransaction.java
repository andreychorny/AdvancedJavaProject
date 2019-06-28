package bankone.com.transactionshistory;

import bankone.com.accounts.Account;

import java.math.BigDecimal;

public class ReceiveTransaction extends Transaction {

    private final String fromWhichAccountNumber;

    public ReceiveTransaction(int id, BigDecimal deliveredAmount, Account accountOfTransaction,
                              String fromWhichAccountNumber) {
        super(id, accountOfTransaction, deliveredAmount);
        this.fromWhichAccountNumber = fromWhichAccountNumber;
    }

    @Override
    public String toString() {
        return "Receive Transaction id= " + super.id + ", at date:" + super.getLocalDateOfTransaction() + ":\n" +
                "ToAccount: " + super.getAccountOfTransaction().getNumber() + ", amount of money sent: " +
                super.getDeliveredAmount()+ "; from account:" + fromWhichAccountNumber;
    }
}
