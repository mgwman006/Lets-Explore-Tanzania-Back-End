package letsexploretanzania.co.tz.letsexploretanzania.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.Tourist;
import letsexploretanzania.co.tz.letsexploretanzania.service.common.EmailService;
import letsexploretanzania.co.tz.letsexploretanzania.service.common.OtpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@Service
public class AuthService {
    private final OtpService otpService;
    private final EmailService emailService;

    public AuthService(OtpService otpService, EmailService emailService) {
        this.otpService = otpService;
        this.emailService = emailService;
    }

    public Result<String> sendOtp(String email)
    {
        String otp = otpService.generateOtp(email);
        try {
            emailService.sendOtpEmail(email, otp);
            return Result.success("success","success");
        }catch (Exception e)
        {
            return Result.failure(e.getMessage());
        }

    }
    public Result<String> verifyOtp(String email, String otp)
    {
        try {
            boolean isValid = otpService.validateOtp(email, otp);
            if (isValid)
                return Result.success("success","Otp validated");
            return Result.failure("validation failed");
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }
}
