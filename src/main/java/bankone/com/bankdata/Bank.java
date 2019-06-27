package bankone.com.bankdata;

import bankone.com.accounts.Account;
import bankone.com.accounts.SavingAccount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static bankone.com.bankdata.BankUtil.*;

public class Bank {

    private static Logger logger = LogManager.getLogger(Bank.class);

    private static Bank bankInstance;

    private List<Customer> customers;

    private List<Employee> employees;

    private List<Account> requestsForAccount;

    public Account findAccount(String number) {
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                if (account.getNumber().equals(number)) return account;
            }
        }
        return null;
    }

    private Bank() {
        customers = new ArrayList<>();

        employees = new ArrayList<>();

        requestsForAccount = new ArrayList<>();
    }

    public synchronized static Bank getInstance() {
        if (bankInstance == null) {
            bankInstance = new Bank();
        }
        return bankInstance;
    }


    public Employee createNewEmployee(String login, String password, String firstName,
                                      String lastName) throws IllegalArgumentException {
        if ((!nameValidationCorrect(firstName) || !nameValidationCorrect(lastName))) {
            logger.warn("BAD FORMAT OF NAME OR LASTNAME! NAME AND LAST NAME MUST BE AT LEAST 2 SYMBOLS LONG AND " +
                    "DO NOT CONTAIN SPECIAL SYMBOLS!");
            throw new IllegalArgumentException("WRONG FORMAT OF NAME/LASTNAME!");
        }
        if ((!logAndPassValidationCorrect(login)) || (!logAndPassValidationCorrect(password))) {
            logger.warn("BAD FORMAT OF LOGIN OR PASSWORD! LOGIN AND PASSWORD MUST BE AT LEAST 6 SYMBOLS LONG AND " +
                    "DO NOT CONTAIN SPECIAL SYMBOLS EXCEPT '_'");
            throw new IllegalArgumentException("WRONG FORMAT OF LOGIN/PASSWORD!");
        }
        if (checkIfLoginUnique(login)) {
            Employee newEmployee = new Employee(login, password, firstName, lastName);
            employees.add(newEmployee);
            return newEmployee;
        } else {
            logger.warn("ENTERED LOGIN" + login + " IS NOT UNIQUE!");
            throw new IllegalArgumentException("LOGIN IS NOT UNIQUE!!!");
        }
    }


    public <T extends Person> T retrievePersonByLogin(String login) {
        for (Customer customer : getInstance().customers) {
            if (customer.getLogin().equals(login)) {
                return (T) customer;
            }
        }
        for (Employee employee : getInstance().employees) {
            if (employee.getLogin().equals(login)) {
                return (T) employee;
            }
        }
        return null;
    }

    public String createRandomNumberForAcc(Customer customer) {
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
        if (!checkIfAccNumberUnique(result)) {
            logger.warn("We created existing random number for acc, do a recursive call of method");
            result = createRandomNumberForAcc(customer);
        }
        return result;
    }

    public void calculateInterestsOfCustomerAccs(Customer customer) {
        for (Account account : customer.getAccounts()) {
            if (account instanceof SavingAccount) {
                LocalDateTime dateOfNow = LocalDateTime.now();
                dateOfNow = dateOfNow.truncatedTo(ChronoUnit.MINUTES);
                long differenceInMinutes = ((SavingAccount) account).getTimeOfLastInterestAdd().until(dateOfNow,
                        ChronoUnit.MINUTES);
                BigDecimal moneyIncrease = account.getAmountOfMoney().multiply(BigDecimal.
                        valueOf(0.01 * differenceInMinutes));
                account.setAmountOfMoney(account.getAmountOfMoney().add(moneyIncrease));
                ((SavingAccount) account).setTimeOfLastInterestAdd(dateOfNow);
            }
        }
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Account> getRequestsForAccount() {
        return requestsForAccount;
    }

}
