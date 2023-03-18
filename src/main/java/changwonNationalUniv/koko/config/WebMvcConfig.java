package changwonNationalUniv.koko.config;

import changwonNationalUniv.koko.interceptor.MemberInfoToModelInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MemberInfoToModelInterceptor())
                .excludePathPatterns("/login", "/join", "/css/**", "/image/**" , "/js/**");
    }
}
