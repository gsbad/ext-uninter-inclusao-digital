package dev.gustavosa.inclusaodigital.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final OficinaSessionInterceptor oficinaSessionInterceptor;

    public WebConfig(OficinaSessionInterceptor oficinaSessionInterceptor) {
        this.oficinaSessionInterceptor = oficinaSessionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(oficinaSessionInterceptor)
                .addPathPatterns("/oficina/**")
                .excludePathPatterns("/oficina/cadastro", "/oficina/cadastro/**");
    }
}
