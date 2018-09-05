package jp.classmethod.spring_stateless_csrf_filter.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CSRFトークンの検証に失敗した時のレスポンスを生成するハンドラ
 */
public interface AccessDeniedHandler {

    /**
     * トークン検証失敗した際の応答をする
     *
     * @param request
     * @param response
     */
    void handleRequest(HttpServletRequest request, HttpServletResponse response);
}
