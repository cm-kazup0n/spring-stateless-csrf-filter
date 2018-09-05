package jp.classmethod.spring_stateless_csrf_filter.session;

import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;

import javax.servlet.http.HttpServletResponse;

/**
 * HttpServletResponseへCookieSession用のCookieを追加するオブジェクト
 */
public interface SessionCookieBaker {

    /**
     * responseへsessionのクッキーを追加する
     * @param response 追加先のレスポンス
     * @param signer 署名
     * @param session セッション
     */
    void addCookie(HttpServletResponse response, TokenSigner signer, CookieSession session);

    /**
     * @return このオブジェクトが生成するクッキーのキー名
     */
    String getCookieName();

}
