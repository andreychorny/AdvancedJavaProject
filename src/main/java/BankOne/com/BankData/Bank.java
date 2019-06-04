package BankOne.com.BankData;

import BankOne.com.TransactionsHistory.Transaction;
import BankOne.com.accounts.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.util.*;

public class Bank {

    static Logger logger = LogManager.getLogger(Bank.class);

    private static List<Customer> customers = new ArrayList<>();

    private static List<Employee> employees = new ArrayList<>();

    private static List<Account> requestsForAccount = new ArrayList<>();

    public static Account findAccount(String number) {
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                if (account.getNumber().equals(number)) return account;
            }
        }
        return null;
    }

    public static Employee createNewEmployee(String login, String password, String firstName,
                                      String lastName) throws Exception {
        if (Bank.checkIfLoginUnique(login)) {
            Employee newEmployee = new Employee(login, password, firstName, lastName);
            employees.add(newEmployee);
            return newEmployee;
        } else {
            logger.warn("ENTERED LOGIN IS NOT UNIQUE!");
            throw new Exception("LOGIN IS NOT UNIQUE!!!");
        }
    }

    void outputAllAccounts() {
        for (Customer customer : customers) {
            System.out.println("Customer: " + customer.getFirstName() + " " + customer.getLastName() + ":");
            for (Account account : customer.getAccounts()) {
                Class accClass = account.getClass();
                String nameOfClass = accClass.toString();
                nameOfClass = nameOfClass.substring(nameOfClass.lastIndexOf(".") + 1);
                System.out.println(nameOfClass + ": " + account.getNumber());
                System.out.println("amount of money: " + account.getAmountOfMoney());
            }
        }
    }

    protected static void addRequestForNewAccount(Account account){
        requestsForAccount.add(account);
    }

    public static boolean checkIfNumberUnique(String number) {
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                if (account.getNumber().equals(number)) return false;
            }
        }
        return true;
    }

    public static boolean checkIfLoginUnique(String login) {
        for (Customer customer : customers) {
            if (customer.getLogin().equals(login)) return false;
        }
        for (Employee employee : employees) {
            if (employee.getLogin().equals(login)) return false;
        }
        return true;
    }

    public static boolean checkIfCustomerInfoIsSuitable(String login, char[] password) {
        for (Customer customer : customers) {
            if (customer.getLogin().equals(login) && Arrays.equals(customer.getPassword(), password)) {
                return true;
            }
        }
        return false;
    }

    public static Customer retrieveCustomerByLogin(String login) {
        for (Customer customer : customers) {
            if (customer.getLogin().equals(login)) {
                return customer;
            }
        }
        return null;
    }

    public static String createRandomNumberForAcc(Customer customer) {
        StringBuffer generatedNumber = new StringBuffer();
        //"BC" is a Bank Code
        generatedNumber.append("BC-");
        DecimalFormat formatter = new DecimalFormat("00000");
        String customerIdFormatted = formatter.format(customer.getId());
        generatedNumber.append(customerIdFormatted);
        generatedNumber.append("-");
        for (int i = 0; i < 3; i++) {
            generatedNumber.append((int) (Math.random() * 10));
        }
        String result = generatedNumber.toString();
        if (!Bank.checkIfNumberUnique(result)) result = createRandomNumberForAcc(customer);
        return result;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public static List<Account> getRequestsForAccount() {
        return requestsForAccount;
    }
}
