package jp.classmethod.spring_stateless_csrf_filter.token;

/**
 * トークン生成中に発生したエラー
 */
public class TokenGenerationException extends RuntimeException {
    public TokenGenerationException(Throwable t) {
        super(t);
    }
}
