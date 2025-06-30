package letsexploretanzania.co.tz.letsexploretanzania.repository;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TourDestinationEnum;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourDestination;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TouristRepository extends JpaRepository<Tourist, Long> {
    boolean existsByEmail(String email);
    Optional<Tourist> findByEmail(String email);

}
