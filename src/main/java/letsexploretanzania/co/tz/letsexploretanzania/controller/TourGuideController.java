package letsexploretanzania.co.tz.letsexploretanzania.controller;

import jakarta.validation.Valid;
import letsexploretanzania.co.tz.letsexploretanzania.common.dtos.MeetingPoint;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.TourActivityAddDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.TourActivityDetailsDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.TourGuideDTO;
import letsexploretanzania.co.tz.letsexploretanzania.service.AWSService;
import letsexploretanzania.co.tz.letsexploretanzania.service.TourGuideService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tour/guide")
public class TourGuideController {

    private final TourGuideService tourGuideService;

    public TourGuideController(TourGuideService tourGuideService, AWSService awsService) {
        this.tourGuideService = tourGuideService;
    }

    @PostMapping(path = "/{tourGuideId}/point/end")
    public ResponseEntity<ApiResponse<TourGuideDTO>> addEndOfTourInformation(
            @PathVariable
            Long tourGuideId,
            @Valid
            @RequestBody MeetingPoint endOfTourInformation

    )
    {
        Result<TourGuideDTO> result = tourGuideService.addEndOfTourInformation(tourGuideId,endOfTourInformation);
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


    @PostMapping(path = "/{tourGuideId}/point/start")
    public ResponseEntity<ApiResponse<TourGuideDTO>> addTourPickUpInformation(
            @PathVariable
            Long tourGuideId,
            @Valid
            @RequestBody MeetingPoint pickingUpInformation
    )
    {
        Result<TourGuideDTO> result = tourGuideService.addTourGuide(tourGuideId,pickingUpInformation);
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
    public ResponseEntity<ApiResponse<TourGuideDTO>> addTourActivity(
            @PathVariable
            Long tourGuideId,
            @Valid
            @RequestPart("metadata") TourActivityAddDTO tourActivity,
            @RequestPart("images") List<MultipartFile> photos

    )
    {
        Result<TourGuideDTO> result = tourGuideService.addTourActivity(tourGuideId,tourActivity,photos);
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
