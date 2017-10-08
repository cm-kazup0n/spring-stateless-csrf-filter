package jp.classmethod.spring_stateless_csrf_filter.token;

public class TokenGenerationException extends RuntimeException {
    public TokenGenerationException(Throwable t) {
        super(t);
    }
}
