package partners.meat.tms.api.commons.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
public class DatabaseAuditAware implements AuditorAware<Long> {
    //등록자 데이터를 자동으로 생성해주기위해
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (null == authentication || !authentication.isAuthenticated()) {
            return null;
        }

        try {
            return Optional.ofNullable(-1L);

        } catch (Exception e) {
            log.error("ERROR : UserDetail failed!", e);
        }

        return Optional.of(-1L);
    }

}
