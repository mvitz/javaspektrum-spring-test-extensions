package de.mvitz.spring.test.extensions;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.util.SocketUtils.findAvailableTcpPort;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration(classes = WithEmbeddedPostgres.EmbeddedPostgresConfiguration.class)
public @interface WithEmbeddedPostgres {

    @Configuration
    @AutoConfigureBefore(DataSourceAutoConfiguration.class)
    class EmbeddedPostgresConfiguration {

        @Bean(destroyMethod = "stop")
        EmbeddedPostgres embeddedPostgres() throws IOException {
            EmbeddedPostgres embeddedPostgres = new EmbeddedPostgres(() -> "9.6.8-1");

            int port = findAvailableTcpPort(10000);
            embeddedPostgres.start("localhost", port, "database", "username", "password");

            return embeddedPostgres;
        }

        @Bean
        BeanFactoryPostProcessor dataSourcePropertiesOverrideConfigurer(EmbeddedPostgres embeddedPostgres) {
            return (beanFactory) -> {
                    Map<String, Object> dataSourcePropertiesOverride = new HashMap<>();
                    dataSourcePropertiesOverride.put("spring.datasource.url", embeddedPostgres.getConnectionUrl().get());
                    dataSourcePropertiesOverride.put("spring.datasource.username", embeddedPostgres.getConfig().get().credentials().username());
                    dataSourcePropertiesOverride.put("spring.datasource.password", embeddedPostgres.getConfig().get().credentials().password());

                    ConfigurableEnvironment configurableEnvironment = beanFactory.getBean(ConfigurableEnvironment.class);
                    configurableEnvironment.getPropertySources().addFirst(new MapPropertySource("dataSourcePropertiesOverride", dataSourcePropertiesOverride));
            };
        }

        @Configuration
        protected static class EmbeddedPostgresDependencyConfiguration
            extends AbstractDependsOnBeanFactoryPostProcessor {

            public EmbeddedPostgresDependencyConfiguration() {
                super(DataSource.class, "embeddedPostgres");
            }
        }
    }
}
