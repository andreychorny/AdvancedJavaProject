package BankOne;

import BankOne.com.BankData.Bank;
import BankOne.com.BankData.Country;
import BankOne.com.BankData.Customer;
import BankOne.com.TransactionsHistory.LocalSendTransaction;
import BankOne.com.accounts.Account;
import BankOne.com.services.CustomerService;
import BankOne.com.services.EmployeeService;
import BankOne.com.services.ReportsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReportsServiceJUnit {

    ReportsService reportsService;

    @BeforeEach
    void createAllDataToPresent() throws Exception {
        EmployeeService employeeService = createEmployeeAndHisService();
        createTwoCustomers(employeeService);
        createAccountsAndDoSomeTransactions(employeeService);
        reportsService = new ReportsService();
    }

    @AfterEach
    void cleanseAllData() {
        Bank.getCustomers().clear();
        Bank.getEmployees().clear();
        Bank.getRequestsForAccount().clear();
    }

    @Test
    void testGenerateReportAboutCustomer() {
        reportsService.generateReportTransactionOfCustomer(Bank.getCustomers().get(0));
        File file = new File("src/main/resources/", "transOfCustomer.txt");
        StringBuffer resultFromFile = new StringBuffer();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                resultFromFile.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(rightFormatOfTransactionsOfCustomerOne(Bank.getCustomers().get(0), Bank.getCustomers().get(1)),
                resultFromFile.toString());
    }

    @Test
    void testGenerateReportTransactionForSpecificType() {
        assertEquals(rightFormatForReportOutputAboutSpecificTypeTrancs(Bank.getCustomers().get(0),
                Bank.getCustomers().get(1)),
                reportsService.generateReportTransactionForSpecificType(LocalSendTransaction.class));
    }


    EmployeeService createEmployeeAndHisService() throws Exception {
        Bank.createNewEmployee("ouroboros", "superqwerty",
                "Andrii", "Chornyi");
        return new EmployeeService("ouroboros", "superqwerty");
    }

    void createTwoCustomers(EmployeeService employeeService) throws Exception {
        employeeService.createNewCustomer("login1", "password1", "name1", "lastName1",
                LocalDate.of(1995, 8, 11), Country.UKRAINE);
        employeeService.createNewCustomer("login2", "password2", "name2", "lastName2",
                LocalDate.of(1999, 12, 31), Country.POLAND);
    }

    void createAccountsAndDoSomeTransactions(EmployeeService employeeService) throws Exception {
        Customer customer1 = Bank.getCustomers().get(0);
        Customer customer2 = Bank.getCustomers().get(1);
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
        customerService1.wireFromInternational(3,
                customer2.getAccounts().get(1).getNumber(), BigDecimal.valueOf(550));
        //SavingAcc1 -> 1150$ -> RegularAcc2
        customerService1.creditFromSavingAcc(1,
                customer1.getAccounts().get(2).getNumber(), BigDecimal.valueOf(1150));
        //Customer2 sends money from his only regular Acc to his only international Acc
        customerService2.creditFromRegularAcc(0,
                customer2.getAccounts().get(1).getNumber(), BigDecimal.valueOf(350));

    }

    private String rightFormatOfTransactionsOfCustomerOne(Customer customer1, Customer customer2) {
        List<Account> accountsCust1 = customer1.getAccounts();
        List<Account> accountsCust2 = customer2.getAccounts();
        String regulAcc1 = accountsCust1.get(0).getNumber();
        String savingAcc1 = accountsCust1.get(1).getNumber();
        String interAcc1 = accountsCust1.get(3).getNumber();
        String regulAcc2 = accountsCust1.get(2).getNumber();
        String secCustInterAcc1 = accountsCust2.get(1).getNumber();

        String secondCustRegulAcc1 = accountsCust2.get(0).getNumber();
        LocalDate dateToCheck = LocalDate.now();
        String dateOfTransactions = dateToCheck.format(DateTimeFormatter.ISO_DATE);
        return "REPORT ABOUT TRANSACTIONS OF CUSTOMER: name1 lastName1: \n" +
                "Local Send Transaction id= 0, at date:" + dateOfTransactions + ":\n" +
                "AccountFrom: " + regulAcc1 + ", amount of money sent: 450.79; to Account:"
                + secondCustRegulAcc1 + "\n" +
                "Local Send Transaction id= 1, at date:" + dateOfTransactions + ":\n" +
                "AccountFrom: " + regulAcc1 + ", amount of money sent: 250.00; to Account:" +
                savingAcc1 + "\n" +
                "Receive Transaction id= 0, at date:" + dateOfTransactions + ":\n" +
                "ToAccount: " + savingAcc1 + ", amount of money sent: 250.00; from account:" +
                regulAcc1 + "\n" +
                "International Out Transaction id= 0, at date:" + dateOfTransactions + ":\n" +
                "AccountFrom: " + interAcc1 + ", amount of money sent: 550.00; to Account:" + secCustInterAcc1 + "\n" +
                "Local Send Transaction id= 0, at date:" + dateOfTransactions + ":\n" +
                "AccountFrom: " + savingAcc1 + ", amount of money sent: 1150.00; to Account:" + regulAcc2 + "\n" +
                "Receive Transaction id= 0, at date:" + dateOfTransactions + ":\n" +
                "ToAccount: " + regulAcc2 + ", amount of money sent: 1150.00; from account:" + savingAcc1 + "\n";
    }

    private String rightFormatForReportOutputAboutSpecificTypeTrancs(Customer customer1, Customer customer2) {
        List<Account> accountsCust1 = customer1.getAccounts();
        List<Account> accountsCust2 = customer2.getAccounts();
        String regulAcc1 = accountsCust1.get(0).getNumber();
        String savingAcc1 = accountsCust1.get(1).getNumber();
        String regulAcc2 = accountsCust1.get(2).getNumber();
        String secCustInterAcc1 = accountsCust2.get(1).getNumber();

        String secondCustRegulAcc1 = accountsCust2.get(0).getNumber();
        LocalDate dateToCheck = LocalDate.now();
        String dateOfTransactions = dateToCheck.format(DateTimeFormatter.ISO_DATE);
        return "REPORT ABOUT TRANSACTIONS OF TYPE: LocalSendTransaction\n" +
                "Customer: name1 lastName1: \n" +
                "Local Send Transaction id= 0, at date:" + dateOfTransactions + ":\n" +
                "AccountFrom: " + regulAcc1 + ", amount of money sent: 450.79; to Account:"
                + secondCustRegulAcc1 + "\n" +
                "Local Send Transaction id= 1, at date:" + dateOfTransactions + ":\n" +
                "AccountFrom: " + regulAcc1 + ", amount of money sent: 250.00; to Account:" +
                savingAcc1 + "\n" +
                "Local Send Transaction id= 0, at date:" + dateOfTransactions + ":\n" +
                "AccountFrom: " + savingAcc1 + ", amount of money sent: 1150.00; to Account:" + regulAcc2 + "\n" +
                "Customer: name2 lastName2: \n" +
                "Local Send Transaction id= 0, at date:" + dateOfTransactions + ":\n" +
                "AccountFrom: "+ secondCustRegulAcc1 +", amount of money sent: 350.00; to Account:"+secCustInterAcc1+
                "\n";
    }
}
