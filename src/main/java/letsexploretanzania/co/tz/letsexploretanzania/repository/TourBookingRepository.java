package letsexploretanzania.co.tz.letsexploretanzania.repository;

import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TourBookingRepository extends JpaRepository<TourBooking,Long> {
  Optional<TourBooking> findByReferenceNumber(String tourRefenceNumber);
}
