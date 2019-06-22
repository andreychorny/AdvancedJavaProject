package bankone.com.accounts;

import bankone.com.bankdata.Customer;
import bankone.com.transactionshistory.LocalSendTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RegularAccount extends Account {

    private int creditIdCount;

    public RegularAccount(BigDecimal amountOfMoney, String number,
                          Customer ownerOfAccount) {
        super(amountOfMoney, number, ownerOfAccount);
        this.creditIdCount = 0;
    }

    public void credit(Account toWhichAccount, BigDecimal howMuch) throws IllegalArgumentException {
        if (getAmountOfMoney().compareTo(howMuch) >= 0) {
            setAmountOfMoney(getAmountOfMoney().subtract(howMuch));
            LocalDate dateOfTransaction = LocalDate.now();
            createNewMemento(dateOfTransaction);
            writeCreditToCustomerHistory(toWhichAccount, howMuch);
            creditIdCount++;
            toWhichAccount.debit(howMuch, this.getNumber());
        } else throw new IllegalArgumentException("NotEnoughMoney");
    }

    private void writeCreditToCustomerHistory(Account toWhichAccount, BigDecimal howMuch) {
        int amountOfTransactionsOfCustomer = getOwnerOfAccount().getHistory().size();
        getOwnerOfAccount().getHistory().put(amountOfTransactionsOfCustomer,
                createNewLocalSendTransaction(howMuch, toWhichAccount));
    }

    LocalSendTransaction createNewLocalSendTransaction(BigDecimal howMuch, Account toWhichAccount) {
        return new LocalSendTransaction(creditIdCount, howMuch, this,
                toWhichAccount.getNumber());
    }
}
