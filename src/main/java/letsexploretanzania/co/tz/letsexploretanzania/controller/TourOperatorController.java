package letsexploretanzania.co.tz.letsexploretanzania.controller;

import jakarta.validation.Valid;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.AddOperatorDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.CreatedOperatorDto;
import letsexploretanzania.co.tz.letsexploretanzania.service.TourOperatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(path = "api/v1/operator")
public class TourOperatorController {

    private final TourOperatorService tourOperatorService;

    public TourOperatorController(TourOperatorService tourOperatorService) {
        this.tourOperatorService = tourOperatorService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreatedOperatorDto>> registerTourOperator(
            @Valid
            @RequestBody AddOperatorDto tourOperator
    )
    {
        Result<CreatedOperatorDto> result = tourOperatorService.registerOperator(tourOperator);
        if (result.isSuccess()) {
            URI location = URI.create("operator/created");
            return ResponseEntity.created(location).body(
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
