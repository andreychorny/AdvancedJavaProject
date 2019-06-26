package bankone.com.bankdata;

import java.util.Arrays;
import java.util.Objects;

public class Employee extends Person {

    public Employee(String login, String password, String firstName, String lastName) {
        super(login, password, firstName, lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(getLogin(), employee.getLogin()) &&
                Arrays.equals(getPassword(), employee.getPassword()) &&
                Objects.equals(getFirstName(), employee.getFirstName()) &&
                Objects.equals(getLastName(), employee.getLastName());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
