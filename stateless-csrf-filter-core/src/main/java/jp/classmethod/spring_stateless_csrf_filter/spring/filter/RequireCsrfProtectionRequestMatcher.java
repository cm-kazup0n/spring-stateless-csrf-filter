package jp.classmethod.spring_stateless_csrf_filter.spring.filter;

import javax.servlet.http.HttpServletRequest;

/**
 * リクエストをCSRFトークンによって検証する必要があるかどうかチェックするマッチャー
 */
public interface RequireCsrfProtectionRequestMatcher {

    /**
     * @param request
     * @return チェックする場合 true
     */
    boolean matches(HttpServletRequest request);

}
