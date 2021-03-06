package de.mvitz.spring.test.extensions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TimeController.class)
@WithLocalDateTime(date = "2019-07-29", time = "14:10:53")
public class TimeControllerTests {

    @Autowired
    MockMvc mvc;

    @Test
    void now_shouldRenderCurrentTime() throws Exception {
        mvc.perform(get("/time"))
            .andExpect(status().isOk())
            .andExpect(content().string(is(equalTo("2019-07-29 14:10:53"))));
    }
}
