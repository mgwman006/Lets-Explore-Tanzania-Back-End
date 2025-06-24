package letsexploretanzania.co.tz.letsexploretanzania.common.enums;

public enum TourType {
    PUBLIC,
    PRIVATE;


    @Override
    public String toString() {
        return name().toLowerCase(); // returns "public" or "private"
    }

}
