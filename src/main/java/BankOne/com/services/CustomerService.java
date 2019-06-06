package BankOne.com.services;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Customer;
import BankOne.com.accounts.Account;
import BankOne.com.accounts.InternationalAccount;
import BankOne.com.accounts.RegularAccount;
import BankOne.com.accounts.SavingAccount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CustomerService {

    private Customer currentCustomer;

    private List<Account> accounts;

    private static Logger logger = LogManager.getLogger(CustomerService.class);

    public CustomerService(String login, String password) throws Exception {
        if (Bank.checkIfLoggingInfoIsSuitable(login, password)) {
            currentCustomer = Bank.retrievePersonByLogin(login);
            accounts = currentCustomer.getAccounts();
        }else {
            throw new Exception("WRONG LOGGING INFO!");
        }
    }

    void showAllAccounts() throws Exception {
        for (Account account : accounts) {
            if (account instanceof RegularAccount) {
                logger.info(accounts.indexOf(account) + ": Regular:" +
                        account.getNumber() + "  balance:" + account.getAmountOfMoney());
            }
            if (account instanceof SavingAccount){
                logger.info(accounts.indexOf(account) + ": Saving:" +
                        account.getNumber() + "  balance:" + account.getAmountOfMoney());
            }
            if (account instanceof InternationalAccount){
                logger.info(accounts.indexOf(account) + ": International:" +
                        account.getNumber() + "  balance:" + account.getAmountOfMoney());

            }
        }
    }

    private void creditFromRegular(int indexOfAccount, String deliverToNumber, BigDecimal amountToDeliver)
            throws Exception {
        RegularAccount accInUse = (RegularAccount) accounts.get(indexOfAccount);
        amountToDeliver = amountToDeliver.setScale(2, RoundingMode.CEILING);
        Account accDeliverTo = Bank.findAccount(deliverToNumber);
        if (accDeliverTo != null) accInUse.credit(accDeliverTo, amountToDeliver);
    }

    private void creditFromSaving(int index, Scanner in) throws Exception {
        SavingAccount accInUse = (SavingAccount) accounts.get(index);
        System.out.println("Write number of account to deliver funds:");
        String deliverToNumber = in.nextLine();
        System.out.println("Amount of money to deliver: ");
        BigDecimal amountToDeliver = new BigDecimal(in.nextDouble());
        amountToDeliver = amountToDeliver.setScale(2, RoundingMode.CEILING);
        Account accDeliverTo = Bank.findAccount(deliverToNumber);
        if(accDeliverTo == null) {
            throw new Exception("THERE IS NO SUCH ACCOUNT(TO DELIVER FUNDS)!");
        }
        if(!(accDeliverTo instanceof  RegularAccount)) {
            throw new Exception("YOU CANNOT SEND MONEY FROM SAVING TYPE OF ACCOUNT TO THIS TYPE!");
        }
        accInUse.credit(accDeliverTo, amountToDeliver);
    }

    private void makeWire() throws Exception {
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

    public void requestForNewAccount(int numberOfAccountType) {
        switch (numberOfAccountType) {
            case 1:
                Bank.getRequestsForAccount().add(new RegularAccount(new BigDecimal(1000),
                        Bank.createRandomNumberForAcc(currentCustomer), currentCustomer));
                break;
            case 2:
                Bank.getRequestsForAccount().add(new SavingAccount(new BigDecimal(1000),
                        Bank.createRandomNumberForAcc(currentCustomer), currentCustomer));
                break;
            case 3:
                Bank.getRequestsForAccount().add(new InternationalAccount(new BigDecimal(1000),
                        Bank.createRandomNumberForAcc(currentCustomer), currentCustomer));
                break;
        }
    }

    void checkHistory() {
        for (int i : currentCustomer.getHistory().keySet()) {
            logger.info(i + ": " + currentCustomer.getHistory().get(i));
        }
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
}
