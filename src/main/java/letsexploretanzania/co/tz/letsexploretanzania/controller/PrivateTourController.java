package letsexploretanzania.co.tz.letsexploretanzania.controller;

import jakarta.validation.Valid;
import letsexploretanzania.co.tz.letsexploretanzania.common.dtos.DeleteResponseDto;
import letsexploretanzania.co.tz.letsexploretanzania.common.dtos.MeetingPoint;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.AddTourPriceDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.TourActivityAddDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.privatetour.PrivateTourAddDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.*;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.privatetour.PrivateTourCreatedDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.privatetour.PrivateTourDetailsDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.privatetour.PrivateTourDetailsListItemDto;
import letsexploretanzania.co.tz.letsexploretanzania.service.PrivateTourService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/v1/tour/private")
public class PrivateTourController {

    private final PrivateTourService privateTourService;

    public PrivateTourController(PrivateTourService privateTourService) {
        this.privateTourService = privateTourService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PrivateTourCreatedDto>> addTour(
            @Valid
            @RequestPart("metadata") PrivateTourAddDto tourRequest,
            @RequestPart("bannerImage") MultipartFile photo
            ) throws IOException
    {

        Result<PrivateTourCreatedDto> result = privateTourService.addTour(tourRequest,photo);

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
    public ResponseEntity<ApiResponse<List<PrivateTourDetailsListItemDto>>> getTours()
    {
        Result<List<PrivateTourDetailsListItemDto>> result = privateTourService.getAllTours();

        if (result.isSuccess())
        {
            return ResponseEntity.ok(
                    ApiResponse.success(result.getData(),HttpStatus.OK.value())
            );
        }
        return ResponseEntity.badRequest().body(ApiResponse.failure(result.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @GetMapping("/{tourId}")
    public ResponseEntity<ApiResponse<PrivateTourDetailsDto>> getTourById(
            @PathVariable
            Long tourId
    )
    {
        Result<PrivateTourDetailsDto> result = privateTourService.getTourById(tourId);
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
        Result<DeleteResponseDto> result = privateTourService.deleteTourById(tourId);
        if (result.isSuccess())
        {
            return ResponseEntity.ok(ApiResponse.success(result.getData(),HttpStatus.OK.value()));
        }
        return ResponseEntity.badRequest().body(ApiResponse.failure(result.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @PostMapping(path = "/{tourId}/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> addBannerImage(
            @PathVariable
            Long tourId,
            @RequestParam("image")MultipartFile image
    )
    {
        Result<String> result = privateTourService.addBannerImage(tourId,image);
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
    public ResponseEntity<ApiResponse<List<String>>> addPhotos(
            @PathVariable
            Long tourId,
            @RequestParam("photos")List<MultipartFile> photos
    )
    {
        Result<List<String>> result = privateTourService.addPhotos(tourId,photos);
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

    @PostMapping(path = "/{tourId}/prices")
    public ResponseEntity<ApiResponse<List<TourPriceDTO>>> addTourPrices(
            @PathVariable
            Long tourId,
            @RequestBody List<AddTourPriceDTO> tourPriceDTOS
    )
    {
        Result<List<TourPriceDTO>> result = privateTourService.addTourPrice(tourId,tourPriceDTOS);
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

    @PostMapping(path = "/{tourId}/guide")
    public ResponseEntity<ApiResponse<TourGuideDTO>> addTourGuide(
            @PathVariable
            Long tourId,
            @Valid
            @RequestBody MeetingPoint pickingUpInformation
    )
    {
        Result<TourGuideDTO> result = privateTourService.addTourGuide(tourId,pickingUpInformation);
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



    @PostMapping(path = "/{tourGuideId}/activity", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<TourActivityDetailsDTO>>> addTourActivity(
            @PathVariable
            Long tourGuideId,
            @Valid
            @RequestPart("metadata") TourActivityAddDTO tourActivity,
            @RequestPart("images") List<MultipartFile> photos

    )
    {
        Result<List<TourActivityDetailsDTO>> result = privateTourService.addTourActivity(tourGuideId,tourActivity,photos);
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

    @PostMapping(path = "/guide/{tourGuideId}/point/end")
    public ResponseEntity<ApiResponse<MeetingPoint>> addEndOfTourInformation(
            @PathVariable
            Long tourGuideId,
            @Valid
            @RequestBody MeetingPoint endOfTourInformation

    )
    {
        Result<MeetingPoint> result = privateTourService.addEndOfTourInformation(tourGuideId,endOfTourInformation);
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

    @GetMapping(path = "/{tourId}/guide")
    public ResponseEntity<ApiResponse<TourGuideDTO>> getTourGuide(
            @PathVariable
            Long tourId
    )
    {
        Result<TourGuideDTO> result = privateTourService.getTourGuide(tourId);
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
