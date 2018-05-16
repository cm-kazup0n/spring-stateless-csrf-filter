package jp.classmethod.spring_stateless_csrf_filter.spring.config;

import jp.classmethod.spring_stateless_csrf_filter.session.CookieSessionProvider;
import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
import jp.classmethod.spring_stateless_csrf_filter.session.SessionProvider;
import jp.classmethod.spring_stateless_csrf_filter.spring.BeanBasedCookieBaker;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatelessCsrfFilterConfiguration {

    @Value("${stateless_csrf.csrf.secret}")
    protected String secret;

    @Value("${stateless_csrf.csrf.cookieName}")
    protected String cookieName;

    @Value("${stateless_csrf.csrf.csrfTokenName}")
    protected String csrfTokenName;

    @Value("${stateless_csrf.csrf.pathPatternsToMatch:}")
    protected String[] pathPatternsToMatch;

    @Value("${stateless_csrf.csrf.secureCookie:true}")
    protected  boolean isCookieSecure;


    @Bean
    public SessionProvider sessionProvider() {
        final BeanBasedCookieBaker cookieBaker = new BeanBasedCookieBaker();
        cookieBaker.setCookieName(cookieName);
        cookieBaker.setCookieSecure(isCookieSecure);

        return new CookieSessionProvider(cookieBaker, tokenSigner());
    }

    @Bean
    public TokenSigner tokenSigner() {
        return new TokenSigner(secret);
    }


    @Bean
    public CsrfTokenFacade csrfTokenFacade() {
        return new CsrfTokenFacade(sessionProvider(), tokenSigner(), csrfTokenName);
    }


}
