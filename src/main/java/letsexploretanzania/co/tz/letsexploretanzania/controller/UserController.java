package letsexploretanzania.co.tz.letsexploretanzania.controller;

import jakarta.validation.Valid;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.UserStatus;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests.EmailVerificationRequestDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.OperatorDetailsDTO;
import letsexploretanzania.co.tz.letsexploretanzania.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping(path = "/{username}/operator")
    public ResponseEntity<ApiResponse<OperatorDetailsDTO>> getUserOperator(@PathVariable String username)
    {
        Result<OperatorDetailsDTO> result = userService.getOperator(username);
        if (result.isSuccess()) {
            return ResponseEntity.ok(
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

//    @PostMapping
//    public ResponseEntity<ApiResponse<CreatedOperatorDto>> registerTourOperator(
//      @Valid
//      @RequestBody AddOperatorDto tourOperator
//    )
//    {
//        Result<CreatedOperatorDto> result = tourOperatorService.registerOperator(tourOperator);
//        if (result.isSuccess()) {
//            URI location = URI.create("operator/created");
//            return ResponseEntity.created(location).body(
//              ApiResponse.success(
//                result.getData(),
//                HttpStatus.OK.value()
//              )
//            );
//        }
//
//        return ResponseEntity.badRequest().body(
//          ApiResponse.failure(
//            result.getMessage(),
//            HttpStatus.BAD_REQUEST.value()
//          )
//        );
//    }
}
