package letsexploretanzania.co.tz.letsexploretanzania.controller;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TourDestinationEnum;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourDestination;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/destination")
public class TourDestinationController {

    @GetMapping
    public ResponseEntity<ApiResponse<List<String>>> getAllDestinations()
    {
        List<String> destinations = Arrays.stream(TourDestinationEnum.values())
                .map( d -> d.getName()).toList();
        return ResponseEntity.ok(ApiResponse.success(destinations, HttpStatus.OK.value()));
    }
}
