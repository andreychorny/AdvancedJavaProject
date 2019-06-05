package BankOne;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Customer;
import BankOne.com.services.EmployeeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceJUnit {

    static EmployeeService serviceForTest;

    @BeforeAll
    static void createEmployeesToWorkWith() throws Exception {
        Bank.createNewEmployee("ouroboros", "superqwerty",
                "Andrii", "Chornyi");
        serviceForTest = new EmployeeService("ouroboros", "superqwerty");
        assertNotNull(serviceForTest);
    }

    @Test
    void testCorrectnessOfLoggingSystem() throws Exception {
        assertThrows(Exception.class, () -> new EmployeeService("ouroboros", "wrongPassword"),
                "Logging info doesn't match - Exception");
    }

    @Test
    void testCreatingOfNewCustomer() throws Exception {
        String newCustomerLogin = "azorahai";
        String newCustomerPassword = "trueKing002";
        String newCustomerFirstName = "Stannis";
        String newCustomerLastName = "Baratheon";
        LocalDate newCustomerDateOfBirth = LocalDate.of(1935, 11, 27);
        Customer newCustomer = new Customer(newCustomerLogin, newCustomerPassword, newCustomerFirstName,
                newCustomerLastName, newCustomerDateOfBirth, LocalDate.now());
        assertEquals(newCustomer, serviceForTest.createNewCustomer(newCustomerLogin,
                newCustomerPassword, newCustomerFirstName, newCustomerLastName,
                newCustomerDateOfBirth));
    }

    @Test
    void testViewingDataOfSpecificCustomer() throws Exception {
        LocalDate customerDateOfBirth = LocalDate.of(2000, 11, 27);
        serviceForTest.createNewCustomer("CustLogin", "CustPassword",
                "CustName", "CustLastName", customerDateOfBirth);
        String outputedCustomerInfo = serviceForTest.viewDataOfClient(
                Bank.retrievePersonByLogin("CustLogin"));
        assertEquals(rightFormatOfKeepingCustomerInfo(), outputedCustomerInfo);
    }

    private String rightFormatOfKeepingCustomerInfo() {
        return "Customer{\nlogin= CustLogin\n firstName= CustName,\n" +
                " lastName= CustLastName,\n dateOfBirth= 2000-11-27,\n dateOfJoiningToBank= 2019-06-05,\n" +
                " accounts:}";
    }
}
