package BankOne.com.accounts;

import BankOne.com.BankData.Customer;

import java.math.BigDecimal;

public class SavingAccount extends Account {
    private int creditIdCount;

    void credit(Account toWhichAccount, BigDecimal howMuch) throws Exception {
        if (getAmountOfMoney().compareTo(howMuch) >= 0) {
            setAmountOfMoney(getAmountOfMoney().subtract(howMuch));
            toWhichAccount.debit(howMuch);
            creditIdCount++;
        } else throw new Exception("NotEnoughMoney");
    }

    public SavingAccount(BigDecimal amountOfMoney, int debitIdCount, String number,
                         Customer ownerOfAccount) {
        super(amountOfMoney, debitIdCount, number, ownerOfAccount);
        this.creditIdCount = 0;
    }
}
