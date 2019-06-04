package BankOne.com.accounts;

import BankOne.com.BankData.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountMemento {

    private final String number;

    private final BigDecimal amountOfMoney;

    private final Integer debitIdCount;

    private final LocalDate dateOfChange;

    public AccountMemento(BigDecimal amountOfMoney, int debitIdCount, String number,
                          LocalDate dateOfChange) {
        this.amountOfMoney = amountOfMoney;
        this.debitIdCount = debitIdCount;
        this.number = number;
        this.dateOfChange = dateOfChange;
    }

    @Override
    public String toString() {
        return "AccountMemento{" +
                "number='" + number + '\'' +
                ", amountOfMoney=" + amountOfMoney +
                ", dateOfChange=" + dateOfChange +
                '}';
    }
}
