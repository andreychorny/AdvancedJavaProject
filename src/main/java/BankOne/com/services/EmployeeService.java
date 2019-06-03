package BankOne.com.services;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class EmployeeService {

    private List<Customer> customers = new ArrayList<>();

    private static Logger logger = LogManager.getLogger(EmployeeService.class);

    void createNewCustomer() throws Exception {
        Scanner in = new Scanner(System.in);
        String login = in.nextLine();
        String password = in.nextLine();
        if (Bank.checkIfLoginUnique(login)) {
            System.out.println("Enter first name of new customer");
            String firstName = in.nextLine();
            System.out.println("Enter last name of new customer");
            String lastName = in.nextLine();
            System.out.println("Enter year of birth");
            int yearOfBirth = in.nextInt();
            yearOfBirth = yearOfBirth - 1900;
            System.out.println("Enter month(number) of birth");
            int monthOfBirth = in.nextInt();
            monthOfBirth = monthOfBirth - 1;
            System.out.println("Enter day of birth");
            int dayOfBirth = in.nextInt();
            customers.add(new Customer(login, password, firstName, lastName,
                    new Date(yearOfBirth, monthOfBirth, dayOfBirth), new Date()));
        } else {
            throw new Exception("LOGIN IS NOT UNIQUE!!!");
        }
    }

    void viewDataOfClient(Customer customer) {
        logger.info(customer.toString());
    }
}
