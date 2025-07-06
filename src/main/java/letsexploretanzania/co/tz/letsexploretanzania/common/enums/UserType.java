package letsexploretanzania.co.tz.letsexploretanzania.common.enums;

public enum UserType {
    TOURIST("Tourist"),
    TOUROPERATOR("Tour Operator");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return this.name();
    }
}


