package partners.meat.tms.api.commons.inteceptors;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import partners.meat.tms.api.commons.utils.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        log.info("JwtToken 호출");
        String accessToken = request.getHeader("ACCESS_TOKEN");
        log.info("AccessToken = {}", accessToken);
        String refreshToken = request.getHeader("REFRESH_TOKEN");
        log.info("RefreshToken = {}", refreshToken);

        if (StringUtils.isNotEmpty(accessToken) && tokenUtils.isValidToken(accessToken)) {
            return true;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("ACCESS_TOKEN", accessToken);
        response.setHeader("REFRESH_TOKEN", refreshToken);
        response.setHeader("msg", "Check the tokens.");
        return false;
    }
}
