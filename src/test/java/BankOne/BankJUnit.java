package BankOne;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Employee;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankJUnit {

    @Test
    void testAddingNewEmployee() throws IllegalArgumentException{
        String newEmployeeOneLogin = "Ouroboros";
        String newEmployeeOnePassword = "qwerty";
        String newEmployeeOneFirstName = "Bob";
        String newEmployeeOneLastName = "McFly";
        Employee employeeOne = new Employee(newEmployeeOneLogin,newEmployeeOnePassword,
                newEmployeeOneFirstName,newEmployeeOneLastName);

        assertEquals(employeeOne, Bank.createNewEmployee(newEmployeeOneLogin,newEmployeeOnePassword,
                newEmployeeOneFirstName,newEmployeeOneLastName));

        assertEquals(Bank.retrievePersonByLogin("Ouroboros").getClass(),Employee.class);
    }

    @Test
    void testRestrictionOfLogginRepetation() throws IllegalArgumentException{
        Bank.createNewEmployee("login1","password1",
                "nameOne","lastNameTwo");
        assertThrows(IllegalArgumentException.class,() ->  Bank.createNewEmployee("login1",
                "password2", "nameTwo","lastNameTwo"),
                "Login exist in database - IllegalArgumentException");
    }

    @Test
    void testValidationOfNameAndLastName(){
        assertThrows(IllegalArgumentException.class,() -> Bank.createNewEmployee("johnny","bravos","Name1",
                "lastName"), "Number in name - bad format");
        assertThrows(IllegalArgumentException.class,() -> Bank.createNewEmployee("johnny","bravos","Name",
                "lastName!"), "Special symbol in lastName - bad format");
        assertThrows(IllegalArgumentException.class,() -> Bank.createNewEmployee("johnny","bravos","Name",
                "Z"), "LastName is too short");
        assertThrows(IllegalArgumentException.class,() -> Bank.createNewEmployee("johnny","bravos","K",
                "Zack"), "Name is too short");
    }

    @Test
    void testValidationOfLoginAndPassword(){
        assertThrows(IllegalArgumentException.class,() -> Bank.createNewEmployee("johnny?","bravos","Name",
                "lastName"), "Special symbol in login - bad format");
        assertThrows(IllegalArgumentException.class,() -> Bank.createNewEmployee("johnny","bravo","Name",
                "lastName"), "password to short - bad format");
    }
}
