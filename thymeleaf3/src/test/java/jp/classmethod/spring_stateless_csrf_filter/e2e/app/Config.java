package jp.classmethod.spring_stateless_csrf_filter.e2e.app;

import jp.classmethod.spring_stateless_csrf_filter.e2e.app.filter.BadRequestAccessDeniedHandler;
import jp.classmethod.spring_stateless_csrf_filter.session.CookieSessionProvider;
import jp.classmethod.spring_stateless_csrf_filter.session.SessionProvider;
import jp.classmethod.spring_stateless_csrf_filter.spring.*;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class Config {

    @Bean
    @Autowired
    public FilterRegistrationBean filterRegistrationBean(CsrfFilter csrfFilter){
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(csrfFilter);
        return registration;
    }

    @Bean
    @Autowired
    public CsrfFilter csrfFilter(RequireCsrfProtectionRequestMatcher matcher, SessionProvider sessionProvider, TokenSigner signer, AccessDeniedHandler accessDeniedHandler, CsrfConfig config) {
        return new CsrfFilter(matcher, sessionProvider, signer, accessDeniedHandler, config.getCookieName());
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new BadRequestAccessDeniedHandler();
    }


    @Bean
    public SessionProvider sessionProvider(TokenSigner tokenSigner){
        final BeanBasedCookieBaker cookieBaker =  new BeanBasedCookieBaker();
        cookieBaker.setCookieName("session");

        return new CookieSessionProvider(cookieBaker, tokenSigner);
    }

    @Bean
    @Autowired
    public TokenSigner tokenSigner(CsrfConfig config){
        return new TokenSigner(config.getSecret());
    }

    @Bean
    public RequireCsrfProtectionRequestMatcher requireCsrfProtectionRequestMatcher(){
        final Set<String> pathsToMatch = new HashSet<>();
        pathsToMatch.add("/secured/**");
        return new DefaultRequestMatcher(pathsToMatch);
    }


}
