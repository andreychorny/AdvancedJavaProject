package bankone;

import bankone.com.bankdata.Bank;
import bankone.com.bankdata.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BankJUnit {

    private Bank bank;
    private final String RIGHT_PASS_FORM = "bravos";
    private final String RIGHT_LOG_FORM = "johnny";
    private final String RIGHT_NAME_FORM = "Name";
    private final String RIGHT_LASTNAME_FORM = "lastName";

    @BeforeEach
    void setUpbank() {
        bank = Bank.getInstance();
    }

    @Test
    void testAddingNewEmployee() throws IllegalArgumentException {
        String newEmployeeOneLogin = "Ouroboros";
        char[] newEmployeeOneSecret = "qwerty".toCharArray();
        String newEmployeeOneFirstName = "Bob";
        String newEmployeeOneLastName = "McFly";
        Employee employeeOne = new Employee(newEmployeeOneLogin, String.valueOf(newEmployeeOneSecret),
                newEmployeeOneFirstName, newEmployeeOneLastName);

        assertEquals(employeeOne, bank.createNewEmployee(newEmployeeOneLogin, String.valueOf(newEmployeeOneSecret),
                newEmployeeOneFirstName, newEmployeeOneLastName));

        assertEquals(bank.retrievePersonByLogin("Ouroboros").getClass(), Employee.class);
    }

    @Test
    void testRestrictionOfLogginRepetation() throws IllegalArgumentException {
        bank.createNewEmployee("login1", "password1",
                "nameOne", "lastNameTwo");
        assertThrows(IllegalArgumentException.class, () -> bank.createNewEmployee("login1",
                "password2", "nameTwo", "lastNameTwo"),
                "Login exist in database - IllegalArgumentException");
    }

    @Test
    void testValidationOfNameAndLastName() {
        assertThrows(IllegalArgumentException.class, () -> bank.createNewEmployee(RIGHT_LOG_FORM, RIGHT_PASS_FORM,
                "Name1", RIGHT_LASTNAME_FORM), "Number in name - bad format");
        assertThrows(IllegalArgumentException.class, () -> bank.createNewEmployee(RIGHT_LOG_FORM, RIGHT_PASS_FORM,
                RIGHT_NAME_FORM, "lastName!"), "Special symbol in lastName - bad format");
        assertThrows(IllegalArgumentException.class, () -> bank.createNewEmployee(RIGHT_LOG_FORM, RIGHT_PASS_FORM,
                RIGHT_NAME_FORM, "Z"), "LastName is too short");
        assertThrows(IllegalArgumentException.class, () -> bank.createNewEmployee(RIGHT_LOG_FORM, RIGHT_PASS_FORM,
                "K", RIGHT_LASTNAME_FORM), "Name is too short");
    }

    @Test
    void testValidationOfLoginAndPassword() {
        assertThrows(IllegalArgumentException.class, () -> bank.createNewEmployee("johnny?", RIGHT_PASS_FORM, RIGHT_NAME_FORM,
                RIGHT_LASTNAME_FORM), "Special symbol in login - bad format");
        assertThrows(IllegalArgumentException.class, () -> bank.createNewEmployee(RIGHT_LOG_FORM, "bravo", RIGHT_NAME_FORM,
                RIGHT_LASTNAME_FORM), "password to short - bad format");
    }
}
