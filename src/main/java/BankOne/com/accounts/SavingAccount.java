package BankOne.com.accounts;

import BankOne.com.BankData.Customer;
import BankOne.com.TransactionsHistory.LocalSendTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingAccount extends Account {

    private int creditIdCount;

    public void credit(Account toWhichAccount, BigDecimal howMuch) throws Exception {
        if (getAmountOfMoney().compareTo(howMuch) >= 0) {
            setAmountOfMoney(getAmountOfMoney().subtract(howMuch));
            LocalDate dateOfTransaction = LocalDate.now();
            createNewMemento(dateOfTransaction);
            writeCreditToHistory(toWhichAccount,howMuch);
            toWhichAccount.debit(howMuch, this.getNumber());
            creditIdCount++;
        } else throw new Exception("NotEnoughMoney");
    }
    void writeCreditToHistory(Account toWhichAccount, BigDecimal howMuch){
        getOwnerOfAccount().getHistory().put(getOwnerOfAccount().getLastTransactionsId(),
                createNewLocalOutTransaction(howMuch, toWhichAccount));
        getOwnerOfAccount().setLastTransactionsId(getOwnerOfAccount().getLastTransactionsId() + 1);
    }
    LocalSendTransaction createNewLocalOutTransaction(BigDecimal howMuch, Account toWhichAccount){
        return new LocalSendTransaction(creditIdCount,howMuch,this,
                toWhichAccount.getNumber());
    }

    public SavingAccount(BigDecimal amountOfMoney, String number,
                         Customer ownerOfAccount) {
        super(amountOfMoney, number, ownerOfAccount);
        this.creditIdCount = 0;
    }

}
