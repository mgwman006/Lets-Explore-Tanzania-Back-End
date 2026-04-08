package letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses;

public record CreatedOperatorDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone
) {
}
