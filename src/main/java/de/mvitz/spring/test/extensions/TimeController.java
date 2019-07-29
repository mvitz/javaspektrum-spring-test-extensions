package de.mvitz.spring.test.extensions;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class TimeController {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/time")
    public String now() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DATE_TIME_FORMAT);
    }
}
