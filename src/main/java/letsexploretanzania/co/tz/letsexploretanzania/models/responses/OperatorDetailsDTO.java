package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

public record OperatorDetailsDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        int numberOfBookings,
        int numberOfTours
) {
}
