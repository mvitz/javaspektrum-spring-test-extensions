package de.mvitz.spring.test.extensions;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PersonController {

    private final JdbcTemplate jdbcTemplate;

    public PersonController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/person")
    public String list() {
        List<String> persons = jdbcTemplate.query("SELECT * FROM person", (rs, rowNum) -> {
            return rs.getString("name");
        });
        return persons.stream().collect(Collectors.joining(", "));
    }
}
