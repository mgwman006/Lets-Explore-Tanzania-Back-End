package letsexploretanzania.co.tz.letsexploretanzania.controller;

import jakarta.validation.Valid;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.PassWordResetDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.OperatorDetailsDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.UserDTO;
import letsexploretanzania.co.tz.letsexploretanzania.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PatchMapping(path = "/password/reset")
    public ResponseEntity<ApiResponse<UserDTO>> resetPassWord(
            @Valid
            @RequestBody
            PassWordResetDTO request
    )
    {
        Result<UserDTO> result = userService.resetPassWord(request.email(), request.passWord());
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
