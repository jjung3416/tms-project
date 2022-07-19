package partners.meat.tms.api.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import partners.meat.tms.api.commons.inteceptors.JwtTokenInterceptor;

@Configuration("TmsInterceptorConfig")
public class InterceptorConfig implements WebMvcConfigurer {
    public void addInterceptors(InterceptorRegistry registry) {
        // Jwt Token정보 검증
        registry.addInterceptor(this.jwtTokenInterceptor())
                .addPathPatterns("/v1/**")
                .excludePathPatterns("/v1/user/signIn")
                .excludePathPatterns("/v1/user/signUp")
                .excludePathPatterns("/v1/auth/**");
    }

    @Bean
    public JwtTokenInterceptor jwtTokenInterceptor() {return new JwtTokenInterceptor();}
}
