package BankOne.com.accounts;

import BankOne.com.BankData.Customer;

import java.math.BigDecimal;

public class RegularAccount extends Account {
    private int creditIdCount;

    void credit(Account toWhichAccount, BigDecimal howMuch) throws Exception {
        if (getAmountOfMoney().compareTo(howMuch) >= 0) {
            setAmountOfMoney(getAmountOfMoney().subtract(howMuch));
            toWhichAccount.debit(howMuch);
            creditIdCount++;
        } else throw new Exception("NotEnoughMoney");
    }

    public RegularAccount(BigDecimal amountOfMoney, String number,
                          Customer ownerOfAccount) {
        super(amountOfMoney, number, ownerOfAccount);
        this.creditIdCount = 0;
    }
}
