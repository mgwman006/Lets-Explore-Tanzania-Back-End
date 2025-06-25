package letsexploretanzania.co.tz.letsexploretanzania.models.responses.privatetour;

import java.util.List;

public record PrivateTourCreatedDto(
        Long id,
        Long tourGuideId,
        String title,
        String overView,
        int durationDays,
        String bannerImageUrl,
        List<String> destinations

) {
}
