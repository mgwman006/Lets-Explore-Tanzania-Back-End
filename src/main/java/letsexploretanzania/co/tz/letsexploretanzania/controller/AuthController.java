package letsexploretanzania.co.tz.letsexploretanzania.controller;

import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.OtpRequestDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.OtpVerificationRequestDTO;
import letsexploretanzania.co.tz.letsexploretanzania.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

