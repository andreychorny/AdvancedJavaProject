package BankOne.com.BankData;

import BankOne.com.accounts.Account;

import java.util.ArrayList;
import java.util.List;

public class Bank {

    static List<Customer> customers = new ArrayList<>();

    void checkAllTransactions() {

    }

    void checkTransactionOfCustomer() {

    }

    void createNewCustomer() {

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
