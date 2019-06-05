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

public class Customer extends Person {

    private int internationalIdCount;

    private int lastTransactionsId;

    private int id;

    private static int currentLastCustomerId = 0;


    private List<Account> accounts = new ArrayList<>();

    private Map<Integer, Transaction> history = new TreeMap<>();

    private LocalDate dateOfBirth;

    private LocalDate dateOfJoiningToBank;

    public Customer(String login, String password, String firstName, String lastName,
                    LocalDate dateOfBirth, LocalDate dateOfJoiningToBank) {
        super(login, password, firstName, lastName);
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


    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDate getDateOfJoiningToBank() {
        return dateOfJoiningToBank;
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
        return "Customer{\n" +
                "login= " + getLogin() + "\n" +
                " firstName= " + getFirstName() + ",\n" +
                " lastName= " + getLastName() + ",\n" +
                " dateOfBirth= " + dateOfBirth + ",\n" +
                " dateOfJoiningToBank= " + dateOfJoiningToBank + ",\n" +
                " accounts:" + accountsInfo +
                '}';
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return getInternationalIdCount() == customer.getInternationalIdCount() &&
                getLastTransactionsId() == customer.getLastTransactionsId() &&
                Objects.equals(getAccounts(), customer.getAccounts()) &&
                Objects.equals(getHistory(), customer.getHistory()) &&
                Objects.equals(getDateOfBirth(), customer.getDateOfBirth()) &&
                Objects.equals(getDateOfJoiningToBank(), customer.getDateOfJoiningToBank());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getInternationalIdCount(), getLastTransactionsId(), getId(), getAccounts(), getHistory(), getDateOfBirth(), getDateOfJoiningToBank());
    }
}
