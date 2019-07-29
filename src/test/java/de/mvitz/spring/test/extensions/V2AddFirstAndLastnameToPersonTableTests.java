package de.mvitz.spring.test.extensions;

import de.mvitz.spring.test.extensions.MigrationTest.MigrationTestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@MigrationTest(fromVersion = 1, toVersion = 2)
public class V2AddFirstAndLastnameToPersonTableTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void migration_shouldSplitNameIntoFirstAndLastname(MigrationTestTemplate template) {
        template.beforeMigration(() -> {
            jdbcTemplate.execute("TRUNCATE TABLE person");
            jdbcTemplate.execute("INSERT INTO person (name) VALUES ('Test Fixture')");
        });

        template.afterMigration(() -> {
            String person = jdbcTemplate.queryForObject(
                "SELECT * FROM person",
                (rs, rowNum) -> {
                    return rs.getString("lastname") + ", " + rs.getString("firstname");
                });
            assertThat(person)
                .isEqualTo("Fixture, Test");
        });
    }
}
