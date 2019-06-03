package BankOne.com.BankData;

public class Employee {

    private String login;

    private char[] password;

    private String firstName;

    private String lastName;

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

    public Employee(String login, String password, String firstName, String lastName) {
        this.login = login;
        this.password = new char[password.length()];
        for(int i=0; i<login.length();i++){
            this.password[i] = password.charAt(i);
        }
        this.firstName = firstName;
        this.lastName = lastName;
    }
}