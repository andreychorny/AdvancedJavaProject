package BankOne.com.BankData;

import BankOne.com.TransactionsHistory.Transaction;
import BankOne.com.accounts.Account;

import java.util.*;

public class Customer {

    private int internationalIdCount;

    private int allTransactionsId;

    private String firstName;

    private String lastName;

    private List<Account> accounts = new ArrayList<>();

    private Map<Integer, Transaction> history = new TreeMap<>();

    private Date dateOfBirth;

    private Date dateOfJoiningToBank;

    void chooseOperation() {

    }

    void makeCredit() {

    }

    void makeWire() {

    }

    void createNewAccount() {

    }

    void checkHistory() {

    }

    public int getAllTransactionsId() {
        return allTransactionsId;
    }

    public Map<Integer, Transaction> getHistory() {
        return history;
    }

    public void setAllTransactionsId(int allTransactionsId) {
        this.allTransactionsId = allTransactionsId;
    }
}
