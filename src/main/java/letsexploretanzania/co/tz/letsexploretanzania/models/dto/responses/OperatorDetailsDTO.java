package letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses;

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
