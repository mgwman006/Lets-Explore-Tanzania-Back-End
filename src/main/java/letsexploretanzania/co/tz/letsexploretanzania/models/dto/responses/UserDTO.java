package letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses;

public record UserDTO(
        Long id,
        String email,
        String password,
        String userType
) {
}
