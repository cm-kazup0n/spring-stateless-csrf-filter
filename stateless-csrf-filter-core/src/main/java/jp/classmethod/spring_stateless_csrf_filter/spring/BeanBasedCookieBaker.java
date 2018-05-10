package jp.classmethod.spring_stateless_csrf_filter.spring;

import jp.classmethod.spring_stateless_csrf_filter.session.CookieSession;
import jp.classmethod.spring_stateless_csrf_filter.session.SessionCookieBaker;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletResponse;

public class BeanBasedCookieBaker extends CookieGenerator implements SessionCookieBaker {

    {
        setCookieSecure(true);
        setCookieMaxAge(-1);
        setCookieHttpOnly(true);
    }

    @Override
    public void addCookie(HttpServletResponse response, TokenSigner signer, CookieSession session) {
        addCookie(response, CookieSession.SerDe.serialize(signer, session));
    }
}
