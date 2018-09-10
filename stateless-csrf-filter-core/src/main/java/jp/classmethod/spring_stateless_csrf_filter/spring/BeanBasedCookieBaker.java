package jp.classmethod.spring_stateless_csrf_filter.spring;

import jp.classmethod.spring_stateless_csrf_filter.session.CookieSession;
import jp.classmethod.spring_stateless_csrf_filter.session.SessionCookieBaker;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletResponse;

/**
 * spring CookieGereratorをベースとしたSessionCookieBakerの実装
 */
public class BeanBasedCookieBaker implements SessionCookieBaker {

    private final CookieGenerator cookieGenerator;

    public BeanBasedCookieBaker(final String cookieName, final boolean isCookieSecure) {
        cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookieSecure(true);
        cookieGenerator.setCookieMaxAge(-1);
        cookieGenerator.setCookieHttpOnly(true);
        cookieGenerator.setCookieName(cookieName);
        cookieGenerator.setCookieSecure(isCookieSecure);
    }

    @Override
    public void addCookie(HttpServletResponse response, TokenSigner signer, CookieSession session) {
        cookieGenerator.addCookie(response, CookieSession.SerDe.serialize(signer, session));
    }

    @Override
    public String getCookieName() {
        return cookieGenerator.getCookieName();
    }
}

