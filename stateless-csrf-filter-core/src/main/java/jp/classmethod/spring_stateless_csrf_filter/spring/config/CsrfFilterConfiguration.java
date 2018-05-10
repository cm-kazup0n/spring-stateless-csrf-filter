package jp.classmethod.spring_stateless_csrf_filter.spring.config;

import jp.classmethod.spring_stateless_csrf_filter.session.SessionProvider;
import jp.classmethod.spring_stateless_csrf_filter.spring.AccessDeniedHandler;
import jp.classmethod.spring_stateless_csrf_filter.spring.BadRequestAccessDeniedHandler;
import jp.classmethod.spring_stateless_csrf_filter.spring.filter.CsrfFilter;
import jp.classmethod.spring_stateless_csrf_filter.spring.filter.DefaultRequestMatcher;
import jp.classmethod.spring_stateless_csrf_filter.spring.filter.RequireCsrfProtectionRequestMatcher;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class CsrfFilterConfiguration extends StatelessCsrfFilterConfiguration {

    @Bean
    public CsrfFilter csrfFilter() {
        final RequireCsrfProtectionRequestMatcher matcher = requireCsrfProtectionRequestMatcher();
        final SessionProvider sessionProvider = sessionProvider();
        TokenSigner signer = tokenSigner();
        AccessDeniedHandler accessDeniedHandler = accessDeniedHandler();
        return new CsrfFilter(matcher, sessionProvider, signer, accessDeniedHandler, cookieName);
    }

    @Bean
    public RequireCsrfProtectionRequestMatcher requireCsrfProtectionRequestMatcher() {
        return new DefaultRequestMatcher(new HashSet<>(Arrays.asList(pathPatternsToMatch)));
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new BadRequestAccessDeniedHandler();
    }

}
