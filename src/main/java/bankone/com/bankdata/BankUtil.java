package bankone.com.bankdata;

import bankone.com.accounts.Account;
import bankone.com.accounts.InternationalAccount;

import java.util.Arrays;

public class BankUtil {

    public static boolean nameValidationCorrect(String name) {
        if (name.length() < 2) {
            return false;
        }
        return name.matches("[A-Za-z]*");
    }

    public static boolean logAndPassValidationCorrect(String login) {
        if (login.length() < 6) {
            return false;
        }
        return login.matches("[A-Za-z0-9_]*");
    }

    public static boolean checkIfIBANIsUnique(String IBANToCheck) {
        for (Customer customer : Bank.getInstance().getCustomers()) {
            for (Account account : customer.getAccounts()) {
                if ((account instanceof InternationalAccount) &&
                        ((InternationalAccount) account).getIBAN().equals(IBANToCheck)) return false;
            }
        }
        return true;
    }

    public static boolean checkIfAccNumberUnique(String number) {
        for (Customer customer : Bank.getInstance().getCustomers()) {
            for (Account account : customer.getAccounts()) {
                if (account.getNumber().equals(number)) return false;
            }
        }
        return true;
    }

    public static boolean checkIfLoginUnique(String login) {
        for (Customer customer : Bank.getInstance().getCustomers()) {
            if (customer.getLogin().equals(login)) return false;
        }
        for (Employee employee : Bank.getInstance().getEmployees()) {
            if (employee.getLogin().equals(login)) return false;
        }
        return true;
    }

    public static <T extends Person> boolean checkIfLoggingInfoIsSuitable(String login,
                                                                          String passwordString) {
        char[] password = passwordString.toCharArray();
        for (Customer customer : Bank.getInstance().getCustomers()) {
            if (customer.getLogin().equals(login) && Arrays.equals(customer.getPassword(), password)) {
                return true;
            }
        }
        for (Employee employee : Bank.getInstance().getEmployees()) {
            if (employee.getLogin().equals(login) && Arrays.equals(employee.getPassword(), password)) {
                return true;
            }
        }
        return false;
    }

}
