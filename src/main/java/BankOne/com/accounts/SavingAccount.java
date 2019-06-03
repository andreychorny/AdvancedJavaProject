package BankOne.com.accounts;

import BankOne.com.BankData.Customer;
import BankOne.com.TransactionsHistory.LocalSendTransaction;

import java.math.BigDecimal;
import java.util.Date;

public class SavingAccount extends Account {
    private int creditIdCount;

    public void credit(Account toWhichAccount, BigDecimal howMuch) throws Exception {
        if (getAmountOfMoney().compareTo(howMuch) >= 0) {
            setAmountOfMoney(getAmountOfMoney().subtract(howMuch));
            toWhichAccount.debit(howMuch);
            Date dateOfTransaction = new Date();
            createNewMemento(dateOfTransaction);
            writeCreditToHistory(dateOfTransaction,toWhichAccount,howMuch);
            creditIdCount++;
        } else throw new Exception("NotEnoughMoney");
    }
    void writeCreditToHistory(Date dateOfTransaction, Account toWhichAccount, BigDecimal howMuch){
        getOwnerOfAccount().getHistory().put(getOwnerOfAccount().getLastTransactionsId(),
                createNewLocalOutTransaction(dateOfTransaction, howMuch, toWhichAccount));
    }
    LocalSendTransaction createNewLocalOutTransaction(Date dateOfTransaction, BigDecimal howMuch, Account toWhichAccount){
        return new LocalSendTransaction(creditIdCount,howMuch,dateOfTransaction,this,
                toWhichAccount.getNumber());
    }

    public SavingAccount(BigDecimal amountOfMoney, String number,
                         Customer ownerOfAccount) {
        super(amountOfMoney, number, ownerOfAccount);
        this.creditIdCount = 0;
    }

}
