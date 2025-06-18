package letsexploretanzania.co.tz.letsexploretanzania.repository;

import letsexploretanzania.co.tz.letsexploretanzania.models.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
