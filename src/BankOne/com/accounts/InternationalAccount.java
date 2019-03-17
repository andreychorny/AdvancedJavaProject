package BankOne.com.accounts;

import BankOne.com.BankData.Customer;

import java.math.BigDecimal;

public class InternationalAccount extends Account {

    void wire(Account toWhichAccount, BigDecimal howMuch) throws Exception {
        if (getAmountOfMoney().compareTo(howMuch) >= 0) {
            setAmountOfMoney(getAmountOfMoney().subtract(howMuch));
            toWhichAccount.debit(howMuch);
        } else throw new Exception("NotEnoughMoney");
    }

    public InternationalAccount(BigDecimal amountOfMoney, int debitIdCount,
                                String number, Customer ownerOfAccount) {
        super(amountOfMoney, debitIdCount, number, ownerOfAccount);
    }
}
