package bankone.com.accounts;

import bankone.com.bankdata.Bank;
import bankone.com.bankdata.BankUtil;
import bankone.com.bankdata.Customer;
import bankone.com.transactionshistory.InternationalOutTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InternationalAccount extends Account {

    private String IBAN;

    public InternationalAccount(BigDecimal amountOfMoney, String number, Customer ownerOfAccount, String IBAN) {
        super(amountOfMoney, number, ownerOfAccount);
        this.IBAN = generateIBAN(IBAN);
    }

    public void wire(Account toWhichAccount, BigDecimal howMuch) throws IllegalArgumentException {
        if (getAmountOfMoney().compareTo(howMuch) >= 0) {
            setAmountOfMoney(getAmountOfMoney().subtract(howMuch));
            LocalDate dateOfTransaction = LocalDate.now();
            createNewMemento(dateOfTransaction);
            writeWireToCustomerHistory(dateOfTransaction, howMuch, toWhichAccount);
            toWhichAccount.debit(howMuch, this.getNumber());
        } else throw new IllegalArgumentException("NotEnoughMoney");
    }

    void writeWireToCustomerHistory(LocalDate dateOfTransaction, BigDecimal howMuch, Account toWhichAccount) {
        int amountOfTransactionsOfCustomer = getOwnerOfAccount().getHistory().size();
        getOwnerOfAccount().getHistory().put(amountOfTransactionsOfCustomer,
                createNewInternationalOutTransaction(dateOfTransaction, howMuch, toWhichAccount));
        getOwnerOfAccount().setInternationalIdCount(getOwnerOfAccount().getInternationalIdCount() + 1);
    }

    private InternationalOutTransaction createNewInternationalOutTransaction(LocalDate dateOfTransaction,
                                                                             BigDecimal howMuch, Account toWhichAccount) {
        return new InternationalOutTransaction(getOwnerOfAccount().getInternationalIdCount(),
                howMuch, this, toWhichAccount.getNumber(), IBAN);
    }

    private String generateIBAN(String countryIBANCode) {
        StringBuffer generatedIBAN = new StringBuffer();
        generatedIBAN.append(countryIBANCode + "-");
        for (int i = 0; i < 8; i++) {
            generatedIBAN.append((int) (Math.random() * 10));
        }
        if (BankUtil.checkIfIBANIsUnique(generatedIBAN.toString())) {
            return generatedIBAN.toString();
        }
        return generateIBAN(countryIBANCode);
    }

    public String getIBAN() {
        return IBAN;
    }
}
