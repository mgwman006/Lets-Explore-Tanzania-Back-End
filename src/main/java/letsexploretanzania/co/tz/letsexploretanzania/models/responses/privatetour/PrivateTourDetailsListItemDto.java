package letsexploretanzania.co.tz.letsexploretanzania.models.responses.privatetour;

import letsexploretanzania.co.tz.letsexploretanzania.models.responses.TourDateDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.TourPriceDTO;

import java.util.List;

public record PrivateTourDetailsListItemDto(
        Long id,
        String title,
        String overView,
        int durationDays,
        String bannerImageUrl,
        List<String> destinations,
        List<TourPriceDTO> tourPrice
) {
}
