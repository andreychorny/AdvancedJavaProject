package BankOne;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Customer;
import BankOne.com.accounts.Account;
import BankOne.com.accounts.InternationalAccount;
import BankOne.com.accounts.RegularAccount;
import BankOne.com.accounts.SavingAccount;
import BankOne.com.services.CustomerService;
import BankOne.com.services.EmployeeService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceJUnit {

    static CustomerService customerService;
    static Customer currentCustomer;

    @BeforeAll
    static void createCAllustomerToWorkWith() throws Exception {
        Bank.createNewEmployee("loginEmployee", "password", "Employee", "Employee");
        EmployeeService employeeService = new EmployeeService("loginEmployee", "password");
        LocalDate dateOfCustomerBirth = LocalDate.of(1955, 10, 26);
        currentCustomer = employeeService.createNewCustomer("loginCustomer", "qwerty",
                "Bob", "Dylan", dateOfCustomerBirth);

    }
    @AfterAll
    static void cleanseAllData() throws Exception {
        Bank.getCustomers().clear();
        Bank.getEmployees().clear();
        Bank.getRequestsForAccount().clear();
    }
    @Test
    void testLoggingIntoCustomerService() throws Exception {
        CustomerService customerServiceToTest = new CustomerService("loginCustomer", "qwerty");
        assertNotNull(customerServiceToTest);
        assertEquals(currentCustomer, customerServiceToTest.getCurrentCustomer());
        assertThrows(Exception.class, () -> new CustomerService("loginCustomer",
                "WrongPassword"), "Login exist in database - Exception");
    }

    @Test
    void testRequestForNewAccount() throws Exception {
        assertEquals(0, Bank.getRequestsForAccount().size());
        customerService = new CustomerService("loginCustomer", "qwerty");
        customerService.requestForNewAccount(1);
        customerService.requestForNewAccount(2);
        customerService.requestForNewAccount(3);
        assertEquals(3, Bank.getRequestsForAccount().size());
        assertTrue(Bank.getRequestsForAccount().get(0) instanceof RegularAccount);
        assertTrue(Bank.getRequestsForAccount().get(1) instanceof SavingAccount);
        assertTrue(Bank.getRequestsForAccount().get(2) instanceof InternationalAccount);
        //Customer applied request, but account doesn't attached to him yet
        assertEquals(0, currentCustomer.getAccounts().size());
        assertEquals(currentCustomer, Bank.getRequestsForAccount().get(0).getOwnerOfAccount());
        assertEquals(currentCustomer, Bank.getRequestsForAccount().get(1).getOwnerOfAccount());
        assertEquals(currentCustomer, Bank.getRequestsForAccount().get(2).getOwnerOfAccount());
    }
}
