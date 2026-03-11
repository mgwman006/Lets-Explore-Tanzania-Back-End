package letsexploretanzania.co.tz.letsexploretanzania.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.*;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.UserDTO;
import letsexploretanzania.co.tz.letsexploretanzania.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/otp/send")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody OtpRequestDTO request) {
        Result<String> result = authService.sendOtp(request.email());
        if (result.isSuccess())
        {
            return ResponseEntity.ok(ApiResponse.success("success", HttpStatus.OK.value()));
        }
        return ResponseEntity.badRequest().body(
                ApiResponse.failure(result.getMessage(), HttpStatus.BAD_REQUEST.value())
        );
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@RequestBody OtpVerificationRequestDTO request) {
        Result<String> result = authService.verifyOtp(request.email(), request.otp());
        if (result.isSuccess())
            return ResponseEntity.ok(ApiResponse.success("success",HttpStatus.OK.value()));
        return ResponseEntity.badRequest().body(ApiResponse.failure(result.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDTO>> logIn(
            @Valid
            @RequestBody LogInDetailsDTO request)
    {

        Result<UserDTO> result = authService.logIn(request.email(), request.passWord());
        if (result.isSuccess())
            return ResponseEntity.ok(
                    ApiResponse.success(
                            result.getData(),
                            HttpStatus.OK.value()
                    )
            );
        return ResponseEntity.badRequest().body(ApiResponse.failure(result.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @PostMapping(path = "/email")
    public ResponseEntity<ApiResponse<String>> sendEmail(@RequestBody GenericEmailDTO genericEmailDTO)
    {
        Result<String> result = authService.sendGenericEmail(genericEmailDTO.toAddress(), genericEmailDTO.subject(), genericEmailDTO.bodyText());
        if (result.isSuccess())
            return ResponseEntity.ok(ApiResponse.success("success", HttpStatus.OK.value()));
        return ResponseEntity.badRequest().body(ApiResponse.failure(result.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

}

