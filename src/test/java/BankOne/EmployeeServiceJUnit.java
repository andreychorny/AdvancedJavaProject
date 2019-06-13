package BankOne;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Country;
import BankOne.com.BankData.Customer;
import BankOne.com.accounts.InternationalAccount;
import BankOne.com.accounts.RegularAccount;
import BankOne.com.services.CustomerService;
import BankOne.com.services.EmployeeService;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceJUnit {

    static EmployeeService serviceForTest;

    @BeforeEach
    void createEmployeesToWorkWith() throws IllegalArgumentException {
        Bank.createNewEmployee("ouroboros", "superqwerty",
                "Andrii", "Chornyi");
        serviceForTest = new EmployeeService("ouroboros", "superqwerty");
        assertNotNull(serviceForTest);
    }

    @AfterEach
    void cleanseAllData(){
        Bank.getCustomers().clear();
        Bank.getEmployees().clear();
        Bank.getRequestsForAccount().clear();
    }

    @Test
    void testValidationOfNameAndLastName(){
        LocalDate dateOfBirth = LocalDate.of(1997,07,01);
        assertThrows(IllegalArgumentException.class,() -> serviceForTest.createNewCustomer("johnny","bravos",
                "Name1", "lastName", dateOfBirth, Country.UKRAINE),
                "Number in name - bad format");
        assertThrows(IllegalArgumentException.class,() -> serviceForTest.createNewCustomer("johnny","bravos",
                "Name", "lastName!", dateOfBirth, Country.UKRAINE),
                "Special symbol in lastName - bad format");
        assertThrows(IllegalArgumentException.class,() -> serviceForTest.createNewCustomer("johnny","bravos",
                "Name","Z", dateOfBirth, Country.UKRAINE), "LastName is too short");
        assertThrows(IllegalArgumentException.class,() -> serviceForTest.createNewCustomer("johnny","bravos",
                "K","Zack", dateOfBirth, Country.UKRAINE), "Name is too short");
    }

    @Test
    void testValidationOfLoginAndPassword(){
        LocalDate dateOfBirth = LocalDate.of(1997,07,01);
        assertThrows(IllegalArgumentException.class,() -> serviceForTest.createNewCustomer("john!ny","bravos",
                "Name", "lastName", dateOfBirth, Country.UKRAINE),
                "Special symbol in loggin - bad format");
        assertThrows(IllegalArgumentException.class,() -> serviceForTest.createNewCustomer("johnny","bravo",
                "Name", "lastName", dateOfBirth, Country.UKRAINE),
                "Password too short - bad format");
    }

    @Test
    void testCorrectnessOfLoggingSystem(){
        assertThrows(IllegalArgumentException.class, () -> new EmployeeService("ouroboros", "wrongPassword"),
                "Logging info doesn't match - IllegalArgumentException");
    }

    @Test
    void testCreatingOfNewCustomer() throws IllegalArgumentException {
        String newCustomerLogin = "azorahai";
        String newCustomerPassword = "trueKing002";
        String newCustomerFirstName = "Stannis";
        String newCustomerLastName = "Baratheon";
        LocalDate newCustomerDateOfBirth = LocalDate.of(1935, 11, 27);
        Customer newCustomer = new Customer(newCustomerLogin, newCustomerPassword, newCustomerFirstName,
                newCustomerLastName, newCustomerDateOfBirth, LocalDate.now(), Country.UKRAINE);
        assertEquals(newCustomer, serviceForTest.createNewCustomer(newCustomerLogin,
                newCustomerPassword, newCustomerFirstName, newCustomerLastName,
                newCustomerDateOfBirth, Country.UKRAINE));
    }

    @Test
    void testLoginExistIllegalArgumentExceptionWhileCreatingCustomer() throws IllegalArgumentException {
        String sameLoginForTwoCustomers = "azorahai";
        serviceForTest.createNewCustomer(sameLoginForTwoCustomers,"zxcvbn", "nameOne",
                "lastNameOne", LocalDate.of(1975, 9, 11),Country.AMERICA);
        assertThrows(IllegalArgumentException.class, () -> serviceForTest.createNewCustomer(sameLoginForTwoCustomers,"qwerty",
                "nameTwo","lastNameTwo", LocalDate.of(1999,1,27),
                Country.ENGLAND));

    }

    @Test
    void testViewingDataOfSpecificCustomer() throws IllegalArgumentException {
        LocalDate customerDateOfBirth = LocalDate.of(2000, 11, 27);
        serviceForTest.createNewCustomer("CustLogin", "CustPassword",
                "CustName", "CustLastName", customerDateOfBirth, Country.POLAND);
        String outputedCustomerInfo = serviceForTest.viewDataOfClient(
                Bank.retrievePersonByLogin("CustLogin"));
        assertEquals(rightFormatOfKeepingCustomerInfo(), outputedCustomerInfo);
    }

    @Test
    void testAcceptingRequestOfAccount() throws IllegalArgumentException{
        Customer currentCustomer;
        LocalDate dateOfCustomerBirth = LocalDate.of(1955, 10, 26);
        currentCustomer = serviceForTest.createNewCustomer("loginCustomer", "qwerty",
                "Bob", "Dylan", dateOfCustomerBirth, Country.GERMANY);
        CustomerService customerService;
        customerService = new CustomerService("loginCustomer", "qwerty");
        customerService.requestForNewAccount(1);
        customerService.requestForNewAccount(2);
        customerService.requestForNewAccount(3);
        assertEquals(3,Bank.getRequestsForAccount().size());
        assertEquals(0,currentCustomer.getAccounts().size());
        serviceForTest.acceptRequestsForAccounts(true);
        assertEquals(1,currentCustomer.getAccounts().size());
        assertEquals(2,Bank.getRequestsForAccount().size());
        serviceForTest.acceptRequestsForAccounts(false);
        assertEquals(1,currentCustomer.getAccounts().size());
        assertEquals(1,Bank.getRequestsForAccount().size());
        serviceForTest.acceptRequestsForAccounts(true);
        assertEquals(2,currentCustomer.getAccounts().size());
        assertEquals(0,Bank.getRequestsForAccount().size());
        assertEquals(RegularAccount.class,currentCustomer.getAccounts().get(0).getClass());
        assertEquals(InternationalAccount.class,currentCustomer.getAccounts().get(1).getClass());
    }

    private String rightFormatOfKeepingCustomerInfo() {
        LocalDate dateToCheck = LocalDate.now();
        String dateJoinToBankAsString = dateToCheck.format(DateTimeFormatter.ISO_DATE);
        return "Customer{\nlogin= CustLogin\n firstName= CustName,\n" +
                " lastName= CustLastName,\n dateOfBirth= 2000-11-27,\n dateOfJoiningToBank= "+dateJoinToBankAsString+
        ",\n" +
                " accounts:}";
    }
}
