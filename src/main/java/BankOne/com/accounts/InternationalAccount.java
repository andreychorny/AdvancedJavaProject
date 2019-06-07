package BankOne.com.accounts;

import BankOne.com.BankData.Customer;
import BankOne.com.TransactionsHistory.InternationalOutTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class InternationalAccount extends Account {

    private String IBAN;

    public void wire(Account toWhichAccount, BigDecimal howMuch) throws Exception {
        if (getAmountOfMoney().compareTo(howMuch) >= 0) {
            setAmountOfMoney(getAmountOfMoney().subtract(howMuch));
            LocalDate dateOfTransaction = LocalDate.now();
            createNewMemento(dateOfTransaction);
            writeWireToCustomerHistory(dateOfTransaction, howMuch, toWhichAccount);
            toWhichAccount.debit(howMuch);
        } else throw new Exception("NotEnoughMoney");
    }

    void writeWireToCustomerHistory(LocalDate dateOfTransaction, BigDecimal howMuch, Account toWhichAccount) {
        getOwnerOfAccount().getHistory().put(getOwnerOfAccount().getLastTransactionsId(),
                createNewInternationalOutTransaction(dateOfTransaction, howMuch, toWhichAccount));
        getOwnerOfAccount().setInternationalIdCount(getOwnerOfAccount().getInternationalIdCount() + 1);
        getOwnerOfAccount().setLastTransactionsId(getOwnerOfAccount().getLastTransactionsId() + 1);
    }

    private InternationalOutTransaction createNewInternationalOutTransaction(LocalDate dateOfTransaction,
                                                                     BigDecimal howMuch, Account toWhichAccount) {
        return new InternationalOutTransaction(getOwnerOfAccount().getInternationalIdCount(),
                howMuch, this,
                toWhichAccount.getNumber(),IBAN);
    }

    public InternationalAccount(BigDecimal amountOfMoney, String number, Customer ownerOfAccount, String IBAN) {
        super(amountOfMoney, number, ownerOfAccount);
        this.IBAN = IBAN;
    }
}
