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

    private String firstName;

    private String lastName;

    private List<Account> accounts = new ArrayList<>();

    private Map<Integer, Transaction> history = new TreeMap<>();

    private Date dateOfBirth;

    private Date dateOfJoiningToBank;

    void chooseOperation() {

    }

    void makeCredit() throws Exception {
        System.out.println("You have such Acceptable accounts:");
        for (Account account : accounts) {
            if (account instanceof RegularAccount) System.out.println(accounts.indexOf(account) + ": Regular:" +
                    account.getNumber());
            if (account instanceof SavingAccount) System.out.println(accounts.indexOf(account) + ": Saving:" +
                    account.getNumber());
        }
        System.out.println("Please choose the index of account for credit:");
        Scanner in = new Scanner(System.in);
        int index = in.nextInt();
        if (accounts.get(index) instanceof RegularAccount) {
            creditFromRegular(index, in);
        }
        if (accounts.get(index) instanceof SavingAccount) {
            creditFromSaving(index, in);
        }

    }

    void creditFromRegular(int index, Scanner in) throws Exception {
        RegularAccount accInUse = (RegularAccount) accounts.get(index);
        in.nextLine();
        System.out.println("Write number of account to deliver funds:");
        String deliverToNumber = in.nextLine();
        System.out.println("Amount of money to deliver: ");
        BigDecimal amountToDeliver = new BigDecimal(in.nextDouble());
        amountToDeliver.setScale(2, RoundingMode.CEILING);
        Account accDeliverTo = Bank.findAccount(deliverToNumber);
        if (accDeliverTo != null) accInUse.credit(accDeliverTo, amountToDeliver);
    }

    void creditFromSaving(int index, Scanner in) throws Exception {
        SavingAccount accInUse = (SavingAccount) accounts.get(index);
        System.out.println("Write number of account to deliver funds:");
        String deliverToNumber = in.nextLine();
        System.out.println("Amount of money to deliver: ");
        BigDecimal amountToDeliver = new BigDecimal(in.nextDouble());
        amountToDeliver.setScale(2, RoundingMode.CEILING);
        Account accDeliverTo = Bank.findAccount(deliverToNumber);
        if (accDeliverTo != null) accInUse.credit(accDeliverTo, amountToDeliver);
    }

    void makeWire() throws Exception {
        List<Integer> acceptableAccountsIds = new LinkedList<>();
        System.out.println("You have such Acceptable accounts:");
        for (Account account : accounts) {
            if (account instanceof InternationalAccount)
                System.out.println(accounts.indexOf(account) + ": International:" +
                        account.getNumber());
        }
        System.out.println("Please choose the index of account for wire:");
        Scanner in = new Scanner(System.in);
        int index = in.nextInt();
        InternationalAccount accInUse = (InternationalAccount) accounts.get(index);
        System.out.println("Write number of account to deliver funds:");
        String deliverToNumber = in.nextLine();
        System.out.println("Amount of money to deliver: ");
        BigDecimal amountToDeliver = new BigDecimal(in.nextDouble());
        amountToDeliver.setScale(2, RoundingMode.CEILING);
        Account accDeliverTo = Bank.findAccount(deliverToNumber);
        if (accDeliverTo != null) accInUse.wire(accDeliverTo, amountToDeliver);
    }

    void createNewAccount() {
        System.out.println("Write 1 for creating Regular account, 2 for Saving and 3 for international.");
        System.out.println("Anything else for exit");
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        switch (n) {
            case 1:
                accounts.add(new RegularAccount(new BigDecimal(1000), createRandomNumber(), this));
                break;
            case 2:
                accounts.add(new SavingAccount(new BigDecimal(1000), createRandomNumber(), this));
                break;
            case 3:
                accounts.add(new InternationalAccount(new BigDecimal(1000), createRandomNumber(), this));
                break;
            default:
                chooseOperation();
        }
    }

    String createRandomNumber() {
        StringBuffer generatedNumber = new StringBuffer("");
        for (int i = 1; i < 17; i++) {
            generatedNumber.append((int) (Math.random() * 10));
            if(i%4==0 && i!=16) generatedNumber.append("-");
        }
        String result = generatedNumber.toString();
        if(!Bank.checkIfNumberUnique(result)) result = createRandomNumber();
        return result;
    }

    public Customer(String firstName, String lastName, Date dateOfBirth, Date dateOfJoiningToBank) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.dateOfJoiningToBank = dateOfJoiningToBank;
        lastTransactionsId = 0;
        internationalIdCount = 0;
    }

    void checkHistory() {
        for (int i : history.keySet()) {
            System.out.println(i + ": " + history.get(i));
        }
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
}
