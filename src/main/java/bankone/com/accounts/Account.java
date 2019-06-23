package bankone.com.accounts;

import bankone.com.bankdata.Customer;
import bankone.com.transactionshistory.ReceiveTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public abstract class Account {

    private BigDecimal amountOfMoney;

    private int debitIdCount;

    private String number;

    private Customer ownerOfAccount;

    private List<AccountMemento> historyOfAccount = new LinkedList<>();

    Account(BigDecimal amountOfMoney, String number, Customer ownerOfAccount) {
        this.amountOfMoney = amountOfMoney;
        this.debitIdCount = 0;
        this.number = number;
        this.ownerOfAccount = ownerOfAccount;
        historyOfAccount.add(new AccountMemento(amountOfMoney, debitIdCount, number, LocalDate.now()));
    }

    void debit(BigDecimal arrivedCash, String numberFromWhichAccount) {
        setAmountOfMoney(getAmountOfMoney().add(arrivedCash));
        LocalDate dateOfTransaction = LocalDate.now();
        createNewMemento(dateOfTransaction);
        writeDebitToCustomerHistory(dateOfTransaction, arrivedCash, numberFromWhichAccount);
        debitIdCount++;
    }

    void createNewMemento(LocalDate dateOfTransaction) {
        historyOfAccount.add(new AccountMemento(amountOfMoney, debitIdCount, number, dateOfTransaction));
    }

    void writeDebitToCustomerHistory(LocalDate dateOfTransaction, BigDecimal arrivedCash, String numberFromWhichAcc) {
        int amountOfTransactionsOfCustomer = getOwnerOfAccount().getHistory().size();
        getOwnerOfAccount().getHistory().put(amountOfTransactionsOfCustomer,
                createNewReceiveTransaction(arrivedCash, numberFromWhichAcc));
    }

    private ReceiveTransaction createNewReceiveTransaction(BigDecimal arrivedCash, String numberFromWhichAcc) {
        return new ReceiveTransaction(debitIdCount, arrivedCash, this, numberFromWhichAcc);
    }

    public BigDecimal getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(BigDecimal amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Customer getOwnerOfAccount() {
        return ownerOfAccount;
    }

    public List<AccountMemento> getHistoryOfAccount() {
        return historyOfAccount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "number=" + number +
                ", amountOfMoney='" + amountOfMoney + '\'' +
                ", ownerOfAccount=" + ownerOfAccount +
                '}';
    }
}
