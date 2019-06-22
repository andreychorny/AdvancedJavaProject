package bankone.com.bankdata;

public enum Country {

    UKRAINE("UA85"),
    POLAND("PL43"),
    ENGLAND("EN75"),
    AMERICA("US11"),
    GERMANY("GR93");

    private final String countryIBAN;

    private Country(String countryIBAN) {
        this.countryIBAN = countryIBAN;
    }

    public String getCountryIBAN() {
        return countryIBAN;
    }
}
