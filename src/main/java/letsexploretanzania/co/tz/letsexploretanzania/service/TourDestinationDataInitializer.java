package letsexploretanzania.co.tz.letsexploretanzania.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TourDestinationEnum;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourDestination;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourDestinationRepository;
import org.springframework.stereotype.Service;

@Service
public class TourDestinationDataInitializer {
    private final TourDestinationRepository tourDestinationRepository;

    public TourDestinationDataInitializer(TourDestinationRepository tourDestinationRepository) {
        this.tourDestinationRepository = tourDestinationRepository;
    }

    @PostConstruct
    @Transactional
    public void initDestinations() {
        for (TourDestinationEnum destinationEnum : TourDestinationEnum.values()) {
                if (!tourDestinationRepository.existsByName(destinationEnum)) {
                    tourDestinationRepository.save(new TourDestination(destinationEnum));
                }
        }
    }

}
