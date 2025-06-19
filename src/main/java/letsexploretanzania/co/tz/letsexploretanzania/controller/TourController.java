package letsexploretanzania.co.tz.letsexploretanzania.controller;

import jakarta.validation.Valid;
import letsexploretanzania.co.tz.letsexploretanzania.common.dtos.DeleteResponseDto;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.TourAddDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.TourCreatedDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.TourDetailsDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.TourDetailsListItemDto;
import letsexploretanzania.co.tz.letsexploretanzania.service.TourService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tour")
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TourDetailsDto>> addTour(
            @Valid
            @RequestBody TourAddDto tourRequest
            )
    {
        Result<TourDetailsDto> result = tourService.addTour(tourRequest);
        if (result.isSuccess())
        {
            URI uri = URI.create("/api/v1/tour");
            return ResponseEntity
                    .created(
                        uri
                    ).body(
                            ApiResponse.success(
                                    result.getMessage(),
                                    result.getData(),
                                    HttpStatus.CREATED.value()
                            )
                    );
        }

        return ResponseEntity.badRequest().body(ApiResponse.failure(result.getMessage(), HttpStatus.BAD_REQUEST.value()));

    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TourDetailsListItemDto>>> getTours()
    {
        Result<List<TourDetailsListItemDto>> result = tourService.getAllTours();

        if (result.isSuccess())
        {
            return ResponseEntity.ok(
                    ApiResponse.success(result.getData(),HttpStatus.OK.value())
            );
        }
        return ResponseEntity.badRequest().body(ApiResponse.failure(result.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @GetMapping("/{tourId}")
    public ResponseEntity<ApiResponse<TourDetailsDto>> getTourById(
            @PathVariable
            Long tourId
    )
    {
        Result<TourDetailsDto> result = tourService.getTourById(tourId);
        if (result.isSuccess())
        {
            return ResponseEntity.ok(ApiResponse.success(result.getData(),HttpStatus.OK.value()));
        }
        return ResponseEntity.badRequest().body(ApiResponse.failure(result.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
    @DeleteMapping("/{tourId}")
    public ResponseEntity<ApiResponse<DeleteResponseDto>> deleteTourById(
            @PathVariable
            Long tourId
    )
    {
        Result<DeleteResponseDto> result = tourService.deleteTourById(tourId);
        if (result.isSuccess())
        {
            return ResponseEntity.ok(ApiResponse.success(result.getData(),HttpStatus.OK.value()));
        }
        return ResponseEntity.badRequest().body(ApiResponse.failure(result.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @PostMapping(path = "/{tourId}/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TourDetailsDto>> addBannerImage(
            @PathVariable
            Long tourId,
            @RequestParam("image")MultipartFile image
    )
    {
        Result<TourDetailsDto> result = tourService.addBannerImage(tourId,image);
        if (result.isSuccess())
        {
            return ResponseEntity
                    .ok(
                            ApiResponse.success(
                                    result.getMessage(),
                                    result.getData(),
                                    HttpStatus.OK.value()
                            )
                    );
        }

        return ResponseEntity.badRequest().body(ApiResponse.failure(result.getMessage(), HttpStatus.BAD_REQUEST.value()));

    }
    @PostMapping(path = "/{tourId}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TourDetailsDto>> addPhotos(
            @PathVariable
            Long tourId,
            @RequestParam("photos")List<MultipartFile> photos
    )
    {
        Result<TourDetailsDto> result = tourService.addPhotos(tourId,photos);
        if (result.isSuccess())
        {
            return ResponseEntity
                    .ok(
                            ApiResponse.success(
                                    result.getMessage(),
                                    result.getData(),
                                    HttpStatus.OK.value()
                            )
                    );
        }

        return ResponseEntity.badRequest().body(ApiResponse.failure(result.getMessage(), HttpStatus.BAD_REQUEST.value()));

    }

}
