package letsexploretanzania.co.tz.letsexploretanzania.controller;

import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.Tour;
import letsexploretanzania.co.tz.letsexploretanzania.service.TourService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/tours")
public class TourController {
    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping(path = "/{tourId}/bookings")
    public ResponseEntity<ApiResponse<List<Tour>>> getBookings(@PathVariable Long tourId) {
        return null;
    }
}
