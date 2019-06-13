package BankOne.com.BankData;

import java.util.Arrays;
import java.util.Objects;

public abstract class Person {

    private String login;

    private char[] password;

    private String firstName;

    private String lastName;

    public Person(String login, String password, String firstName, String lastName) {
        this.login = login;
        this.password = new char[password.length()];
        for (int i = 0; i < password.length(); i++) {
            this.password[i] = password.charAt(i);
        }
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(getLogin(), person.getLogin()) &&
                Arrays.equals(getPassword(), person.getPassword()) &&
                Objects.equals(getFirstName(), person.getFirstName()) &&
                Objects.equals(getLastName(), person.getLastName());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getLogin(), getFirstName(), getLastName());
        result = 31 * result + Arrays.hashCode(getPassword());
        return result;
    }
}
