package BankOne.com.services;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Customer;
import BankOne.com.BankData.Employee;
import BankOne.com.accounts.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeService {

    private Employee currentEmployee;

    private static Logger logger = LogManager.getLogger(EmployeeService.class);

    public EmployeeService(String login, String password) throws Exception {
        if (Bank.checkIfLoggingInfoIsSuitable(login, password)) {
            currentEmployee = Bank.retrievePersonByLogin(login);
        }else {
            throw new Exception("WRONG LOGGING INFO!");
        }
    }

    public Customer createNewCustomer(String login, String password, String firstName,
                           String lastName, LocalDate dateOfBirth) throws Exception {
        if (Bank.checkIfLoginUnique(login)) {
            Customer newCustomer = new Customer(login, password, firstName, lastName,
                    dateOfBirth, LocalDate.now());
            Bank.getCustomers().add(newCustomer);
            return newCustomer;
        } else {
            throw new Exception("ENTERED LOGIN IS NOT UNIQUE!!!");
        }
    }

    public String viewDataOfClient(Customer customer) {
        logger.info("Retrieving data of customer:");
        logger.info(customer.toString());
        return customer.toString();
    }

    //possible realisation of this is strongly depended from front-end
    void acceptRequestsForAccounts(boolean decision){
        List<Account> requests = Bank.getRequestsForAccount();
        if(requests.size()!=0){
            Account chechingAccount = requests.get(0);
            logger.info("Request from: " + chechingAccount.getOwnerOfAccount().getFirstName() + " " +
                    chechingAccount.getOwnerOfAccount().getLastName() + " with login: " +
                    chechingAccount.getOwnerOfAccount().getLogin());
            logger.info("Requested account of type " + chechingAccount.getClass().getName());
            logger.info("Do you accept this account?");

            //somewhere here should be decision check on front-end
            if(decision) {
                chechingAccount.getOwnerOfAccount().addAccount(chechingAccount);
            }
            requests.remove(0);
        }
    }
}
