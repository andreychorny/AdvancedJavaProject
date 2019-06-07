package BankOne;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Customer;
import BankOne.com.accounts.Account;
import BankOne.com.accounts.InternationalAccount;
import BankOne.com.accounts.RegularAccount;
import BankOne.com.accounts.SavingAccount;
import BankOne.com.services.CustomerService;
import BankOne.com.services.EmployeeService;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceJUnit {

    private CustomerService customerService;
    private Customer currentCustomer;
    private EmployeeService employeeService;

    @BeforeEach
    void createCustomerToWorkWith() throws Exception {
        Bank.createNewEmployee("loginEmployee", "password", "Employee", "Employee");
        employeeService = new EmployeeService("loginEmployee", "password");
        LocalDate dateOfCustomerBirth = LocalDate.of(1955, 10, 26);
        currentCustomer = employeeService.createNewCustomer("loginCustomer", "qwerty",
                "Bob", "Dylan", dateOfCustomerBirth);
        customerService = new CustomerService("loginCustomer", "qwerty");

    }

    @AfterEach
    void cleanseAllData() throws Exception {
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

    @Test
    void testCheckAllAccounts() throws Exception {
        customerService.requestForNewAccount(1);
        customerService.requestForNewAccount(2);
        customerService.requestForNewAccount(3);
        customerService.requestForNewAccount(1);
        employeeAcceptsAllAccounts();
        assertEquals(4, currentCustomer.getAccounts().size());
        assertEquals(rightInfoFormatForCheckAllAccounts(), customerService.showAllAccounts());

    }

    @Test
    void testCreditFromRegularAcc() throws Exception {
        customerService.requestForNewAccount(1);
        employeeService.acceptRequestsForAccounts(true);
        Customer secondCustomer = createSecondCustomerAndHisAccounts();
        //Amount of money: RoundingMode.DOWN till 2 digits after dot
        assertEquals(BigDecimal.valueOf(549.21), customerService.creditFromRegularAcc(0,
                secondCustomer.getAccounts().get(0).getNumber(), BigDecimal.valueOf(450.79123)));
        assertEquals(BigDecimal.valueOf(1450.79), secondCustomer.getAccounts().get(0).getAmountOfMoney());
    }

    @Test
    void testCreditFromSavingAcc() throws Exception {
        customerService.requestForNewAccount(2);
        employeeService.acceptRequestsForAccounts(true);
        Customer secondCustomer = createSecondCustomerAndHisAccounts();
        //Amount of money: RoundingMode.DOWN till 2 digits after dot
        assertEquals(BigDecimal.valueOf(549.21), customerService.creditFromSavingAcc(0,
                secondCustomer.getAccounts().get(0).getNumber(), BigDecimal.valueOf(450.79123)));
        assertEquals(BigDecimal.valueOf(1450.79), secondCustomer.getAccounts().get(0).getAmountOfMoney());
        assertThrows(Exception.class, () -> customerService.creditFromSavingAcc(
                0,secondCustomer.getAccounts().get(1).getNumber(), BigDecimal.valueOf(100)),
                "You cannot send money from saving account to saving account");
        assertThrows(Exception.class, () -> customerService.creditFromSavingAcc(
                0,secondCustomer.getAccounts().get(2).getNumber(), BigDecimal.valueOf(100)),
                "You cannot send money from saving account to international account");

    }

    Customer createSecondCustomerAndHisAccounts() throws Exception {
        Customer secondCustomer = employeeService.createNewCustomer("login2", "password2",
                "name2","lastName2", LocalDate.of(1996, 8, 24));
        CustomerService secondCustomerService = new CustomerService("login2", "password2");
        secondCustomerService.requestForNewAccount(1);
        secondCustomerService.requestForNewAccount(2);
        secondCustomerService.requestForNewAccount(3);
        employeeService.acceptRequestsForAccounts(true);
        employeeService.acceptRequestsForAccounts(true);
        employeeService.acceptRequestsForAccounts(true);
        return secondCustomer;
    }

    void employeeAcceptsAllAccounts() throws Exception {
        EmployeeService employeeService = new EmployeeService("loginEmployee", "password");
        while (Bank.getRequestsForAccount().size() != 0) {
            employeeService.acceptRequestsForAccounts(true);
        }
    }

    String rightInfoFormatForCheckAllAccounts() {
        return "0: Regular:" + currentCustomer.getAccounts().get(0).getNumber() + "  balance:1000\n" +
                "1: Saving:" + currentCustomer.getAccounts().get(1).getNumber() + "  balance:1000\n" +
                "2: International:" + currentCustomer.getAccounts().get(2).getNumber() + "  balance:1000\n" +
                "3: Regular:" + currentCustomer.getAccounts().get(3).getNumber() + "  balance:1000\n";
    }
}
