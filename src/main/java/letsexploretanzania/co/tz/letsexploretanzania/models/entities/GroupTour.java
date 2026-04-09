package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import io.apimatic.core.types.BaseModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
@DiscriminatorValue("GROUP")
public class GroupTour extends Tour {

    private Integer maxGroupSize;
    private Double pricePerPerson;
    private boolean requiresMinimumParticipants;
    private Integer minimumParticipants;

    public Integer getMaxGroupSize() {
        return maxGroupSize;
    }

    public void setMaxGroupSize(Integer maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public Double getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(Double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

    public boolean isRequiresMinimumParticipants() {
        return requiresMinimumParticipants;
    }

    public void setRequiresMinimumParticipants(boolean requiresMinimumParticipants) {
        this.requiresMinimumParticipants = requiresMinimumParticipants;
    }

    public Integer getMinimumParticipants() {
        return minimumParticipants;
    }

    public void setMinimumParticipants(Integer minimumParticipants) {
        this.minimumParticipants = minimumParticipants;
    }
}