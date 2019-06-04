import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Employee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BankJUnit {

    @Test
    public void testAddingNewEmployee() throws Exception{
        String newEmployeeOneLogin = "Ouroboros";
        String newEmployeeOnePassword = "qwerty";
        String newEmployeeOneFirstName = "Bob";
        String newEmployeeOneLastName = "McFly";
        Employee employeeOne = new Employee(newEmployeeOneLogin,newEmployeeOnePassword,
                newEmployeeOneFirstName,newEmployeeOneLastName);
        assertEquals(employeeOne, Bank.createNewEmployee(newEmployeeOneLogin,newEmployeeOnePassword,
                newEmployeeOneFirstName,newEmployeeOneLastName));

        String newEmployeeTwoLogin = "Ouroboros";
        String newEmployeeTwoPassword = "ambasador42";
        String newEmployeeTwoFirstName = "Johnny";
        String newEmployeeTwoLastName = "Willi1ams";
        assertThrows(Exception.class,() ->  Bank.createNewEmployee(newEmployeeTwoLogin,
                newEmployeeTwoPassword, newEmployeeTwoFirstName,newEmployeeTwoLastName),
                "Login exist in database Exception");

    }
}
