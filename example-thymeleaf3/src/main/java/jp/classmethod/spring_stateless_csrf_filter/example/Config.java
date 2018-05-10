package jp.classmethod.spring_stateless_csrf_filter.example;

import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
import jp.classmethod.spring_stateless_csrf_filter.spring.config.CsrfInterceptorConfiguration;
import jp.classmethod.spring_stateless_csrf_filter.spring.interceptor.CsrfTokenValidationInterceptor;
import jp.classmethod.spring_stateless_csrf_filter.thymeleaf3.CsrfTokenDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
@Import({CsrfInterceptorConfiguration.class})
public class Config extends WebMvcConfigurerAdapter {

    @Autowired
    private CsrfTokenFacade csrfTokenFacade;

    @Autowired
    private CsrfTokenValidationInterceptor tokenValidationInterceptor;

    @Bean
    public ViewResolver viewResolver() {
        final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        final SpringTemplateEngine engine = new SpringTemplateEngine();
        final CsrfTokenDialect dialect = new CsrfTokenDialect(csrfTokenFacade);
        engine.addDialect(dialect.getPrefix(), dialect);
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver classLoaderTemplateResolver = new ClassLoaderTemplateResolver();
        classLoaderTemplateResolver.setCacheable(false);
        classLoaderTemplateResolver.setPrefix("templates/");
        classLoaderTemplateResolver.setSuffix(".html");
        classLoaderTemplateResolver.setTemplateMode("HTML");
        classLoaderTemplateResolver.setCacheable(false);
        return classLoaderTemplateResolver;
    }

    @Bean
    public MappedInterceptor csrfProtectionIntercetorRegistration() {
        return new MappedInterceptor(new String[]{"/**"}, tokenValidationInterceptor);
    }

}
