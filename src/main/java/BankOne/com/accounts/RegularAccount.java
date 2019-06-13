package BankOne.com.accounts;

import BankOne.com.BankData.Customer;
import BankOne.com.TransactionsHistory.LocalSendTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RegularAccount extends Account {

    private int creditIdCount;

    public void credit(Account toWhichAccount, BigDecimal howMuch) throws IllegalArgumentException {
        if (getAmountOfMoney().compareTo(howMuch) >= 0) {
            setAmountOfMoney(getAmountOfMoney().subtract(howMuch));
            LocalDate dateOfTransaction = LocalDate.now();
            createNewMemento(dateOfTransaction);
            writeCreditToCustomerHistory(toWhichAccount,howMuch);
            creditIdCount++;
            toWhichAccount.debit(howMuch, this.getNumber());
        } else throw new IllegalArgumentException("NotEnoughMoney");
    }

    private void writeCreditToCustomerHistory(Account toWhichAccount, BigDecimal howMuch){
        getOwnerOfAccount().getHistory().put(getOwnerOfAccount().getLastTransactionsId(),
                createNewLocalSendTransaction( howMuch, toWhichAccount));
        getOwnerOfAccount().setLastTransactionsId(getOwnerOfAccount().getLastTransactionsId() + 1);
    }
    LocalSendTransaction createNewLocalSendTransaction(BigDecimal howMuch, Account toWhichAccount){
       return new LocalSendTransaction(creditIdCount, howMuch,this,
               toWhichAccount.getNumber());
    }

    public RegularAccount(BigDecimal amountOfMoney, String number,
                          Customer ownerOfAccount) {
        super(amountOfMoney, number, ownerOfAccount);
        this.creditIdCount = 0;
    }
}
