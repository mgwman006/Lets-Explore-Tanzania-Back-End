package letsexploretanzania.co.tz.letsexploretanzania.repository;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TourDestinationEnum;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourDestination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TourDestinationRepository extends JpaRepository<TourDestination, Long> {
    boolean existsByName(TourDestinationEnum name);
    Optional<TourDestination> findByName(TourDestinationEnum name);

}
