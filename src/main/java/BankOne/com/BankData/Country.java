package BankOne.com.BankData;

public enum Country {

    UKRAINE("UA85-3996-2200-0000"),
    POLAND("PL43-9581-3590-3344"),
    ENGLAND("EN75-3257-1212-0000"),
    AMERICA("US11-3323-8798-6675"),
    GERMANY("GR93-9397-7321-3410");

    private final String countryIBAN;

    private Country(String countryIBAN) {
        this.countryIBAN = countryIBAN;
    }

    public String getCountryIBAN() {
        return countryIBAN;
    }
}
