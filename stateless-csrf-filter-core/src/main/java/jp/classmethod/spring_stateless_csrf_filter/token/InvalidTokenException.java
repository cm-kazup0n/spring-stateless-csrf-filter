package jp.classmethod.spring_stateless_csrf_filter.token;

/**
 * トークン検証に失敗した場合の例外
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(final String message) {
        super(message);
    }
}
