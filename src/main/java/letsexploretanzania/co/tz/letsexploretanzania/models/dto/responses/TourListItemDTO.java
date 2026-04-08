package letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses;

public record TourListItemDTO(
        Long id,
        String title,
        String overView,
        int durationDays,
        String bannerImageUrl,
        String tourType,
        boolean isLive
) {
}
