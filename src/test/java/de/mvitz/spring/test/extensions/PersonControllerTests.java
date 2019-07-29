package de.mvitz.spring.test.extensions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureTestDatabase
public class PersonControllerTests {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void list_shouldRenderListOfKnownPersonNames() {
        jdbcTemplate.execute("TRUNCATE TABLE person");
        jdbcTemplate.execute("INSERT INTO person (firstname, lastname) VALUES ('Ein', 'Test'), ('Hallo', 'Welt')");

         String content = restTemplate.getForObject("http://localhost:" + port + "/person", String.class);

         assertThat(content)
             .isEqualTo("Test, Ein; Welt, Hallo");
    }
}
