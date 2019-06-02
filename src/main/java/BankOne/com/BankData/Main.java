package BankOne.com.BankData;

public class Main {

    public static void main(String... args) throws Exception{
        Bank.createNewCustomer();
        customerOutput(0);
        Bank.customers.get(0).createNewAccount();
        Bank.customers.get(0).createNewAccount();

        Bank.createNewCustomer();
        customerOutput(1);
        Bank.customers.get(1).createNewAccount();
        Bank.outputAllAccounts();

        Bank.customers.get(0).makeCredit();
        Bank.outputAllAccounts();

        Bank.customers.get(0).getHistory();

    }

    public static void customerOutput(int id){
        Customer customer = Bank.customers.get(id);
        System.out.println(customer.getFirstName() +" "+customer.getLastName());
        System.out.println(customer.getDateOfBirth());
        System.out.println(customer.getDateOfJoiningToBank());
    }
}
