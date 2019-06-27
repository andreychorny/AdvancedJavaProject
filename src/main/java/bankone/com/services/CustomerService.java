package bankone.com.services;

import bankone.com.accounts.*;
import bankone.com.bankdata.Bank;
import bankone.com.bankdata.BankUtil;
import bankone.com.bankdata.Customer;
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
    private Bank bank;
    private static final String NO_SUCH_ACCOUNT = "THERE IS NO SUCH ACCOUNT NUMBER AS ";
    private static final String PARRENT_LOCATION = "src/main/resources/";

    public CustomerService(String login, String password) throws IllegalArgumentException {
        if (BankUtil.checkIfLoggingInfoIsSuitable(login, password)) {
            bank = Bank.getInstance();
            currentCustomer = bank.retrievePersonByLogin(login);
            bank.calculateInterestsOfCustomerAccs(currentCustomer);
        } else {
            logger.warn("You put wrong logging info. check your password and login again");
            throw new IllegalArgumentException("WRONG LOGGING INFO!");
        }
    }

    public String showAllAccounts() {
        StringBuilder allAccountsInfo = new StringBuilder();
        final String BALANCE = "  balance:";
        for (Account account : currentCustomer.getAccounts()) {
            if (account instanceof RegularAccount) {
                allAccountsInfo.append(currentCustomer.getAccounts().indexOf(account) + ": Regular:" +
                        account.getNumber() + BALANCE + account.getAmountOfMoney() + "\n");
            }
            if (account instanceof SavingAccount) {
                allAccountsInfo.append(currentCustomer.getAccounts().indexOf(account) + ": Saving:" +
                        account.getNumber() + BALANCE + account.getAmountOfMoney() + "\n");
            }
            if (account instanceof InternationalAccount) {
                allAccountsInfo.append(currentCustomer.getAccounts().indexOf(account) + ": International:" +
                        account.getNumber() + BALANCE + account.getAmountOfMoney() + "\n");
            }
        }
        logger.info(allAccountsInfo.toString());
        return allAccountsInfo.toString();
    }

    public BigDecimal creditFromRegularAcc(int indexOfAccount, String deliverToNumber, BigDecimal amountToDeliver)
            throws IllegalArgumentException {
        RegularAccount accInUse = (RegularAccount) currentCustomer.getAccounts().get(indexOfAccount);
        amountToDeliver = amountToDeliver.setScale(2, RoundingMode.DOWN);
        logger.info("Customer id:" + currentCustomer.getId() + ", " + currentCustomer.getFirstName() + " " +
                currentCustomer.getLastName() + "\n sends funds from RegularAccount:" +
                currentCustomer.getAccounts().get(indexOfAccount).getNumber() + " to Account:" + deliverToNumber
                + "\n in amount of " + amountToDeliver.toString() + "$");
        Account accDeliverTo = bank.findAccount(deliverToNumber);
        if (accDeliverTo != null) {
            accInUse.credit(accDeliverTo, amountToDeliver);
        } else {
            logger.warn(NO_SUCH_ACCOUNT + deliverToNumber + "!!!");
            throw new IllegalArgumentException("NO SUCH ACCOUNT NUMBER");
        }
        return accInUse.getAmountOfMoney();
    }

    public BigDecimal creditFromSavingAcc(int indexOfAccount, String deliverToNumber, BigDecimal amountToDeliver)
            throws IllegalArgumentException {
        bank.calculateInterestsOfCustomerAccs(currentCustomer);
        SavingAccount accInUse = (SavingAccount) currentCustomer.getAccounts().get(indexOfAccount);
        amountToDeliver = amountToDeliver.setScale(2, RoundingMode.DOWN);
        logger.info("Customer id:" + currentCustomer.getId() + ", " + currentCustomer.getFirstName() + " " +
                currentCustomer.getLastName() + "\n sends funds from SavingAccount:" +
                currentCustomer.getAccounts().get(indexOfAccount).getNumber() + " to Account:" + deliverToNumber
                + "\n in amount of " + amountToDeliver.toString() + "$");
        Account accDeliverTo = bank.findAccount(deliverToNumber);
        if (accDeliverTo instanceof RegularAccount) {
            accInUse.credit(accDeliverTo, amountToDeliver);
        } else if (accDeliverTo == null) {
            logger.warn(NO_SUCH_ACCOUNT + deliverToNumber + "!!!");
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
        Account accDeliverTo = bank.findAccount(deliverToNumber);
        if (accDeliverTo != null) {
            if (!(accDeliverTo instanceof InternationalAccount)) {
                throw new IllegalArgumentException("You can send money only to another international accounts");
            }
            accInUse.wire(accDeliverTo, amountToDeliver);
        } else {
            logger.warn(NO_SUCH_ACCOUNT + deliverToNumber + "!!!");
            throw new IllegalArgumentException("No such account number to send money!");
        }
        return accInUse.getAmountOfMoney();
    }

    public void requestForNewAccount(int numberOfAccountType) throws IllegalArgumentException {
        switch (numberOfAccountType) {
            case 1:
                bank.getRequestsForAccount().add(new RegularAccount(new BigDecimal(1000),
                        bank.createRandomNumberForAcc(currentCustomer), currentCustomer));
                break;
            case 2:
                bank.getRequestsForAccount().add(new SavingAccount(new BigDecimal(1000),
                        bank.createRandomNumberForAcc(currentCustomer), currentCustomer));
                break;
            case 3:
                bank.getRequestsForAccount().add(new InternationalAccount(new BigDecimal(1000),
                        bank.createRandomNumberForAcc(currentCustomer), currentCustomer,
                        currentCustomer.getCountry().getCountryIBAN()));
                break;
            default: {
                logger.error("Such type of requested account doesn't exist");
                throw new IllegalArgumentException("No such type of account");
            }
        }
    }


    public String checkHistoryOfSpecificAccount(int indexOfAccount) {
        File file = new File(PARRENT_LOCATION, "customerReportHistoryOfAccount.txt");
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
            logger.error(e);
        }
        return resultOutput;
    }

    public String showStateOfAccountPerSpecificDate(LocalDate date, int indexOfAccount) {
        File file = new File(PARRENT_LOCATION, "customerReportStateOfAccountPerSpecificDate.txt");
        StringBuilder resultOutput = new StringBuilder();
        Boolean isMementoFound = false;
        Account currentAcc = currentCustomer.getAccounts().get(indexOfAccount);
        resultOutput.append("Result for Account:" + currentAcc.getNumber() + " per date:" + date + "\n");
        for (AccountMemento accMemento : currentAcc.getHistoryOfAccount()) {
            if (date.compareTo(accMemento.getDateOfChange()) >= 0) {
                resultOutput.append(accMemento + "\n");
                isMementoFound = true;
            }
        }
        if (!isMementoFound) {
            resultOutput.append("Account hadn't exist in that time");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()))) {
            bw.write(resultOutput.toString());
        } catch (IOException e) {
            logger.error("IOException in 'showStateOfAccountPerSpecificDate'");
            logger.error(e);
        }
        return resultOutput.toString();
    }

    public String checkTransactionsPerSpecificDate(LocalDate dateFrom, LocalDate dateTo) {
        File file = new File(PARRENT_LOCATION, "customerReportHistoryBetween2Dates.txt");
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
            logger.error(e);
        }
        return resultOutput;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
}
