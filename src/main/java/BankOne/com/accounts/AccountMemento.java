package BankOne.com.accounts;

import BankOne.com.BankData.Customer;

import java.math.BigDecimal;
import java.util.Date;

public class AccountMemento {

    private final BigDecimal amountOfMoney;

    private final Integer debitIdCount;

    private final String number;

    private final Date dateOfChange;

    public AccountMemento(BigDecimal amountOfMoney, int debitIdCount, String number, Date dateOfChange) {
        this.amountOfMoney = amountOfMoney;
        this.debitIdCount = debitIdCount;
        this.number = number;
        this.dateOfChange = dateOfChange;
    }
}
