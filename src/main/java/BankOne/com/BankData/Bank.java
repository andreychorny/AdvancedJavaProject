package BankOne.com.BankData;

import BankOne.com.TransactionsHistory.Transaction;
import BankOne.com.accounts.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    void createNewEmployee() throws Exception {
        Scanner in = new Scanner(System.in);
        String login = in.nextLine();
        String password = in.nextLine();
        if (Bank.checkIfLoginUnique(login)) {
            System.out.println("Enter first name of new employee");
            String firstName = in.nextLine();
            System.out.println("Enter last name of new employee");
            String lastName = in.nextLine();
            employees.add(new Employee(login, password, firstName, lastName));
        } else {
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

    public List<Customer> getCustomers() {
        return customers;
    }

    public static List<Account> getRequestsForAccount() {
        return requestsForAccount;
    }
}
