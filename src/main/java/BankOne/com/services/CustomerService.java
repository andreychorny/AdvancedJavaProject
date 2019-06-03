package BankOne.com.services;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Customer;
import BankOne.com.accounts.Account;
import BankOne.com.accounts.InternationalAccount;
import BankOne.com.accounts.RegularAccount;
import BankOne.com.accounts.SavingAccount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CustomerService {

    private Customer currentCustomer;

    private List<Account> accounts;

    public CustomerService(String login, char[] password) throws Exception {
        if (Bank.checkIfCustomerInfoIsSuitable(login, password)) {
            currentCustomer = Bank.retrieveCustomerByLogin(login);
            accounts = currentCustomer.getAccounts();
        }else {
            throw new Exception("WRONG LOGGING INFO!");
        }
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
        amountToDeliver = amountToDeliver.setScale(2, RoundingMode.CEILING);
        Account accDeliverTo = Bank.findAccount(deliverToNumber);
        if (accDeliverTo != null) accInUse.credit(accDeliverTo, amountToDeliver);
    }

    void creditFromSaving(int index, Scanner in) throws Exception {
        SavingAccount accInUse = (SavingAccount) accounts.get(index);
        System.out.println("Write number of account to deliver funds:");
        String deliverToNumber = in.nextLine();
        System.out.println("Amount of money to deliver: ");
        BigDecimal amountToDeliver = new BigDecimal(in.nextDouble());
        amountToDeliver = amountToDeliver.setScale(2, RoundingMode.CEILING);
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
        amountToDeliver = amountToDeliver.setScale(2, RoundingMode.CEILING);
        Account accDeliverTo = Bank.findAccount(deliverToNumber);
        if (accDeliverTo != null) accInUse.wire(accDeliverTo, amountToDeliver);
    }

    void requestForNewAccount(int numberOfAccountType) {
        switch (numberOfAccountType) {
            case 1:
                accounts.add(new RegularAccount(new BigDecimal(1000), createRandomNumber(), currentCustomer));
                break;
            case 2:
                accounts.add(new SavingAccount(new BigDecimal(1000), createRandomNumber(), currentCustomer));
                break;
            case 3:
                accounts.add(new InternationalAccount(new BigDecimal(1000), createRandomNumber(), currentCustomer));
                break;
        }
    }

    void checkHistory() {
        for (int i : currentCustomer.getHistory().keySet()) {
            System.out.println(i + ": " + currentCustomer.getHistory().get(i));
        }
    }

    private String createRandomNumber() {
        StringBuffer generatedNumber = new StringBuffer();
        for (int i = 1; i < 17; i++) {
            generatedNumber.append((int) (Math.random() * 10));
            if (i % 4 == 0 && i != 16) generatedNumber.append("-");
        }
        String result = generatedNumber.toString();
        if (!Bank.checkIfNumberUnique(result)) result = createRandomNumber();
        return result;
    }
}
