package work.jatin.newsfeed.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@EnableJdbcRepositories(basePackages = "work.jatin.newsfeed.repositories")
public class JpaConfig {

}
