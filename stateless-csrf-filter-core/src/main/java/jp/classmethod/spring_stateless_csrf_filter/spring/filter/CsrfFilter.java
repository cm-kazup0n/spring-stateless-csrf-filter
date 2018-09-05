package jp.classmethod.spring_stateless_csrf_filter.spring.filter;

import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
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

    private final CsrfTokenFacade csrfTokenFacade;

    public CsrfFilter(RequireCsrfProtectionRequestMatcher matcher, CsrfTokenFacade csrfTokenFacade, AccessDeniedHandler accessDeniedHandler) {
        this.matcher = matcher;
        this.csrfTokenFacade = csrfTokenFacade;
        this.accessDeniedHandler = accessDeniedHandler;
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
     *
     * @param request
     * @param response
     */
    void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        csrfTokenFacade.populateCsrfToken(request, response, true);
    }

    /**
     * リクエストを検証する
     *
     * @param request
     * @return 検証に成功した場合 true
     */
    boolean validateToken(HttpServletRequest request) {
        return csrfTokenFacade.validate(request).orElse(false);
    }

    String formatRequest(HttpServletRequest request) {
        return request.getMethod() + " " + request.getRequestURI();
    }


}
