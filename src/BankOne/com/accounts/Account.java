package BankOne.com.accounts;

import BankOne.com.BankData.Customer;

import java.math.BigDecimal;

public abstract class Account {

    private BigDecimal amountOfMoney;

    private int debitIdCount;

    private String number;

    private Customer ownerOfAccount;

    void debit(BigDecimal arrivedCash) {
        setAmountOfMoney(getAmountOfMoney().add(arrivedCash));
    }

    Account(BigDecimal amountOfMoney, int debitIdCount, String number, Customer ownerOfAccount) {
        this.amountOfMoney = amountOfMoney;
        this.debitIdCount = debitIdCount;
        this.number = number;
        this.ownerOfAccount = ownerOfAccount;
    }

    BigDecimal getAmountOfMoney() {
        return amountOfMoney;
    }

    public int getDebitIdCount() {
        return debitIdCount;
    }

    public String getNumber() {
        return number;
    }

    public Customer getOwnerOfAccount() {
        return ownerOfAccount;
    }

    void setAmountOfMoney(BigDecimal amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public void setDebitIdCount(int debitIdCount) {
        this.debitIdCount = debitIdCount;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setOwnerOfAccount(Customer ownerOfAccount) {
        this.ownerOfAccount = ownerOfAccount;
    }
}
