package BankOne.com.accounts;

import BankOne.com.BankData.Customer;
import BankOne.com.TransactionsHistory.LocalSendTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RegularAccount extends Account {

    private int creditIdCount;

    public void credit(Account toWhichAccount, BigDecimal howMuch) throws Exception {
        if (getAmountOfMoney().compareTo(howMuch) >= 0) {
            setAmountOfMoney(getAmountOfMoney().subtract(howMuch));
            LocalDate dateOfTransaction = LocalDate.now();
            createNewMemento(dateOfTransaction);
            writeCreditToHistory(toWhichAccount,howMuch);
            creditIdCount++;
            toWhichAccount.debit(howMuch);
        } else throw new Exception("NotEnoughMoney");
    }

    void writeCreditToHistory(Account toWhichAccount, BigDecimal howMuch){
        getOwnerOfAccount().getHistory().put(getOwnerOfAccount().getLastTransactionsId(),
                createNewLocalSendTransaction( howMuch, toWhichAccount));
    }
    LocalSendTransaction createNewLocalSendTransaction(BigDecimal howMuch, Account toWhichAccount){
       return new LocalSendTransaction(creditIdCount,howMuch,this,
               toWhichAccount.getNumber());
    }

    public RegularAccount(BigDecimal amountOfMoney, String number,
                          Customer ownerOfAccount) {
        super(amountOfMoney, number, ownerOfAccount);
        this.creditIdCount = 0;
    }
}
