package letsexploretanzania.co.tz.letsexploretanzania.common.enums;

public enum TourDestinationEnum {
    ZANZIBAR(1,"Zanzibar"),
    TARANGIRE(2,"Tarangire"),
    MIKUMI(3,"Mikumi"),
    NGORONGORO(4,"Ngorongoro"),
    ARUSHA(5,"Arusha"),
    RUAHA(6,"Ruaha"),
    SELOUS(7,"Selous"),
    NYERERE(8,"Nyerere"),
    SAADANI(9,"Saadani"),
    KILIMANJARO(10,"Kilimanjaro");

    private int code;
    private String name;

    TourDestinationEnum(int code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
