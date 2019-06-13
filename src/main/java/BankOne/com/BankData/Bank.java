package BankOne.com.BankData;

import BankOne.com.TransactionsHistory.Transaction;
import BankOne.com.accounts.Account;
import BankOne.com.accounts.InternationalAccount;
import BankOne.com.accounts.SavingAccount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
                                      String lastName) throws IllegalArgumentException {
        if((!nameValidation(firstName) || !nameValidation(lastName))){
            logger.warn("BAD FORMAT OF NAME OR LASTNAME! NAME AND LAST NAME MUST BE AT LEAST 2 SYMBOLS LONG AND " +
                    "DO NOT CONTAIN SPECIAL SYMBOLS!");
            throw new IllegalArgumentException("WRONG FORMAT OF NAME/LASTNAME!");
        }
        if((!loginAndPasswordValidation(login)) || (!loginAndPasswordValidation(password))){
            logger.warn("BAD FORMAT OF LOGIN OR PASSWORD! LOGIN AND PASSWORD MUST BE AT LEAST 6 SYMBOLS LONG AND " +
                    "DO NOT CONTAIN SPECIAL SYMBOLS EXCEPT '_'");
            throw new IllegalArgumentException("WRONG FORMAT OF LOGIN/PASSWORD!");
        }
        if (Bank.checkIfLoginUnique(login)) {
            Employee newEmployee = new Employee(login, password, firstName, lastName);
            employees.add(newEmployee);
            return newEmployee;
        } else {
            logger.warn("ENTERED LOGIN IS NOT UNIQUE!");
            throw new IllegalArgumentException("LOGIN IS NOT UNIQUE!!!");
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

    public static<T extends Person> boolean checkIfLoggingInfoIsSuitable(String login,
                                                                        String passwordString) {
        char[] password = passwordString.toCharArray();
        for (Customer customer : customers) {
            if (customer.getLogin().equals(login) && Arrays.equals(customer.getPassword(), password)) {
                return true;
            }
        }
        for (Employee employee : employees) {
            if (employee.getLogin().equals(login) && Arrays.equals(employee.getPassword(), password)) {
                return true;
            }
        }
        return false;
    }

    public static<T extends Person> T retrievePersonByLogin(String login) {
        for (Customer customer : customers) {
            if (customer.getLogin().equals(login)) {
                return (T)customer;
            }
        }
        for (Employee employee : employees) {
            if (employee.getLogin().equals(login)) {
                return (T)employee;
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

    public static boolean checkIfIBANIsUnique(String IBANToCheck){
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                if ((account instanceof InternationalAccount) &&
                        ((InternationalAccount) account).getIBAN().equals(IBANToCheck)) return false;
            }
        }
        return true;
    }

    public static void calculateInterestsOfCustomerAccs(Customer customer){
        for (Account account : customer.getAccounts()){
            if(account instanceof SavingAccount){
                LocalDateTime dateOfNow = LocalDateTime.now();
                dateOfNow = dateOfNow.truncatedTo(ChronoUnit.MINUTES);
                long differenceInMinutes = ((SavingAccount)account).getTimeOfLastInterestAdd().until(dateOfNow,
                        ChronoUnit.MINUTES);
                BigDecimal moneyIncrease = account.getAmountOfMoney().multiply(BigDecimal.
                        valueOf(0.01*differenceInMinutes));
                account.setAmountOfMoney(account.getAmountOfMoney().add(moneyIncrease));
                ((SavingAccount) account).setTimeOfLastInterestAdd(dateOfNow);
            }
        }
    }

    public static boolean nameValidation(String name){
        if(name.length()<2){
            return false;
        }
        return name.matches( "[A-Za-z]*" );
    }

    public static boolean loginAndPasswordValidation(String login){
        if(login.length()<6){
            return false;
        }
        return login.matches( "[A-Za-z0-9_]*" );
    }

    public static List<Customer> getCustomers() {
        return customers;
    }

    public static List<Account> getRequestsForAccount() {
        return requestsForAccount;
    }

    public static List<Employee> getEmployees() {
        return employees;
    }
}
