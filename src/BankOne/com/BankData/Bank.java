package BankOne.com.BankData;

import BankOne.com.accounts.Account;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Bank {

    static List<Customer> customers = new ArrayList<>();

    void checkAllTransactions() {

    }

    void checkTransactionOfCustomer() {

    }

    void createNewCustomer() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter first name of new customer");
        String firstName = in.nextLine();
        System.out.println("Enter last name of new customer");
        String lastName = in.nextLine();
        System.out.println("Enter year of birth");
        int yearOfBirth = in.nextInt();
        System.out.println("Enter month(number) of birth");
        int monthOfBirth = in.nextInt();
        System.out.println("Enter day of birth");
        int dayOfBirth = in.nextInt();
        customers.add(new Customer(firstName, lastName, new Date(yearOfBirth, monthOfBirth, dayOfBirth), new Date()));
    }

    static Account findAccount(String number){
        for(Customer customer : customers){
            for(Account account : customer.getAccounts()){
                if(account.getNumber().equals(number)) return account;
            }
        }
        return null;
    }
}
