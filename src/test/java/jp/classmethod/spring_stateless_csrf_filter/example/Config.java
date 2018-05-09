package jp.classmethod.spring_stateless_csrf_filter.example;

import jp.classmethod.spring_stateless_csrf_filter.session.CookieSessionProvider;
import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
import jp.classmethod.spring_stateless_csrf_filter.session.SessionProvider;
import jp.classmethod.spring_stateless_csrf_filter.spring.BeanBasedCookieBaker;
import jp.classmethod.spring_stateless_csrf_filter.thymeleaf.CsrfTokenDialect;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class Config extends WebMvcConfigurerAdapter {

    @Bean
    public ViewResolver viewResolver() {
        final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    @Primary
    public SpringTemplateEngine templateEngine() {
        final SpringTemplateEngine engine = new SpringTemplateEngine();
        final CsrfTokenDialect dialect = new CsrfTokenDialect(getCsrfTokenFacade());
        engine.addDialect(dialect.getPrefix(), dialect);
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    @Bean
    public SessionProvider sessionProvider(TokenSigner tokenSigner){
        final BeanBasedCookieBaker cookieBaker =  new BeanBasedCookieBaker();
        cookieBaker.setCookieName("session");

        return new CookieSessionProvider(cookieBaker, tokenSigner);
    }

    @Bean
    public TokenSigner tokenSigner() {
        return new TokenSigner("secret");
    }

    @Qualifier("csrfTokenName")
    @Bean
    public String getCsrfTokenName(){
        return "__CSRF";
    }

    @Bean
    public CsrfTokenFacade getCsrfTokenFacade(){
        return new CsrfTokenFacade();
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

}
