package partners.meat.tms.api.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing

public class DatabaseAuditConfig {

    @Bean
    public DatabaseAuditAware auditorAware() {
        return new DatabaseAuditAware();
    }
}
