package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

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
