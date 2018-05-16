package jp.classmethod.spring_stateless_csrf_filter.token;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(final String message){
        super(message);
    }
}
