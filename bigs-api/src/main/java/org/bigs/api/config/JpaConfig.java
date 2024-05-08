package org.bigs.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EntityScan(basePackages = "org.bigs.domain")
@EnableJpaRepositories(basePackages = "org.bigs.domain")
@Configuration
public class JpaConfig {
}
