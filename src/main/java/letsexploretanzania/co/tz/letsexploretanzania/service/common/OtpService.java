package letsexploretanzania.co.tz.letsexploretanzania.service.common;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
public class OtpService {

    private final RedisTemplate<String, String> redisTemplate;

    public OtpService(
            RedisTemplate<String, String> redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    public String generateOtp(String email) {
        String otp = String.format("%06d", new SecureRandom().nextInt(1000000));
        redisTemplate.opsForValue().set(email, otp, Duration.ofMinutes(5));
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        String storedOtp = redisTemplate.opsForValue().get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete(email); // Invalidate OTP after use
            return true;
        }
        return false;
    }
}
