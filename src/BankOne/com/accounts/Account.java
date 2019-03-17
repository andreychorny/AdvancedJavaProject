package BankOne.com.accounts;

import BankOne.com.BankData.Customer;
import BankOne.com.TransactionsHistory.ReceiveTransaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class Account {

    private BigDecimal amountOfMoney;

    private int debitIdCount;

    private String number;

    private Customer ownerOfAccount;

    private List<AccountMemento> historyOfAccount = new LinkedList<>();

    void debit(BigDecimal arrivedCash) {
        setAmountOfMoney(getAmountOfMoney().add(arrivedCash));
        Date dateOfTransaction = new Date();
        createNewMemento(dateOfTransaction);
        writeDebitToHistory(dateOfTransaction, arrivedCash);
        debitIdCount++;
    }

    void createNewMemento(Date dateOfTransaction) {
        historyOfAccount.add(new AccountMemento(amountOfMoney, debitIdCount, number, dateOfTransaction));
    }

    void writeDebitToHistory(Date dateOfTransaction, BigDecimal arrivedCash) {
        ownerOfAccount.getHistory().put(ownerOfAccount.getAllTransactionsId(),
                createNewReceiveTransaction(dateOfTransaction,arrivedCash));
        ownerOfAccount.setAllTransactionsId(ownerOfAccount.getAllTransactionsId() + 1);
    }
    ReceiveTransaction createNewReceiveTransaction(Date dateOfTransaction, BigDecimal arrivedCash){
        return new ReceiveTransaction(debitIdCount, arrivedCash, dateOfTransaction,
                this, this.getNumber());
    }

    Account(BigDecimal amountOfMoney, String number, Customer ownerOfAccount) {
        this.amountOfMoney = amountOfMoney;
        this.debitIdCount = 0;
        this.number = number;
        this.ownerOfAccount = ownerOfAccount;
        historyOfAccount.add(new AccountMemento(amountOfMoney, debitIdCount, number, new Date()));
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

    public void setAmountOfMoney(BigDecimal amountOfMoney) {
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

    public List<AccountMemento> getHistoryOfAccount() {
        return historyOfAccount;
    }

    public void setHistoryOfAccount(List<AccountMemento> historyOfAccount) {
        this.historyOfAccount = historyOfAccount;
    }
}
