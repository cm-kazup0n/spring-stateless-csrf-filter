package jp.classmethod.spring_stateless_csrf_filter.example_thymeleaf3;

import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
import jp.classmethod.spring_stateless_csrf_filter.spring.config.CsrfInterceptorConfiguration;
import jp.classmethod.spring_stateless_csrf_filter.spring.interceptor.CsrfTokenValidationInterceptor;
import jp.classmethod.spring_stateless_csrf_filter.thymeleaf3.CsrfTokenDialect;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Configuration
@Import({CsrfInterceptorConfiguration.class})
public class Config implements WebMvcConfigurer {

    @Autowired
    private CsrfTokenFacade csrfTokenFacade;

    @Autowired
    private CsrfTokenValidationInterceptor tokenValidationInterceptor;

    @Autowired
    public void setSprintTemplateEngine(SpringTemplateEngine engine) {
        final IDialect dialect = new CsrfTokenDialect(csrfTokenFacade);
        engine.addDialect(dialect);
        engine.addDialect(new LayoutDialect()); //optional
    }


    @Bean
    public MappedInterceptor csrfProtectionIntercetorRegistration() {
        return new MappedInterceptor(new String[]{"/**"}, tokenValidationInterceptor);
    }

}
