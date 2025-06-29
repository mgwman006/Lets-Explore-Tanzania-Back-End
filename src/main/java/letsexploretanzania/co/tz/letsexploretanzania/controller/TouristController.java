package letsexploretanzania.co.tz.letsexploretanzania.controller;

import jakarta.validation.Valid;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TouristStatus;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.EmailVerificationRequestDTO;
import letsexploretanzania.co.tz.letsexploretanzania.service.TouristService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/tourist")
public class TouristController {
    private final TouristService touristService;

    public TouristController(TouristService touristService) {
        this.touristService = touristService;
    }

    @PostMapping(path = "/verify")
    public ResponseEntity<ApiResponse<TouristStatus>> verifyTouristByEmail(
            @Valid
            @RequestBody
            EmailVerificationRequestDTO requestDTO
    )
    {
        Result<TouristStatus> result = touristService.verifyTouristByEmail(requestDTO.email());
        if (result.isSuccess())
            return ResponseEntity.ok(
                    ApiResponse.success(
                            result.getData(),
                            HttpStatus.OK.value()
                    )
            );

        return ResponseEntity.badRequest().body(
                ApiResponse.failure(result.getMessage(),HttpStatus.BAD_REQUEST.value())
        );
    }
}
