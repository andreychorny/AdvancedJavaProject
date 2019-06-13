package BankOne.com.services;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Customer;
import BankOne.com.TransactionsHistory.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class ReportsService {

    private List<Customer> customers;
    private static Logger logger = LogManager.getLogger(ReportsService.class);


    public String generateReportTransactionOfCustomer(Customer customer) {
        File file = new File("src/main/resources/", "transactionsOfSpecificCustomer.txt");
        String resultOutput;
        resultOutput = "REPORT ABOUT TRANSACTIONS OF CUSTOMER: " + customer.getFirstName() + " " +
                customer.getLastName() + ": \n";
        for (Transaction transaction : customer.getHistory().values()) {
            resultOutput += transaction + "\n";
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()))) {
            bw.write(resultOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultOutput;
    }

    void generateReportTenBiggestTransaction(Customer customer) {
        logger.info("REPORT ABOUT TEN BIGGEST TRANSACTIONS:");
        TreeSet<Transaction> biggestTransactions = new TreeSet<>((Transaction t1, Transaction t2) ->
                t1.getDeliveredAmount().compareTo(t2.getDeliveredAmount()));
        for (Transaction currentTransaction : customer.getHistory().values()) {
            if (biggestTransactions.size() < 10) {
                biggestTransactions.add(currentTransaction);
                logger.info(currentTransaction);
            } else {
                if (currentTransaction.getDeliveredAmount().
                        compareTo(biggestTransactions.first().getDeliveredAmount()) > 0) {
                    biggestTransactions.pollFirst();
                    biggestTransactions.add(currentTransaction);
                }
            }
        }
    }

    void generateReportTransactionForSpecificDate(LocalDate date) {
        logger.info("REPORT ABOUT TRANSACTIONS OF DATE: " + date);
        for (Customer customer : customers) {
            logger.info("Customer: " + customer.getFirstName() + " " +
                    customer.getLastName() + ": ");
            for (Transaction transaction : customer.getHistory().values()) {
                if (transaction.getLocalDateOfTransaction().equals(date)) {
                    logger.info(transaction);
                }
            }
        }
    }

    public String generateReportTransactionForSpecificType(Class<? extends Transaction> classOfType) {
        String checkedType = classOfType.getSimpleName();
        String resultOutput;
        resultOutput = "REPORT ABOUT TRANSACTIONS OF TYPE: " + checkedType +"\n";
        for (Customer customer : customers) {
            resultOutput += "Customer: " + customer.getFirstName() + " " +
                    customer.getLastName() + ": \n";
            for (Transaction transaction : customer.getHistory().values()) {
                if (transaction.getClass().getSimpleName().equals(checkedType)) {
                    resultOutput += transaction +"\n";
                }
            }
        }
        return resultOutput;
    }

    void generateReportFiveLastCustomers() {
        logger.info("REPORT ABOUT 5 LAST CUSTOMERS: ");
        for (int i = (customers.size() - 5); i < customers.size(); i++) {
            logger.info(customers.get(i));
        }
    }

    public ReportsService() {
        customers = Bank.getCustomers();
    }

}
