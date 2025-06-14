package letsexploretanzania.co.tz.letsexploretanzania.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheck {

    @GetMapping
    public String isServerAlive()
    {
        return "Hey buddy, I am alive";
    }

}
