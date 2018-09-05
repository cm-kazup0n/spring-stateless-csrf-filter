package jp.classmethod.spring_stateless_csrf_filter.session;

import jp.classmethod.spring_stateless_csrf_filter.token.Token;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import jp.classmethod.spring_stateless_csrf_filter.util.Option;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * セッションへのCSRFトークンの埋め込みと検証を行うファサード
 */
public class CsrfTokenFacade {

    private SessionProvider sessionProvider;
    private TokenSigner tokenSigner;
    private String csrfTokenName;

    public CsrfTokenFacade(SessionProvider sessionProvider, TokenSigner tokenSigner, String csrfTokenName) {
        this.sessionProvider = sessionProvider;
        this.tokenSigner = tokenSigner;
        this.csrfTokenName = csrfTokenName;
    }

    /**
     * @return トークンのリクエストパラメータ名
     */
    public String getCsrfTokenName() {
        return csrfTokenName;
    }

    /**
     *  requestから既存のセッションを取得(or 生成）してCSRFトークンを生成する
     * @param request 処理中のリクエスト
     * @param response 処理中のレスポンス
     * @param create trueならセッションが未生成の場合は生成する
     * @return CSRFトークン
     */
    public Optional<String> populateCsrfToken(final HttpServletRequest request, final HttpServletResponse response, final boolean create) {
        //現在のセッションを取得(or 生成)する
        return sessionProvider.get(request, create).map(session -> {
            //新しいトークンを生成
            final String token = Token.SerDe.signAndEncode(tokenSigner, Token.Builder.generate());
            //セッションに保存
            sessionProvider.flush(response, session.put(csrfTokenName, token));
            return token;
        });
    }

    /**
     * リクエストとして受信したトークンのバリデーションを行う
     * @param request
     * @return バリデーション結果 trueなら有効なトークン
     */
    public Optional<Boolean> validate(final HttpServletRequest request) {
        //リクエストパラメータからトークンを取得する
        final Optional<Token> parameter = Optional.ofNullable(request.getParameter(csrfTokenName)).flatMap(token -> Token.SerDe.decodeAndVerifyByOptional(tokenSigner, token));
        //セッションからもトークンを取得
        final Optional<Token> cookie = sessionProvider.get(request, false).flatMap(session -> session.get(csrfTokenName)).flatMap(token -> Token.SerDe.decodeAndVerifyByOptional(tokenSigner, token));
        //比較する
        return Option.map2(parameter, cookie, tuple -> tuple._1.compareSafely(tuple._2));
    }


}
