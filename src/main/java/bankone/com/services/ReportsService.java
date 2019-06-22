package bankone.com.services;

import bankone.com.bankdata.Bank;
import bankone.com.bankdata.Customer;
import bankone.com.transactionshistory.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

public class ReportsService {

    private static Logger logger = LogManager.getLogger(ReportsService.class);
    private List<Customer> customers;


    public ReportsService() {
        customers = Bank.getCustomers();
    }

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
            logger.error(e);
        }
        return resultOutput;
    }

    public String generateReportTenBiggestTransaction() {
        File file = new File("src/main/resources/", "tenBiggestTransactions.txt");
        String resultOutput;
        resultOutput = "REPORT ABOUT TEN BIGGEST TRANSACTIONS:\n";
        TreeSet<Transaction> biggestTransactions = new TreeSet<>((Transaction t1, Transaction t2) ->
                t1.getDeliveredAmount().compareTo(t2.getDeliveredAmount()));
        for (Customer customer : customers) {
            for (Transaction currentTransaction : customer.getHistory().values()) {
                if (biggestTransactions.size() < 10) {
                    biggestTransactions.add(currentTransaction);
                    resultOutput += currentTransaction + "\n";
                } else {
                    if (currentTransaction.getDeliveredAmount().
                            compareTo(biggestTransactions.first().getDeliveredAmount()) > 0) {
                        biggestTransactions.pollFirst();
                        biggestTransactions.add(currentTransaction);
                    }
                }
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()))) {
            bw.write(resultOutput);
        } catch (IOException e) {
            logger.error(e);
        }
        return resultOutput;
    }

    public String generateReportTransactionForSpecificDate(LocalDate date) {
        File file = new File("src/main/resources/", "transactionsForSpecificDate.txt");
        String resultOutput;
        resultOutput = "REPORT ABOUT TRANSACTIONS OF DATE: " + date + "\n";
        for (Customer customer : customers) {
            if (!customer.getAccounts().isEmpty()) {
                resultOutput += "Customer: " + customer.getFirstName() + " " + customer.getLastName() + ": \n";
                for (Transaction transaction : customer.getHistory().values()) {
                    if (transaction.getLocalDateOfTransaction().equals(date)) {
                        resultOutput += transaction + "\n";
                    }
                }
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()))) {
            bw.write(resultOutput);
        } catch (IOException e) {
            logger.error(e);
        }
        return resultOutput;
    }

    public String generateReportTransactionForSpecificType(Class<? extends Transaction> classOfType) {
        File file = new File("src/main/resources/", "transactionForSpecificType.txt");
        String checkedType = classOfType.getSimpleName();
        String resultOutput;
        resultOutput = "REPORT ABOUT TRANSACTIONS OF TYPE: " + checkedType + "\n";
        for (Customer customer : customers) {
            if (!customer.getAccounts().isEmpty()) {
                resultOutput += "Customer: " + customer.getFirstName() + " " +
                        customer.getLastName() + ": \n";
                for (Transaction transaction : customer.getHistory().values()) {
                    if (transaction.getClass().getSimpleName().equals(checkedType)) {
                        resultOutput += transaction + "\n";
                    }
                }
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()))) {
            bw.write(resultOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultOutput;
    }

    public String generateReportFiveLastCustomers() {
        File file = new File("src/main/resources/", "fiveLastCustomers.txt");
        String resultOutput;
        resultOutput = "REPORT ABOUT 5 LAST CUSTOMERS:\n";
        for (int i = (customers.size() - 5); i < customers.size(); i++) {
            resultOutput += customers.get(i) + "\n";
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()))) {
            bw.write(resultOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultOutput;
    }

}
