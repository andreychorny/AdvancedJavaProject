package BankOne.com.services;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Customer;
import BankOne.com.accounts.Account;
import BankOne.com.accounts.InternationalAccount;
import BankOne.com.accounts.RegularAccount;
import BankOne.com.accounts.SavingAccount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


public class CustomerService {

    private static Logger logger = LogManager.getLogger(CustomerService.class);
    private Customer currentCustomer;

    public CustomerService(String login, String password) throws IllegalArgumentException {
        if (Bank.checkIfLoggingInfoIsSuitable(login, password)) {
            currentCustomer = Bank.retrievePersonByLogin(login);
            Bank.calculateInterestsOfCustomerAccs(currentCustomer);
        } else {
            throw new IllegalArgumentException("WRONG LOGGING INFO!");
        }
    }

    public String showAllAccounts() {
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
            throws IllegalArgumentException {
        RegularAccount accInUse = (RegularAccount) currentCustomer.getAccounts().get(indexOfAccount);
        amountToDeliver = amountToDeliver.setScale(2, RoundingMode.DOWN);
        logger.info("Customer id:" + currentCustomer.getId() + ", " + currentCustomer.getFirstName() + " " +
                currentCustomer.getLastName() + "\n sends funds from RegularAccount:" +
                currentCustomer.getAccounts().get(indexOfAccount).getNumber() + " to Account:" + deliverToNumber
                + "\n in amount of " + amountToDeliver.toString() + "$");
        Account accDeliverTo = Bank.findAccount(deliverToNumber);
        if (accDeliverTo != null) {
            accInUse.credit(accDeliverTo, amountToDeliver);
        } else {
            logger.warn("THERE IS NO SUCH ACCOUNT NUMBER AS " + deliverToNumber + "!!!");
            throw new IllegalArgumentException("NO SUCH ACCOUNT NUMBER");
        }
        return accInUse.getAmountOfMoney();
    }

    public BigDecimal creditFromSavingAcc(int indexOfAccount, String deliverToNumber, BigDecimal amountToDeliver)
            throws IllegalArgumentException {
        Bank.calculateInterestsOfCustomerAccs(currentCustomer);
        SavingAccount accInUse = (SavingAccount) currentCustomer.getAccounts().get(indexOfAccount);
        amountToDeliver = amountToDeliver.setScale(2, RoundingMode.DOWN);
        logger.info("Customer id:" + currentCustomer.getId() + ", " + currentCustomer.getFirstName() + " " +
                currentCustomer.getLastName() + "\n sends funds from SavingAccount:" +
                currentCustomer.getAccounts().get(indexOfAccount).getNumber() + " to Account:" + deliverToNumber
                + "\n in amount of " + amountToDeliver.toString() + "$");
        Account accDeliverTo = Bank.findAccount(deliverToNumber);
        if (accDeliverTo != null && (accDeliverTo instanceof RegularAccount)) {
            accInUse.credit(accDeliverTo, amountToDeliver);
        } else if (accDeliverTo == null) {
            logger.warn("THERE IS NO SUCH ACCOUNT NUMBER AS " + deliverToNumber + "!!!");
            throw new IllegalArgumentException("No such account number to send money!");
        } else if (!(accDeliverTo instanceof RegularAccount)) {
            logger.warn("YOU CANNOT SEND MONEY FROM SAVING ACCOUNT TO THIS TYPE OF ACCOUNTS!");
            throw new IllegalArgumentException("Wrong type of destination account!");
        }
        return accInUse.getAmountOfMoney();
    }

    public BigDecimal wireFromInternational(int indexOfAccount, String deliverToNumber, BigDecimal amountToDeliver)
            throws IllegalArgumentException {
        InternationalAccount accInUse = (InternationalAccount) currentCustomer.getAccounts().get(indexOfAccount);
        amountToDeliver = amountToDeliver.setScale(2, RoundingMode.DOWN);
        Account accDeliverTo = Bank.findAccount(deliverToNumber);
        if (accDeliverTo != null) {
            if (!(accDeliverTo instanceof InternationalAccount)) {
                throw new IllegalArgumentException("You can send money only to another international accounts");
            }
            accInUse.wire(accDeliverTo, amountToDeliver);
        } else {
            logger.warn("THERE IS NO SUCH ACCOUNT NUMBER AS " + deliverToNumber + "!!!");
            throw new IllegalArgumentException("No such account number to send money!");
        }
        return accInUse.getAmountOfMoney();
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


    public String checkHistoryOfSpecificAccount(int indexOfAccount) {
        File file = new File("src/main/resources/", "customerReportHistoryOfAccount.txt");
        String resultOutput;
        Account currentAccount = currentCustomer.getAccounts().get(indexOfAccount);
        resultOutput = "Customer: " + currentCustomer.getFirstName() + " " +
                currentCustomer.getLastName() + "\nhistory of Account:" + currentAccount.getNumber() + ":\n";
        for (int i : currentCustomer.getHistory().keySet()) {
            if (currentCustomer.getHistory().get(i).getAccountOfTransaction().equals(currentAccount)) {
                resultOutput += currentCustomer.getHistory().get(i) + "\n";
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()))) {
            bw.write(resultOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultOutput;
    }

    public void checkTransactionsPerSpecificDate(LocalDate dateFrom, LocalDate dateTo) {
        File file = new File("src/main/resources/", "customerReportHistoryBetween2Dates.txt");
        String resultOutput;
        resultOutput = "Customer: " + currentCustomer.getFirstName() + " " +
                currentCustomer.getLastName() + "\nhistory of transactions between " + dateFrom + " and " +
                dateTo + "\n";
        for (int i : currentCustomer.getHistory().keySet()) {
            LocalDate dateOfTransaction = currentCustomer.getHistory().get(i).getLocalDateOfTransaction();
            if (dateOfTransaction.compareTo(dateFrom) >= 0 && (dateOfTransaction.compareTo(dateTo) <= 0)) {
                resultOutput += currentCustomer.getHistory().get(i) + "\n";
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()))) {
            bw.write(resultOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
}
