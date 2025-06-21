package letsexploretanzania.co.tz.letsexploretanzania.common.enums;

public enum CurrencyEnum {
    USD("$"),
    EUR("€"),
    TZS("TSh"),
    GBP("£");

    private final String symbol;

    CurrencyEnum(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCode() {
        return this.name();
    }
}

