package BankOne.com.BankData;

import BankOne.com.TransactionsHistory.Transaction;
import BankOne.com.accounts.Account;
import BankOne.com.accounts.InternationalAccount;
import BankOne.com.accounts.RegularAccount;
import BankOne.com.accounts.SavingAccount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

public class Customer {

    private int internationalIdCount;

    private int lastTransactionsId;

    private int id;

    private static int currentLastCustomerId = 0;

    private String login;

    private char[] password;

    private String firstName;

    private String lastName;

    private List<Account> accounts = new ArrayList<>();

    private Map<Integer, Transaction> history = new TreeMap<>();

    private LocalDate dateOfBirth;

    private LocalDate dateOfJoiningToBank;

    public Customer(String login, String password, String firstName, String lastName,
                    LocalDate dateOfBirth, LocalDate dateOfJoiningToBank) {
        this.login = login;
        this.password = new char[password.length()];
        for (int i = 0; i < login.length(); i++) {
            this.password[i] = password.charAt(i);
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.dateOfJoiningToBank = dateOfJoiningToBank;
        lastTransactionsId = 0;
        internationalIdCount = 0;
        id = currentLastCustomerId;
        currentLastCustomerId += 1;
    }


    public int getLastTransactionsId() {
        return lastTransactionsId;
    }

    public Map<Integer, Transaction> getHistory() {
        return history;
    }

    public void setLastTransactionsId(int lastTransactionsId) {
        this.lastTransactionsId = lastTransactionsId;
    }

    public int getInternationalIdCount() {
        return internationalIdCount;
    }

    public void setInternationalIdCount(int internationalIdCount) {
        this.internationalIdCount = internationalIdCount;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDate getDateOfJoiningToBank() {
        return dateOfJoiningToBank;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public<A extends Account> void addAccount(A account){
        accounts.add(account);
    }

    @Override
    public String toString() {
        String accountsInfo = new String("");
        for (Account account : accounts) {
            accountsInfo = accountsInfo + accounts.indexOf(account) + "). " +
                    account.getClass().getName() + ": " + account.getNumber() + "\n";
        }
        return "Customer{" +
                "login='" + login + "\n" +
                ", firstName='" + firstName + "\n" +
                ", lastName='" + lastName + "\n" +
                ", dateOfBirth=" + dateOfBirth + "\n" +
                ", dateOfJoiningToBank=" + dateOfJoiningToBank + "\n" +
                ", accounts:" + accountsInfo +
                '}';
    }

    public int getId() {
        return id;
    }
}
