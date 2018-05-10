package jp.classmethod.spring_stateless_csrf_filter.spring.config;

import jp.classmethod.spring_stateless_csrf_filter.spring.AccessDeniedHandler;
import jp.classmethod.spring_stateless_csrf_filter.spring.BadRequestAccessDeniedHandler;
import jp.classmethod.spring_stateless_csrf_filter.spring.interceptor.CsrfTokenValidationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class CsrfInterceptorConfiguration extends StatelessCsrfFilterConfiguration {

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new BadRequestAccessDeniedHandler();
    }


    @Bean
    public CsrfTokenValidationInterceptor csrfTokenValidationInterceptor() {
        return new CsrfTokenValidationInterceptor(csrfTokenFacade(), accessDeniedHandler());
    }

    @Bean
    public MappedInterceptor mappedInterceptor() {
        return new MappedInterceptor(new String[]{"**/"}, csrfTokenValidationInterceptor());
    }

}
