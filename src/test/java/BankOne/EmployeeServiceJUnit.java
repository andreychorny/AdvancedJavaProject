package BankOne;

import BankOne.com.BankData.Bank;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EmployeeServiceJUnit {

    @BeforeAll
    public static void createEmployeesToWorkWith() throws Exception{
        Bank.createNewEmployee("ouroboros","superqwerty",
                "Andrii","Chornyi");
        Bank.createNewEmployee("trickyknight","12345678",
                "James","Bond");
    }

    @Test
    public void testAddingNewEmployee(){


    }
}
