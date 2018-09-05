package jp.classmethod.spring_stateless_csrf_filter.spring.filter;

import jp.classmethod.spring_stateless_csrf_filter.session.Session;
import jp.classmethod.spring_stateless_csrf_filter.session.SessionProvider;
import jp.classmethod.spring_stateless_csrf_filter.spring.AccessDeniedHandler;
import jp.classmethod.spring_stateless_csrf_filter.token.Token;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * リクエストをCSRFトークン検証するフィルター
 */
public class CsrfFilter extends OncePerRequestFilter {

    /**
     * セッションにトークンを保存するときのデフォルトキー
     */
    public static final String CSRF_TOKEN_NAME = "__CSRF_TOKEN";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AccessDeniedHandler accessDeniedHandler;
    private final RequireCsrfProtectionRequestMatcher matcher;
    private final SessionProvider sessionProvider;
    private final TokenSigner signer;
    private final String csrfTokenName;


    public CsrfFilter(RequireCsrfProtectionRequestMatcher matcher, SessionProvider sessionProvider, TokenSigner signer, AccessDeniedHandler accessDeniedHandler, String tokenName) {
        this.matcher = matcher;
        this.sessionProvider = sessionProvider;
        this.signer = signer;
        this.accessDeniedHandler = accessDeniedHandler;
        this.csrfTokenName = tokenName;
    }

    public CsrfFilter(RequireCsrfProtectionRequestMatcher matcher, SessionProvider sessionProvider, TokenSigner signer, AccessDeniedHandler accessDeniedHandler) {
        this(matcher, sessionProvider, signer, accessDeniedHandler, CSRF_TOKEN_NAME);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //リクエストの検証が必要かチェックする
        if (!matcher.matches(request)) {
            logger.debug("Pass through request " + formatRequest(request));
            refreshToken(request, response);
            filterChain.doFilter(request, response);
            return;
        }
        //検証を行う
        if (!validateToken(request)) {
            logger.debug("Validation failed " + formatRequest(request));
            refreshToken(request, response);
            accessDeniedHandler.handleRequest(request, response);
            return;
        }
        //ok
        refreshToken(request, response);
        filterChain.doFilter(request, response);
    }

    /**
     * セッションに保存するトークンを更新する
     * @param request
     * @param response
     */
    void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final Token newToken = Token.Builder.generate();
        final Session session = sessionProvider.get(request, true).get().put(csrfTokenName, Token.SerDe.signAndEncode(signer, newToken));
        sessionProvider.flush(response, session);
    }

    /**
     * リクエストを検証する
     * @param request
     * @return 検証に成功した場合 true
     */
    boolean validateToken(HttpServletRequest request) {
        final Session session = sessionProvider.get(request, true).get();
        final Optional<Token> tokenFromSession = session.get(csrfTokenName).map(s -> Token.SerDe.decodeAndVerify(signer, s));
        final Optional<Token> tokenFromCookie = Optional.ofNullable(request.getParameter(csrfTokenName)).map(s -> Token.SerDe.decodeAndVerify(signer, s));
        return tokenFromSession.flatMap(s -> tokenFromCookie.map(c -> c.compareSafely(s))).orElse(false);
    }

    String formatRequest(HttpServletRequest request) {
        return request.getMethod() + " " + request.getRequestURI();
    }


}
