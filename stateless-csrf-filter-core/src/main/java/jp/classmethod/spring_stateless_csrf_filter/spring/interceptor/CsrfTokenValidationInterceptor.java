package jp.classmethod.spring_stateless_csrf_filter.spring.interceptor;

import jp.classmethod.spring_stateless_csrf_filter.session.CsrfTokenFacade;
import jp.classmethod.spring_stateless_csrf_filter.spring.AccessDeniedHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CSRFトークンによる検証を行うインターセプター
 */
public class CsrfTokenValidationInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CsrfTokenValidationInterceptor.class);

    private final CsrfTokenFacade csrfTokenFacade;
    private final AccessDeniedHandler accessDeniedHandler;

    public CsrfTokenValidationInterceptor(CsrfTokenFacade csrfTokenFacade, AccessDeniedHandler accessDeniedHandler) {
        this.csrfTokenFacade = csrfTokenFacade;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (requestNeedsInspect(handler)) {
            logger.debug("Intercept request: {} {}", request.getMethod(), request.getRequestURI());
            //チェックが必要な場合は検証に成功した時以外はアクセス拒否
            final boolean valid = csrfTokenFacade.validate(request).orElse(false);
            if (!valid) {
                logger.debug("Request denied; request: {} {} ", request.getMethod(), request.getRequestURI());
                accessDeniedHandler.handleRequest(request, response);
            }
            return valid;
        } else {
            return true;
        }
    }

    /**
     * 処理するリクエストの検証を行う必要があるかどうかチェックする
     *
     * @param handler
     * @return 処理する必要がある場合 true
     */
    boolean requestNeedsInspect(Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }
        //メソッドにマーカーアノテーションがついていたらチェックする
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        return handlerMethod.getMethodAnnotation(ProtectedByCsrfFilter.class) != null;
    }
}
