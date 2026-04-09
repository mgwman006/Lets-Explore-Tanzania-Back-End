package letsexploretanzania.co.tz.letsexploretanzania.controller;

import jakarta.validation.Valid;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests.AddOperatorDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests.privatetour.PrivateTourAddDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.booking.BookingListItemDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.CreatedOperatorDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.TourListItemDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.privatetour.PrivateTourCreatedDto;
import letsexploretanzania.co.tz.letsexploretanzania.service.PrivateTourService;
import letsexploretanzania.co.tz.letsexploretanzania.service.TourOperatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/operator")
public class TourOperatorController {

    private final TourOperatorService tourOperatorService;


    public TourOperatorController(TourOperatorService tourOperatorService)
    {
        this.tourOperatorService = tourOperatorService;
    }

    @GetMapping(path = "/{operatorId}/tours")
    public ResponseEntity<ApiResponse<List<TourListItemDTO>>> getTours(
            @PathVariable Long operatorId
    )
    {
        Result<List<TourListItemDTO>> result = tourOperatorService.getTours(operatorId);
        if (result.isSuccess()) {
            return  ResponseEntity.ok().body(
                    ApiResponse.success(
                            result.getData(),
                            HttpStatus.OK.value()
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

    @PostMapping(path = "/{operatorId}/tour/private", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PrivateTourCreatedDto>> createPrivateTour(
            @PathVariable Long operatorId,
            @Valid
            @RequestPart("metadata") PrivateTourAddDto tourRequest,
            @RequestPart("bannerImage") MultipartFile photo
    ) throws IOException
    {

        Result<PrivateTourCreatedDto> result = tourOperatorService.addPrivateTour(
                operatorId,
                tourRequest,
                photo
        );

        if (result.isSuccess()) {
            URI location = URI.create("operator/created");
            return  ResponseEntity.created(location).body(
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

    @PostMapping(path = "/{operatorId}/public/tour")
    public ResponseEntity<ApiResponse<CreatedOperatorDto>> createPublicTour(
            @PathVariable Long operatorId,
            @Valid
            @RequestBody AddOperatorDto tourOperator
    )
    {
        return null;
    }

    @GetMapping(path = "/{operatorId}/bookings")
    public ResponseEntity<ApiResponse<List<BookingListItemDTO>>> getBookings(
      @PathVariable Long operatorId
    )
    {
        Result<List<BookingListItemDTO>> result = tourOperatorService.getBookings(operatorId);
        if (result.isSuccess())
        {
            return ResponseEntity.ok().body(
              ApiResponse.success(
                result.getData(),
                HttpStatus.OK.value()
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
