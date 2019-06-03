package BankOne.com.BankData;

import BankOne.com.TransactionsHistory.Transaction;
import BankOne.com.accounts.Account;
import BankOne.com.accounts.InternationalAccount;
import BankOne.com.accounts.RegularAccount;
import BankOne.com.accounts.SavingAccount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Customer {

    private int internationalIdCount;

    private int lastTransactionsId;

    private String login;

    private char[] password;

    private String firstName;

    private String lastName;

    private List<Account> accounts = new ArrayList<>();

    private Map<Integer, Transaction> history = new TreeMap<>();

    private Date dateOfBirth;

    private Date dateOfJoiningToBank;


    public Customer(String login, String password, String firstName, String lastName,
                    Date dateOfBirth, Date dateOfJoiningToBank) {
        this.login = login;
        this.password = new char[password.length()];
        for(int i=0; i<login.length();i++){
            this.password[i] = password.charAt(i);
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.dateOfJoiningToBank = dateOfJoiningToBank;
        lastTransactionsId = 0;
        internationalIdCount = 0;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Date getDateOfJoiningToBank() {
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

    @Override
    public String toString() {
        String accountsInfo = new String("");
        for(Account account : accounts){
                accountsInfo = accountsInfo + accounts.indexOf(account) + "). "+
                        account.getClass().getName() +": "+ account.getNumber() +"\n";
        }
        return "Customer{" +
                "login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", dateOfJoiningToBank=" + dateOfJoiningToBank +
                ", accounts:" + accountsInfo +
                '}';
    }
}