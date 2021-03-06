package bankone.com.bankdata;

import bankone.com.transactionshistory.Transaction;
import bankone.com.accounts.Account;

import java.time.LocalDate;
import java.util.*;

public class Customer extends Person {

    private static int currentLastCustomerId = 0;
    private int internationalIdCount;
    private int id;
    private Country country;

    private List<Account> accounts = new ArrayList<>();

    private Map<Integer, Transaction> history = new TreeMap<>();

    private LocalDate dateOfBirth;

    private LocalDate dateOfJoiningToBank;

    public Customer(String login, String password, String firstName, String lastName,
                    LocalDate dateOfBirth, LocalDate dateOfJoiningToBank, Country country) {
        super(login, password, firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.dateOfJoiningToBank = dateOfJoiningToBank;
        this.country = country;
        internationalIdCount = 0;
        id = currentLastCustomerId;
        currentLastCustomerId++;
    }

    public Map<Integer, Transaction> getHistory() {
        return history;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDate getDateOfJoiningToBank() {
        return dateOfJoiningToBank;
    }

    public <A extends Account> void addAccount(A account) {
        accounts.add(account);
    }

    @Override
    public String toString() {
        StringBuilder accountsInfo = new StringBuilder();
        for (Account account : accounts) {
            accountsInfo.append(accounts.indexOf(account) + "). " +
                    account.getClass().getName() + ": " + account.getNumber() + "\n");
        }
        return "Customer{\n" +
                "login= " + getLogin() + "\n" +
                " firstName= " + getFirstName() + ",\n" +
                " lastName= " + getLastName() + ",\n" +
                " dateOfBirth= " + dateOfBirth + ",\n" +
                " dateOfJoiningToBank= " + dateOfJoiningToBank + ",\n" +
                " accounts:\n" + accountsInfo +
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
                getCountry() == customer.getCountry() &&
                Objects.equals(getAccounts(), customer.getAccounts()) &&
                Objects.equals(getHistory(), customer.getHistory()) &&
                Objects.equals(getDateOfBirth(), customer.getDateOfBirth()) &&
                Objects.equals(getDateOfJoiningToBank(), customer.getDateOfJoiningToBank());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInternationalIdCount(), getCountry(), getAccounts(),
                getHistory(), getDateOfBirth(), getDateOfJoiningToBank());
    }

    public Country getCountry() {
        return country;
    }
}
