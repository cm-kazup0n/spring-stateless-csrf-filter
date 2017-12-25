package jp.classmethod.spring_stateless_csrf_filter.spring;

import jp.classmethod.spring_stateless_csrf_filter.session.Session;
import jp.classmethod.spring_stateless_csrf_filter.session.SessionProvider;
import jp.classmethod.spring_stateless_csrf_filter.token.Token;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class CsrfFilter extends OncePerRequestFilter {

    public static final String CSRF_TOKEN_NAME = "__CSRF_TOKEN";

    private final AccessDeniedHandler accessDeniedHandler;
    private final RequireCsrfProtectionRequestMatcher matcher;
    private final SessionProvider sessionProvider;
    private final TokenSigner signer;


    CsrfFilter(RequireCsrfProtectionRequestMatcher matcher, SessionProvider sessionProvider, TokenSigner signer, AccessDeniedHandler accessDeniedHandler) {
        this.matcher = matcher;
        this.sessionProvider = sessionProvider;
        this.signer = signer;
        this.accessDeniedHandler = accessDeniedHandler;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //pass through
        if (!matcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        //validate
        if (!validateToken(request)) {
            refreshToken(request, response);
            accessDeniedHandler.handleRequest(request, response);
            return;
        }
        //ok
        refreshToken(request, response);
        filterChain.doFilter(request, response);
    }


    void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final Token newToken = Token.Builder.generate();
        final Session session = sessionProvider.get(request, true).get().put(CSRF_TOKEN_NAME, Token.SerDe.signAndEncode(signer, newToken));
        sessionProvider.flush(response, session);
    }

    boolean validateToken(HttpServletRequest request) {
        final Session session = sessionProvider.get(request, true).get();
        final Optional<Token> tokenFromSession = session.get(CSRF_TOKEN_NAME).map(s -> Token.SerDe.decodeAndVerify(signer, (String) s));
        final Optional<Token> tokenFromCookie = Optional.ofNullable(request.getParameter(CSRF_TOKEN_NAME)).map(s -> Token.SerDe.decodeAndVerify(signer, s));
        return tokenFromSession.flatMap(s -> tokenFromCookie.map(c -> c.compareSafely(s))).orElse(false);
    }


}
