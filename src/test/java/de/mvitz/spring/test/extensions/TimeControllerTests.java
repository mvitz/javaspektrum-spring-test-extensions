package de.mvitz.spring.test.extensions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
public class TimeControllerTests {

    @Autowired
    MockMvc mvc;

    @Test
    void now_shouldRenderCurrentTime() throws Exception {
        mvc.perform(get("/time"))
            .andExpect(status().isOk())
            .andExpect(content().string(is(equalTo("2019-07-29 14:10:53"))));
    }

    @TestConfiguration
    static class TestClockConfiguration {
        @Bean
        public Clock clock() {
            LocalDateTime localDateTime = LocalDateTime.of(2019, 7, 29, 14, 10, 53);
            return Clock.fixed(localDateTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        }
    }
}
