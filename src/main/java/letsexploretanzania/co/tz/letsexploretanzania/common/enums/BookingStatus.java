package letsexploretanzania.co.tz.letsexploretanzania.common.enums;

public enum BookingStatus {
    NEW(1,"New","Customer sent a request"),
    PENDING(2,"Pending","A Tour Operator Accepted a booking request waiting for payment"),
    CONFIRMED(3,"Confirmed", "A customer already paid for the trip"),
    CANCELLED(4,"Cancelled","A customer or an operator cancelled");

    int id;
    String description;
    String name;
    BookingStatus(int id, String name, String description)
    {
        this.id=id;
        this.description = description;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
