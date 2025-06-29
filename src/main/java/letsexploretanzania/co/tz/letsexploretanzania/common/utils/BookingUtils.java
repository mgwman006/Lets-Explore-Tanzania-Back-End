package letsexploretanzania.co.tz.letsexploretanzania.common.utils;

import java.util.UUID;

public class BookingUtils {


    public static String generateBookingReference()
    {
        // Generate a UUID and take the first 8 characters
        String uuidPart = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
        return "BK-" + uuidPart;
    }

     public static void main(String[] args)
     {
        System.out.println(generateBookingReference());
     }

}
