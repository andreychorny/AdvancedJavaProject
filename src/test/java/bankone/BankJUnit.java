package bankone;

import bankone.com.bankdata.Bank;
import bankone.com.bankdata.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankJUnit {

    private Bank bank;
    
    @BeforeEach
    void setUpbank(){
        bank = Bank.getInstance();
    }
    
    @Test
    void testAddingNewEmployee() throws IllegalArgumentException{
        String newEmployeeOneLogin = "Ouroboros";
        String newEmployeeOnePassword = "qwerty";
        String newEmployeeOneFirstName = "Bob";
        String newEmployeeOneLastName = "McFly";
        Employee employeeOne = new Employee(newEmployeeOneLogin,newEmployeeOnePassword,
                newEmployeeOneFirstName,newEmployeeOneLastName);

        assertEquals(employeeOne, bank.createNewEmployee(newEmployeeOneLogin,newEmployeeOnePassword,
                newEmployeeOneFirstName,newEmployeeOneLastName));

        assertEquals(bank.retrievePersonByLogin("Ouroboros").getClass(),Employee.class);
    }

    @Test
    void testRestrictionOfLogginRepetation() throws IllegalArgumentException{
        bank.createNewEmployee("login1","password1",
                "nameOne","lastNameTwo");
        assertThrows(IllegalArgumentException.class,() ->  bank.createNewEmployee("login1",
                "password2", "nameTwo","lastNameTwo"),
                "Login exist in database - IllegalArgumentException");
    }

    @Test
    void testValidationOfNameAndLastName(){
        assertThrows(IllegalArgumentException.class,() -> bank.createNewEmployee("johnny","bravos",
                "Name1","lastName"), "Number in name - bad format");
        assertThrows(IllegalArgumentException.class,() -> bank.createNewEmployee("johnny","bravos",
                "Name","lastName!"), "Special symbol in lastName - bad format");
        assertThrows(IllegalArgumentException.class,() -> bank.createNewEmployee("johnny","bravos",
                "Name","Z"), "LastName is too short");
        assertThrows(IllegalArgumentException.class,() -> bank.createNewEmployee("johnny","bravos",
                "K","Zack"), "Name is too short");
    }

    @Test
    void testValidationOfLoginAndPassword(){
        assertThrows(IllegalArgumentException.class,() -> bank.createNewEmployee("johnny?","bravos","Name",
                "lastName"), "Special symbol in login - bad format");
        assertThrows(IllegalArgumentException.class,() -> bank.createNewEmployee("johnny","bravo","Name",
                "lastName"), "password to short - bad format");
    }
}
