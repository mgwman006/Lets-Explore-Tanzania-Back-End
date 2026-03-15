package letsexploretanzania.co.tz.letsexploretanzania.common.enums;

public enum BookingStatus
{
    NEW(1, "New", "New Trip"),
    PENDING_PAYMENT(2, "Pending Payment", "Booking created waiting for payment"),
    CONFIRMED(3, "Confirmed", "A customer already paid for the trip"),
    CANCELLED(4, "Cancelled", "A customer or an operator cancelled"),
    PAYMENT_FAILED(5, "Payment Failed", "A customer tried to pay the payment but error occured"),
    EXPIRED(6, "Cancelled", "Out of Tour Date");


    final int id;
    final String description;
    final String name;

    BookingStatus(int id, String name, String description)
    {
        this.id = id;
        this.description = description;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }
}
