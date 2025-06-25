package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import jakarta.persistence.*;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TourDestinationEnum;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "destinations")
public class TourDestination {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private TourDestinationEnum name;

    public TourDestination() {
    }

    public TourDestination(TourDestinationEnum name) {
        this.name = name;
    }

    public TourDestinationEnum getName() {
        return name;
    }

    public void setName(TourDestinationEnum name) {
        this.name = name;
    }

}
