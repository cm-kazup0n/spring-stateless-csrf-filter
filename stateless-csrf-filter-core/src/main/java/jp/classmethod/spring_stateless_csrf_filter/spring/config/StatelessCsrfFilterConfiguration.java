package jp.classmethod.spring_stateless_csrf_filter.spring.config;

import jp.classmethod.spring_stateless_csrf_filter.session.CookieSessionProvider;
import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
import jp.classmethod.spring_stateless_csrf_filter.session.SessionProvider;
import jp.classmethod.spring_stateless_csrf_filter.spring.BeanBasedCookieBaker;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CSRFトークンの検証を行う基本コンポーネントの設定
 */
@Configuration
public class StatelessCsrfFilterConfiguration {

    /**
     * 署名のキー
     */
    @Value("${stateless_csrf.csrf.secret}")
    protected String secret;

    /**
     * セッションを保存するクッキー名
     */
    @Value("${stateless_csrf.csrf.cookieName}")
    protected String cookieName;

    /**
     * トークンを保存するパラメータ/セッション名
     */
    @Value("${stateless_csrf.csrf.csrfTokenName}")
    protected String csrfTokenName;

    /**
     * フィルターによるチェックを行うパス
     */
    @Value("${stateless_csrf.csrf.pathPatternsToMatch:}")
    protected String[] pathPatternsToMatch;

    /**
     * セッション用クッキーにsecureフラグを付与するか. 付与する場合true
     */
    @Value("${stateless_csrf.csrf.secureCookie:true}")
    protected boolean isCookieSecure;


    @Bean
    public SessionProvider sessionProvider() {
        final BeanBasedCookieBaker cookieBaker = new BeanBasedCookieBaker(cookieName, isCookieSecure);
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
