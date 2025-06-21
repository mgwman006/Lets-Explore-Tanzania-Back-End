package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import jakarta.persistence.*;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.CurrencyEnum;

import java.math.BigDecimal;

@Entity
@Table(name = "tourprices")
public class TourPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal pricePerPerson;
    @Column(length = 10)
    @Enumerated
    private CurrencyEnum currency;
    @ManyToOne
    @JoinColumn(name = "tour_id")
    Tour tour;

    public TourPrice() {
    }

    public TourPrice(int quantity, BigDecimal pricePerPerson, CurrencyEnum currency) {
        this.quantity = quantity;
        this.pricePerPerson = pricePerPerson;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(BigDecimal pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyEnum currency) {
        this.currency = currency;
    }
}

