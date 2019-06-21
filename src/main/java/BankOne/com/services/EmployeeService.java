package BankOne.com.services;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Country;
import BankOne.com.BankData.Customer;
import BankOne.com.BankData.Employee;
import BankOne.com.accounts.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;

public class EmployeeService {

    private static Logger logger = LogManager.getLogger(EmployeeService.class);
    private Employee currentEmployee;

    public EmployeeService(String login, String password) throws IllegalArgumentException {
        if (Bank.checkIfLoggingInfoIsSuitable(login, password)) {
            currentEmployee = Bank.retrievePersonByLogin(login);
        } else {
            logger.warn("You entered wrong logging info. Check your password and login again");
            throw new IllegalArgumentException("WRONG LOGGING INFO!");
        }
    }

    public Customer createNewCustomer(String login, String password, String firstName,
                                      String lastName, LocalDate dateOfBirth, Country country)
            throws IllegalArgumentException {
        if ((!Bank.nameValidationCorrect(firstName) || !Bank.nameValidationCorrect(lastName))) {
            logger.warn("BAD FORMAT OF NAME OR LASTNAME! NAME AND LAST NAME MUST BE AT LEAST 2 SYMBOLS LONG AND " +
                    "DO NOT CONTAIN SPECIAL SYMBOLS!");
            throw new IllegalArgumentException("WRONG FORMAT OF NAME/LASTNAME!");
        }
        if ((!Bank.logAndPassValidationCorrect(login)) || (!Bank.logAndPassValidationCorrect(password))) {
            logger.warn("BAD FORMAT OF LOGIN OR PASSWORD! LOGIN AND PASSWORD MUST BE AT LEAST 6 SYMBOLS LONG AND " +
                    "DO NOT CONTAIN SPECIAL SYMBOLS EXCEPT '_'");
            throw new IllegalArgumentException("WRONG FORMAT OF LOGIN/PASSWORD!");
        }
        if (Bank.checkIfLoginUnique(login)) {
            Customer newCustomer = new Customer(login, password, firstName, lastName,
                    dateOfBirth, LocalDate.now(), country);
            Bank.getCustomers().add(newCustomer);
            return newCustomer;
        } else {
            logger.warn("Entered login is not unique!!!");
            throw new IllegalArgumentException("ENTERED LOGIN IS NOT UNIQUE!!!");
        }
    }

    public String viewDataOfClient(Customer customer) {
        logger.info("Retrieving data of customer:");
        logger.info(customer.toString());
        return customer.toString();
    }

    //possible realisation of this is strongly depended from front-end
    public void acceptRequestsForAccounts(boolean decision) {
        List<Account> requests = Bank.getRequestsForAccount();
        if (requests.size() != 0) {
            Account chechingAccount = requests.get(0);
            logger.info("Request from: " + chechingAccount.getOwnerOfAccount().getFirstName() + " " +
                    chechingAccount.getOwnerOfAccount().getLastName() + " with login: " +
                    chechingAccount.getOwnerOfAccount().getLogin());
            logger.info("Requested account of type " + chechingAccount.getClass().getName());
            logger.info("Do you accept this account?");

            //somewhere here should be decision check on front-end
            if (decision) {
                chechingAccount.getOwnerOfAccount().addAccount(chechingAccount);
            }
            requests.remove(0);
        }
    }
}
