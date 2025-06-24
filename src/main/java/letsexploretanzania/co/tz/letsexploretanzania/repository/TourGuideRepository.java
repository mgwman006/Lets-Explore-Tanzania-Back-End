package letsexploretanzania.co.tz.letsexploretanzania.repository;

import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourGuideRepository extends JpaRepository<TourGuide,Long> {
}
