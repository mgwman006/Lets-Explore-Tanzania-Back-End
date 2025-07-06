package letsexploretanzania.co.tz.letsexploretanzania.controller;

import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.OperatorDetailsDTO;
import letsexploretanzania.co.tz.letsexploretanzania.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{userId}/operator")
    public ResponseEntity<ApiResponse<OperatorDetailsDTO>> getUserOperator(@PathVariable Long userId)
    {
        Result<OperatorDetailsDTO> result = userService.getOperator(userId);
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
}
