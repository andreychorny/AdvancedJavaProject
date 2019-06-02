package BankOne.com.accounts;

import BankOne.com.BankData.Customer;
import BankOne.com.TransactionsHistory.InternationalOutTransaction;

import java.math.BigDecimal;
import java.util.Date;

public class InternationalAccount extends Account {

    public void wire(Account toWhichAccount, BigDecimal howMuch) throws Exception {
        if (getAmountOfMoney().compareTo(howMuch) >= 0) {
            setAmountOfMoney(getAmountOfMoney().subtract(howMuch));
            Date dateOfTransaction = new Date();
            createNewMemento(dateOfTransaction);
            writeWireToHistory(dateOfTransaction, howMuch, toWhichAccount);
            toWhichAccount.debit(howMuch);
        } else throw new Exception("NotEnoughMoney");
    }

    void writeWireToHistory(Date dateOfTransaction, BigDecimal howMuch, Account toWhichAccount) {
        getOwnerOfAccount().getHistory().put(getOwnerOfAccount().getLastTransactionsId(),
                createNewInternationalOutTransaction(dateOfTransaction, howMuch, toWhichAccount));
        getOwnerOfAccount().setInternationalIdCount(getOwnerOfAccount().getInternationalIdCount() + 1);
        getOwnerOfAccount().setLastTransactionsId(getOwnerOfAccount().getLastTransactionsId() + 1);
    }

    InternationalOutTransaction createNewInternationalOutTransaction(Date dateOfTransaction,
                                                                     BigDecimal howMuch, Account toWhichAccount) {
        return new InternationalOutTransaction(getOwnerOfAccount().getInternationalIdCount(),
                howMuch, dateOfTransaction, this,
                toWhichAccount.getNumber());
    }

    public InternationalAccount(BigDecimal amountOfMoney,
                                String number, Customer ownerOfAccount) {
        super(amountOfMoney, number, ownerOfAccount);
    }
}
