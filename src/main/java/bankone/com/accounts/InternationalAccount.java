package bankone.com.accounts;

import bankone.com.bankdata.BankUtil;
import bankone.com.bankdata.Customer;
import bankone.com.transactionshistory.InternationalOutTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InternationalAccount extends Account {

    private String iban;

    public InternationalAccount(BigDecimal amountOfMoney, String number, Customer ownerOfAccount, String iban) {
        super(amountOfMoney, number, ownerOfAccount);
        this.iban = generateIban(iban);
    }

    public void wire(Account toWhichAccount, BigDecimal howMuch) throws IllegalArgumentException {
        if (getAmountOfMoney().compareTo(howMuch) >= 0) {
            setAmountOfMoney(getAmountOfMoney().subtract(howMuch));
            LocalDate dateOfTransaction = LocalDate.now();
            createNewMemento(dateOfTransaction);
            writeWireToCustomerHistory(howMuch, toWhichAccount);
            toWhichAccount.debit(howMuch, this.getNumber());
        } else throw new IllegalArgumentException("NotEnoughMoney");
    }

    void writeWireToCustomerHistory(BigDecimal howMuch, Account toWhichAccount) {
        int amountOfTransactionsOfCustomer = getOwnerOfAccount().getHistory().size();
        getOwnerOfAccount().getHistory().put(amountOfTransactionsOfCustomer,
                createNewInternationalOutTransaction(howMuch, toWhichAccount));
        getOwnerOfAccount().setInternationalIdCount(getOwnerOfAccount().getInternationalIdCount() + 1);
    }

    private InternationalOutTransaction createNewInternationalOutTransaction(BigDecimal howMuch,
                                                                             Account toWhichAccount) {
        return new InternationalOutTransaction(getOwnerOfAccount().getInternationalIdCount(),
                howMuch, this, toWhichAccount.getNumber(), iban);
    }

    private String generateIban(String countryibanCode) {
        StringBuilder generatediban = new StringBuilder();
        generatediban.append(countryibanCode + "-");
        for (int i = 0; i < 8; i++) {
            generatediban.append((int) (Math.random() * 10));
        }
        if (BankUtil.checkIfIbanIsUnique(generatediban.toString())) {
            return generatediban.toString();
        }
        return generateIban(countryibanCode);
    }

    public String getIban() {
        return iban;
    }
}
