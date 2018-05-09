package jp.classmethod.spring_stateless_csrf_filter.session;

import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;

import javax.servlet.http.HttpServletResponse;

public interface SessionCookieBaker {

    void addCookie(HttpServletResponse response, TokenSigner signer, CookieSession session);

    String getCookieName();

}
