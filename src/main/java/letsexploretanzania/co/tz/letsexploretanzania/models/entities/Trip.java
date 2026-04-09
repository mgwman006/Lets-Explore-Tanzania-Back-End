package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import jakarta.persistence.*;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TripStatus;

import java.time.LocalDate;

@Entity
@Table(name = "trips")
public class Trip {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Tour tour;

  private LocalDate startDate;
  private LocalDate endDate;
  private Integer totalSpots;
  private Integer bookedSpots = 0;
  @Enumerated(EnumType.STRING)
  private TripStatus status;
}
