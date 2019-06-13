package BankOne;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Country;
import BankOne.com.BankData.Customer;
import BankOne.com.accounts.Account;
import BankOne.com.accounts.InternationalAccount;
import BankOne.com.accounts.RegularAccount;
import BankOne.com.accounts.SavingAccount;
import BankOne.com.services.CustomerService;
import BankOne.com.services.EmployeeService;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

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
                "Bob", "Dylan", dateOfCustomerBirth, Country.AMERICA);
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
        //But possible account itself already knows to which user it belongs to
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
        assertThrows(Exception.class, () -> customerService.creditFromRegularAcc(0,
                secondCustomer.getAccounts().get(0).getNumber(), BigDecimal.valueOf(10000)),
                "Not enough money on account");
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
        assertThrows(Exception.class, () -> customerService.creditFromRegularAcc(0,
                secondCustomer.getAccounts().get(0).getNumber(), BigDecimal.valueOf(10000)),
                "Not enough money on account");
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

    @Test
    void testWireFromInternationalAcc() throws Exception{
        customerService.requestForNewAccount(3);
        employeeService.acceptRequestsForAccounts(true);
        Customer secondCustomer = createSecondCustomerAndHisAccounts();
        assertThrows(Exception.class, () -> customerService.creditFromRegularAcc(0,
                secondCustomer.getAccounts().get(2).getNumber(), BigDecimal.valueOf(10000)),
                "Not enough money on account");
        assertThrows(Exception.class, () -> customerService.creditFromRegularAcc(0,
                secondCustomer.getAccounts().get(0).getNumber(), BigDecimal.valueOf(10000)),
                "You can send money only to other international accounts");
        //Amount of money: RoundingMode.DOWN till 2 digits after dot
        assertEquals(BigDecimal.valueOf(549.21), customerService.wireFromInternational(0,
                secondCustomer.getAccounts().get(2).getNumber(), BigDecimal.valueOf(450.79123)));
        assertEquals(BigDecimal.valueOf(1450.79), secondCustomer.getAccounts().get(2).getAmountOfMoney());

    }

    @Test
    void testCheckHistoryOfSpecificAccount() throws Exception {
        createSecondCustomerAndHisAccounts();
        createFirstCustmAccountsAndDoSomeTransactions();
        assertEquals(rightFormatForHistoryOfSpecificAccount(),
                customerService.checkHistoryOfSpecificAccount(0));

        File file = new File("src/main/resources/", "customerReportHistoryOfAccount.txt");
        StringBuffer resultFromFile = new StringBuffer();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                resultFromFile.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(rightFormatForHistoryOfSpecificAccount(),resultFromFile.toString());
    }

    @Test
    void testAddingInterestToSavingAcc() throws Exception{
        customerService.requestForNewAccount(2);
        employeeService.acceptRequestsForAccounts(true);
        assertTrue(currentCustomer.getAccounts().get(0) instanceof SavingAccount);
        TimeUnit.MINUTES.sleep(1);
        Bank.calculateInterestsOfCustomerAccs(currentCustomer);

        assertEquals(BigDecimal.valueOf(1010.00).setScale(2, RoundingMode.DOWN),
                currentCustomer.getAccounts().get(0).getAmountOfMoney());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                ((SavingAccount)currentCustomer.getAccounts().get(0)).getTimeOfLastInterestAdd());
    }

    @Test
    void testAddingInterestToSavingAccForLongTime() throws Exception{
        customerService.requestForNewAccount(2);
        employeeService.acceptRequestsForAccounts(true);
        //We set false date, minus 3 days and 5 hours for current saving acc. General - difference is 77 hours.
        setFalseDateToSavingAcc();
        BigDecimal expectedAmountOfMoney = BigDecimal.valueOf(1000 + (1000*0.01*77*60));
        Bank.calculateInterestsOfCustomerAccs(currentCustomer);
        assertEquals(expectedAmountOfMoney,currentCustomer.getAccounts().get(0).getAmountOfMoney());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                ((SavingAccount)currentCustomer.getAccounts().get(0)).getTimeOfLastInterestAdd());
    }
    Customer createSecondCustomerAndHisAccounts() throws Exception {
        Customer secondCustomer = employeeService.createNewCustomer("login2", "password2",
                "name2","lastName2", LocalDate.of(1996, 8, 24),
                Country.POLAND);
        CustomerService secondCustomerService = new CustomerService("login2", "password2");
        secondCustomerService.requestForNewAccount(1);
        secondCustomerService.requestForNewAccount(2);
        secondCustomerService.requestForNewAccount(3);
        employeeService.acceptRequestsForAccounts(true);
        employeeService.acceptRequestsForAccounts(true);
        employeeService.acceptRequestsForAccounts(true);
        return secondCustomer;
    }

    private void employeeAcceptsAllAccounts() throws Exception {
        EmployeeService employeeService = new EmployeeService("loginEmployee", "password");
        while (Bank.getRequestsForAccount().size() != 0) {
            employeeService.acceptRequestsForAccounts(true);
        }
    }

    private String rightInfoFormatForCheckAllAccounts() {
        return "0: Regular:" + currentCustomer.getAccounts().get(0).getNumber() + "  balance:1000\n" +
                "1: Saving:" + currentCustomer.getAccounts().get(1).getNumber() + "  balance:1000\n" +
                "2: International:" + currentCustomer.getAccounts().get(2).getNumber() + "  balance:1000\n" +
                "3: Regular:" + currentCustomer.getAccounts().get(3).getNumber() + "  balance:1000\n";
    }


    private void createFirstCustmAccountsAndDoSomeTransactions() throws Exception {
        Customer customer1 = Bank.getCustomers().get(0);
        Customer customer2 = Bank.getCustomers().get(1);
        CustomerService customerService1 = new CustomerService(customer1.getLogin(),
                new String(customer1.getPassword()));
        CustomerService customerService2 = new CustomerService(customer2.getLogin(),
                new String(customer2.getPassword()));
        //customer1 has 3 accounts: 1 regular, 1 saving and 1 international
        customerService1.requestForNewAccount(1);
        customerService1.requestForNewAccount(2);
        customerService1.requestForNewAccount(3);
        while (!Bank.getRequestsForAccount().isEmpty()) {
            employeeService.acceptRequestsForAccounts(true);
        }
        //Such transactions of customer1:
        //RegularAcc1 -> 450.79$ -> SecondCustomerRegularAcc1
        customerService1.creditFromRegularAcc(0,
                customer2.getAccounts().get(0).getNumber(), BigDecimal.valueOf(450.79123));
        //RegularAcc1 -> 250$ -> SavingAcc1
        customerService1.creditFromRegularAcc(0,
                customer1.getAccounts().get(1).getNumber(), BigDecimal.valueOf(250));
        //InternationalAcc1 -> 550$ -> SecondCustomerInternationalAcc1
        customerService1.wireFromInternational(2,
                customer2.getAccounts().get(2).getNumber(), BigDecimal.valueOf(550));
        //SavingAcc1 -> 1150$ -> RegularAcc1
        customerService1.creditFromSavingAcc(1,
                customer1.getAccounts().get(0).getNumber(), BigDecimal.valueOf(1150));
        //Customer2 sends money from his only regular Acc to regularAcc1 of first customer
        customerService2.creditFromRegularAcc(0,
                customer1.getAccounts().get(0).getNumber(), BigDecimal.valueOf(350));

    }

    private String rightFormatForHistoryOfSpecificAccount(){
        String checkedAccountNumber = currentCustomer.getAccounts().get(0).getNumber();
        String firstCustSavingAccount = currentCustomer.getAccounts().get(1).getNumber();
        String secondCustRegularAccount = Bank.getCustomers().get(1).getAccounts().get(0).getNumber();
        return "Customer: Bob Dylan\n" +
                "history of Account:"+checkedAccountNumber+":\n" +
                "Local Send Transaction id= 0, at date:2019-06-13:\n" +
                "AccountFrom: "+checkedAccountNumber+", amount of money sent: 450.79; to Account:"+
                secondCustRegularAccount+"\n" +
                "Local Send Transaction id= 1, at date:2019-06-13:\n" +
                "AccountFrom: "+checkedAccountNumber+", amount of money sent: 250.00; to Account:"+
                firstCustSavingAccount+"\n" +
                "Receive Transaction id= 0, at date:2019-06-13:\n" +
                "ToAccount: "+checkedAccountNumber+", amount of money sent: 1150.00; from account:"+
                firstCustSavingAccount+"\n" +
                "Receive Transaction id= 1, at date:2019-06-13:\n" +
                "ToAccount: "+checkedAccountNumber+", amount of money sent: 350.00; from account:"+
                secondCustRegularAccount+"\n";
    }

    private void setFalseDateToSavingAcc(){
        LocalDateTime falseDateTimeForAcc = LocalDateTime.now().minusDays(3);
        falseDateTimeForAcc = falseDateTimeForAcc.minusHours(5);
        falseDateTimeForAcc = falseDateTimeForAcc.truncatedTo(ChronoUnit.MINUTES);
        ((SavingAccount)currentCustomer.getAccounts().get(0)).setTimeOfLastInterestAdd(falseDateTimeForAcc);
    }
}
