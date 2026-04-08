package letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.privatetour;

import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.TourPriceDTO;

import java.util.List;

public record PrivateTourDetailsDto(
        Long id,
        String title,
        String overView,
        int durationDays,
        String bannerImageUrl,
        boolean isLive,
        List<String> destinations,
        List<TourPriceDTO> tourPrice,
        List<String> photos,
        Long operatorId
) {
}
