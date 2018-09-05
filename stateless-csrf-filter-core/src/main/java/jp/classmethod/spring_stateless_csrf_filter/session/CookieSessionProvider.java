package jp.classmethod.spring_stateless_csrf_filter.session;

import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class CookieSessionProvider implements SessionProvider {

    private final SessionCookieBaker baker;
    private final TokenSigner signer;

    public CookieSessionProvider(SessionCookieBaker baker, TokenSigner signer) {
        this.baker = baker;
        this.signer = signer;
    }

    @Override
    public Optional<Session> get(HttpServletRequest request, boolean create) {
        final Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[]{});
        final String cookieName = baker.getCookieName();
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return Optional.of(CookieSession.SerDe.deserialize(signer, cookie.getValue()));
            }
        }
        return Optional.ofNullable( create ? CookieSession.create(): null);
    }

    @Override
    public void flush(HttpServletResponse response, Session session) {
        if (session instanceof CookieSession) {
            baker.addCookie(response, signer, (CookieSession) session);
        } else {
            throw new IllegalArgumentException("CookieSessionProvider accepts only CookieSession, But given + " + session.getClass().getCanonicalName());
        }
    }

}
