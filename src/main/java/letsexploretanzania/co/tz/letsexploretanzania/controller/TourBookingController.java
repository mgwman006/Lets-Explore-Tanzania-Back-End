package letsexploretanzania.co.tz.letsexploretanzania.controller;

import jakarta.validation.Valid;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.BookingAddDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.BookingCreatedDTO;
import letsexploretanzania.co.tz.letsexploretanzania.service.TourBookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(path = "api/v1/tour/booking")
public class TourBookingController {
    private final TourBookingService tourBookingService;

    public TourBookingController(TourBookingService tourBookingService) {
        this.tourBookingService = tourBookingService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingCreatedDTO>> addBooking(
            @Valid
            @RequestBody
            BookingAddDTO bookingAddDTO
    )
    {
        Result<BookingCreatedDTO> result = tourBookingService.addBooking(bookingAddDTO);
        if (result.isSuccess())
        {
            URI uri = URI.create("tour/booking/"+result.getData().id());
            return ResponseEntity.created(uri).body(
                    ApiResponse.success(
                            result.getData(),
                            HttpStatus.CREATED.value()
                    )
            );
        }
        return ResponseEntity.badRequest().body(
                ApiResponse.failure(
                        result.getMessage(),
                        HttpStatus.BAD_REQUEST.value()
                )
        );
    }
}
