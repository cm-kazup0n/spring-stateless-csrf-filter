package jp.classmethod.spring_stateless_csrf_filter.example_thymeleaf2;

import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
import jp.classmethod.spring_stateless_csrf_filter.spring.config.CsrfInterceptorConfiguration;
import jp.classmethod.spring_stateless_csrf_filter.spring.interceptor.CsrfTokenValidationInterceptor;
import jp.classmethod.spring_stateless_csrf_filter.thymeleaf2.CsrfTokenDialect;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.thymeleaf.dialect.IDialect;

@Configuration
@Import({CsrfInterceptorConfiguration.class})
public class Config extends WebMvcConfigurerAdapter {

    @Autowired
    private CsrfTokenFacade csrfTokenFacade;

    @Autowired
    private CsrfTokenValidationInterceptor tokenValidationInterceptor;


    @Bean
    public IDialect csrfTokenDialect() {
        return new CsrfTokenDialect(csrfTokenFacade);
    }

    @Bean
    public IDialect layoutDialect() {
        return new LayoutDialect();
    }


    @Bean
    public MappedInterceptor csrfProtectionIntercetorRegistration() {
        return new MappedInterceptor(new String[]{"/**"}, tokenValidationInterceptor);
    }

}
