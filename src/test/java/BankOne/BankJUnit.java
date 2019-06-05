package BankOne;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Employee;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankJUnit {

    @Test
    void testAddingNewEmployee() throws Exception{
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
    void testRestrictionOfLogginRepetation() throws Exception{
        Bank.createNewEmployee("login1","password1",
                "nameOne","lastNameTwo");
        assertThrows(Exception.class,() ->  Bank.createNewEmployee("login1",
                "password2", "nameTwo","lastNameTwo"),
                "Login exist in database - Exception");
    }
}
