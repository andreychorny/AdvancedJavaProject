package BankOne.com.services;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Customer;
import BankOne.com.TransactionsHistory.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.*;

public class ReportsService {

    private Bank bank;
    private List<Customer> customers = new ArrayList<>();
    private static Logger logger = LogManager.getLogger(ReportsService.class);


    void generateReportTransactionOfCustomer(Customer customer) {
        logger.info("REPORT ABOUT TRANSACTIONS OF CUSTOMER" + customer.getFirstName() + " " +
                customer.getLastName() + ": ");
        for (Transaction transaction : customer.getHistory().values()) {
            logger.info(transaction);
        }
    }

    void generateReportTenBiggestTransaction(Customer customer) {
        logger.info("REPORT ABOUT TEN BIGGEST TRANSACTIONS:");
        TreeSet<Transaction> biggestTransactions = new TreeSet<>((Transaction t1, Transaction t2) ->
                t1.getDeliveredAmount().compareTo(t2.getDeliveredAmount()));
        for (Transaction currentTransaction : customer.getHistory().values()) {
            if(biggestTransactions.size()<10){
                biggestTransactions.add(currentTransaction);
                logger.info(currentTransaction);
            }else {
                if(currentTransaction.getDeliveredAmount().
                        compareTo(biggestTransactions.first().getDeliveredAmount()) > 0){
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

    <T extends Transaction> void generateReportTransactionForSpecificType(T typeOfTransactionToCheck) {
        String checkedType = typeOfTransactionToCheck.getClass().getName();
        logger.info("REPORT ABOUT TRANSACTIONS OF TYPE: " + checkedType);
        for (Customer customer : customers) {
            logger.info("Customer: " + customer.getFirstName() + " " +
                    customer.getLastName() + ": ");
            for (Transaction transaction : customer.getHistory().values()) {
                if (transaction.getClass().getName().equals(checkedType)) {
                    logger.info(transaction);
                }
            }
        }
    }

    void generateReportFiveLastCustomers() {
        logger.info("REPORT ABOUT 5 LAST CUSTOMERS: ");
        for (int i = (customers.size() - 5); i < customers.size(); i++) {
            logger.info(customers.get(i));
        }
    }

    public ReportsService(Bank bank) {
        this.bank = bank;
        customers = this.bank.getCustomers();
    }

}
