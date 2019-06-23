package bankone;

import bankone.com.bankdata.Bank;
import bankone.com.bankdata.Country;
import bankone.com.bankdata.Customer;
import bankone.com.transactionshistory.LocalSendTransaction;
import bankone.com.accounts.Account;
import bankone.com.accounts.InternationalAccount;
import bankone.com.services.CustomerService;
import bankone.com.services.EmployeeService;
import bankone.com.services.ReportsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportsServiceJUnit {

    private ReportsService reportsService;
    private Bank bank;

    @BeforeEach
    void createAllDataToPresent() throws IllegalArgumentException {
        bank = Bank.getInstance();
        EmployeeService employeeService = createEmployeeAndHisService();
        createSixCustomers(employeeService);
        createAccountsAndDoSomeTransactions(employeeService);
        reportsService = new ReportsService();
    }

    @AfterEach
    void cleanseAllData() {
        bank.getCustomers().clear();
        bank.getEmployees().clear();
        bank.getRequestsForAccount().clear();
    }

    @Test
    void testGenerateReportAboutCustomer() {
        reportsService.generateReportTransactionOfCustomer(bank.getCustomers().get(0));
        File file = new File("src/main/resources/", "transactionsOfSpecificCustomer.txt");
        StringBuffer resultFromFile = new StringBuffer();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                resultFromFile.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(rightFormatOfTransactionsOfCustomerOne(bank.getCustomers().get(0), bank.getCustomers().get(1)),
                resultFromFile.toString());
    }

    @Test
    void testGenerateReportTransactionForSpecificType() {
        assertEquals(rightFormatForReportOutputAboutSpecificTypeTrancs(),
                reportsService.generateReportTransactionForSpecificType(LocalSendTransaction.class));
    }

    @Test
    void testReportTransactionForSpecificDate() {
        assertEquals(rightFormatForReportOfSpecificDate(),
                reportsService.generateReportTransactionForSpecificDate(LocalDate.now().minusDays(100)));
    }

    //Test without asserts, ask how to do test for reports
    @Test
    void testReportFiveLastCustomers() {
        reportsService.generateReportFiveLastCustomers();
    }

    //Test without asserts, ask how to do test for reports
    @Test
    void testReportTenBiggestTransactions(){
        reportsService.generateReportTenBiggestTransaction();
    }

    EmployeeService createEmployeeAndHisService() throws IllegalArgumentException {
        bank.createNewEmployee("ouroboros", "superqwerty",
                "Andrii", "Chornyi");
        return new EmployeeService("ouroboros", "superqwerty");
    }

    void createSixCustomers(EmployeeService employeeService) throws IllegalArgumentException {
        employeeService.createNewCustomer("login1", "password1", "nameOne",
                "lastNameOne", LocalDate.of(1995, 8, 11), Country.UKRAINE);
        employeeService.createNewCustomer("login2", "password2", "nameTwo",
                "lastNameTwo", LocalDate.of(1999, 12, 31), Country.POLAND);
        employeeService.createNewCustomer("AzorAhai", "TrueKing54", "Stannis",
                "Baratheon", LocalDate.of(1957, 11, 23), Country.AMERICA);
        employeeService.createNewCustomer("BobDylan", "wrumMmMm", "Bob",
                "Dylan", LocalDate.of(1988, 1, 2), Country.AMERICA);
        employeeService.createNewCustomer("ZZZcccZZZ", "AAaaaDDD", "AAddAD",
                "DASD", LocalDate.of(1975, 8, 15), Country.ENGLAND);
        employeeService.createNewCustomer("DishonoredKnight", "Vaserman", "Kyle",
                "Aranofski", LocalDate.of(1968, 5, 18), Country.GERMANY);
    }

    void createAccountsAndDoSomeTransactions(EmployeeService employeeService) throws IllegalArgumentException {
        Customer customer1 = bank.getCustomers().get(0);
        Customer customer2 = bank.getCustomers().get(1);
        CustomerService customerService1 = new CustomerService(customer1.getLogin(),
                new String(customer1.getPassword()));
        CustomerService customerService2 = new CustomerService(customer2.getLogin(),
                new String(customer2.getPassword()));
        //customer1 has 4 accounts: 2 regular, 1 saving and 1 international
        customerService1.requestForNewAccount(1);
        customerService1.requestForNewAccount(2);
        customerService1.requestForNewAccount(1);
        customerService1.requestForNewAccount(3);
        //customer2 has 2 accounts: 1 regular and 1 international
        customerService2.requestForNewAccount(1);
        customerService2.requestForNewAccount(3);
        while (!bank.getRequestsForAccount().isEmpty()) {
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
        customerService1.wireFromInternational(3,
                customer2.getAccounts().get(1).getNumber(), BigDecimal.valueOf(550));
        //SavingAcc1 -> 1150$ -> RegularAcc2
        customerService1.creditFromSavingAcc(1,
                customer1.getAccounts().get(2).getNumber(), BigDecimal.valueOf(1150));
        //Customer2 sends money from his only regular Acc to his only international Acc
        customerService2.creditFromRegularAcc(0,
                customer2.getAccounts().get(1).getNumber(), BigDecimal.valueOf(350));

        //change dates of several transactions for 'specific date' test
        customer1.getHistory().get(0).setDateOfTransaction(LocalDate.now().minusDays(100));
        customer2.getHistory().get(0).setDateOfTransaction(LocalDate.now().minusDays(100));
        customer1.getHistory().get(1).setDateOfTransaction(LocalDate.now().minusDays(75));
        customer1.getHistory().get(2).setDateOfTransaction(LocalDate.now().minusDays(75));
        customer1.getHistory().get(3).setDateOfTransaction(LocalDate.now().minusDays(35));
        customer2.getHistory().get(1).setDateOfTransaction(LocalDate.now().minusDays(35));

    }

    private String rightFormatOfTransactionsOfCustomerOne(Customer customer1, Customer customer2) {
        List<Account> accountsCust1 = customer1.getAccounts();
        List<Account> accountsCust2 = customer2.getAccounts();
        String regulAcc1 = accountsCust1.get(0).getNumber();
        String savingAcc1 = accountsCust1.get(1).getNumber();
        String interAcc1 = accountsCust1.get(3).getNumber();
        String interAcc1IBAN = ((InternationalAccount) accountsCust1.get(3)).getIBAN();
        String regulAcc2 = accountsCust1.get(2).getNumber();
        String secCustInterAcc1 = accountsCust2.get(1).getNumber();
        String secondCustRegulAcc1 = accountsCust2.get(0).getNumber();
        LocalDate dateToCheck = LocalDate.now();
        return "REPORT ABOUT TRANSACTIONS OF CUSTOMER: nameOne lastNameOne: \n" +
                "Local Send Transaction id= 0, at date:" + dateToCheck.minusDays(100) + ":\n" +
                "AccountFrom: " + regulAcc1 + ", amount of money sent: 450.79; to Account:"
                + secondCustRegulAcc1 + "\n" +
                "Local Send Transaction id= 1, at date:" + dateToCheck.minusDays(75) + ":\n" +
                "AccountFrom: " + regulAcc1 + ", amount of money sent: 250.00; to Account:" +
                savingAcc1 + "\n" +
                "Receive Transaction id= 0, at date:" + dateToCheck.minusDays(75) + ":\n" +
                "ToAccount: " + savingAcc1 + ", amount of money sent: 250.00; from account:" +
                regulAcc1 + "\n" +
                "International Out Transaction id= 0, at date:" + dateToCheck.minusDays(35) + ":\n" +
                "AccountFrom: " + interAcc1 + ", amount of money sent: 550.00; to Account:" + secCustInterAcc1 +
                "; IBAN code: " + interAcc1IBAN + "\n" +
                "Local Send Transaction id= 0, at date:" + dateToCheck + ":\n" +
                "AccountFrom: " + savingAcc1 + ", amount of money sent: 1150.00; to Account:" + regulAcc2 + "\n" +
                "Receive Transaction id= 0, at date:" + dateToCheck + ":\n" +
                "ToAccount: " + regulAcc2 + ", amount of money sent: 1150.00; from account:" + savingAcc1 + "\n";
    }

    private String rightFormatForReportOutputAboutSpecificTypeTrancs() {
        List<Account> accountsCust1 = bank.getCustomers().get(0).getAccounts();
        List<Account> accountsCust2 = bank.getCustomers().get(1).getAccounts();
        String regulAcc1 = accountsCust1.get(0).getNumber();
        String savingAcc1 = accountsCust1.get(1).getNumber();
        String regulAcc2 = accountsCust1.get(2).getNumber();
        String secCustInterAcc1 = accountsCust2.get(1).getNumber();
        String secondCustRegulAcc1 = accountsCust2.get(0).getNumber();
        LocalDate dateToCheck = LocalDate.now();
        return "REPORT ABOUT TRANSACTIONS OF TYPE: LocalSendTransaction\n" +
                "Customer: nameOne lastNameOne: \n" +
                "Local Send Transaction id= 0, at date:" + dateToCheck.minusDays(100) + ":\n" +
                "AccountFrom: " + regulAcc1 + ", amount of money sent: 450.79; to Account:"
                + secondCustRegulAcc1 + "\n" +
                "Local Send Transaction id= 1, at date:" + dateToCheck.minusDays(75) + ":\n" +
                "AccountFrom: " + regulAcc1 + ", amount of money sent: 250.00; to Account:" +
                savingAcc1 + "\n" +
                "Local Send Transaction id= 0, at date:" + dateToCheck + ":\n" +
                "AccountFrom: " + savingAcc1 + ", amount of money sent: 1150.00; to Account:" + regulAcc2 + "\n" +
                "Customer: nameTwo lastNameTwo: \n" +
                "Local Send Transaction id= 0, at date:" + dateToCheck + ":\n" +
                "AccountFrom: " + secondCustRegulAcc1 + ", amount of money sent: 350.00; to Account:" + secCustInterAcc1 +
                "\n";
    }

    private String rightFormatForReportOfSpecificDate() {
        List<Account> accountsCust1 = bank.getCustomers().get(0).getAccounts();
        List<Account> accountsCust2 = bank.getCustomers().get(1).getAccounts();
        String regulAcc1 = accountsCust1.get(0).getNumber();
        String secondCustRegulAcc1 = accountsCust2.get(0).getNumber();
        LocalDate date100DaysAgo = LocalDate.now().minusDays(100);
        return "REPORT ABOUT TRANSACTIONS OF DATE: " + date100DaysAgo + "\n" +
                "Customer: nameOne lastNameOne: \n" +
                "Local Send Transaction id= 0, at date:" + date100DaysAgo + ":\n" +
                "AccountFrom: " + regulAcc1 + ", amount of money sent: 450.79; to Account:" + secondCustRegulAcc1 + "\n" +
                "Customer: nameTwo lastNameTwo: \n" +
                "Receive Transaction id= 0, at date:" + date100DaysAgo + ":\n" +
                "ToAccount: " + secondCustRegulAcc1 + ", amount of money sent: 450.79; from account:" + regulAcc1 + "\n";
    }
}
