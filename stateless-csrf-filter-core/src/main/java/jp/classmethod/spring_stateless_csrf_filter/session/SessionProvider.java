package jp.classmethod.spring_stateless_csrf_filter.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * HttpServletRequestからのセッションの生成と保存を行うアダプタ
 */
public interface SessionProvider {

    /**
     * リクエストからセッションを取得/生成する
     * @param request 処理中のリクエスト
     * @param create セッションがない場合生成するならtrue
     * @return セッション
     */
    Optional<Session> get(HttpServletRequest request, boolean create);

    /**
     * セッション情報を保存する
     * @param response 保存先のリクエスト
     * @param session セッション
     */
    void flush(HttpServletResponse response, Session session);

}
