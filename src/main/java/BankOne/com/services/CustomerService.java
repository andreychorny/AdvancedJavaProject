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


    private static Logger logger = LogManager.getLogger(CustomerService.class);

    public CustomerService(String login, String password) throws Exception {
        if (Bank.checkIfLoggingInfoIsSuitable(login, password)) {
            currentCustomer = Bank.retrievePersonByLogin(login);
        } else {
            throw new Exception("WRONG LOGGING INFO!");
        }
    }

    public String showAllAccounts() throws Exception {
        String allAccountsInfo = "";
        for (Account account : currentCustomer.getAccounts()) {
            if (account instanceof RegularAccount) {
                allAccountsInfo += currentCustomer.getAccounts().indexOf(account) + ": Regular:" +
                        account.getNumber() + "  balance:" + account.getAmountOfMoney() + "\n";
            }
            if (account instanceof SavingAccount) {
                allAccountsInfo += currentCustomer.getAccounts().indexOf(account) + ": Saving:" +
                        account.getNumber() + "  balance:" + account.getAmountOfMoney() + "\n";
            }
            if (account instanceof InternationalAccount) {
                allAccountsInfo += currentCustomer.getAccounts().indexOf(account) + ": International:" +
                        account.getNumber() + "  balance:" + account.getAmountOfMoney() + "\n";
            }
        }
        logger.info(allAccountsInfo);
        return allAccountsInfo;
    }

    public BigDecimal creditFromRegularAcc(int indexOfAccount, String deliverToNumber, BigDecimal amountToDeliver)
            throws Exception {
        RegularAccount accInUse = (RegularAccount) currentCustomer.getAccounts().get(indexOfAccount);
        amountToDeliver = amountToDeliver.setScale(2, RoundingMode.DOWN);
        logger.info("Customer id:" + currentCustomer.getId() + ", " + currentCustomer.getFirstName() + " " +
                currentCustomer.getLastName() + "\n sends funds from RegularAccount:" +
                currentCustomer.getAccounts().get(indexOfAccount).getNumber() + " to Account:" + deliverToNumber
                + "\n in amount of " + amountToDeliver.toString() + "$");
        Account accDeliverTo = Bank.findAccount(deliverToNumber);
        if (accDeliverTo != null) {
            accInUse.credit(accDeliverTo, amountToDeliver);
        }else {
            logger.warn("THERE IS NO SUCH ACCOUNT NUMBER AS " + deliverToNumber + "!!!");
        }
        return accInUse.getAmountOfMoney();
    }

    public BigDecimal creditFromSavingAcc(int indexOfAccount, String deliverToNumber, BigDecimal amountToDeliver)
            throws Exception {
        SavingAccount accInUse = (SavingAccount) currentCustomer.getAccounts().get(indexOfAccount);
        amountToDeliver = amountToDeliver.setScale(2, RoundingMode.DOWN);
        logger.info("Customer id:" + currentCustomer.getId() + ", " + currentCustomer.getFirstName() + " " +
                currentCustomer.getLastName() + "\n sends funds from SavingAccount:" +
                currentCustomer.getAccounts().get(indexOfAccount).getNumber() + " to Account:" + deliverToNumber
                + "\n in amount of " + amountToDeliver.toString() + "$");
        Account accDeliverTo = Bank.findAccount(deliverToNumber);
        if (accDeliverTo != null && (accDeliverTo instanceof RegularAccount)) {
            accInUse.credit(accDeliverTo, amountToDeliver);
        }else if(accDeliverTo == null){
            logger.warn("THERE IS NO SUCH ACCOUNT NUMBER AS " + deliverToNumber + "!!!");
            throw new Exception("No such account number to send money!");
        }else if(!(accDeliverTo instanceof RegularAccount)){
            logger.warn("YOU CANNOT SEND MONEY FROM SAVING ACCOUNT TO THIS TYPE OF ACCOUNTS!");
            throw new Exception("Wrong type of destination account!");
        }
        return accInUse.getAmountOfMoney();
    }

    private void wireFromInternational(int indexOfAccount, String deliverToNumber, BigDecimal amountToDeliver)
            throws Exception {
        InternationalAccount accInUse = (InternationalAccount) currentCustomer.getAccounts().get(indexOfAccount);
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
                        Bank.createRandomNumberForAcc(currentCustomer), currentCustomer,
                        currentCustomer.getCountry().getCountryIBAN()));
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
