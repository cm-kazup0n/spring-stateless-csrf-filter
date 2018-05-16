package jp.classmethod.spring_stateless_csrf_filter.thymeleaf2;

/**
 * CsrfTokenElementProcessorでトークン生成中に発生する例外
 */
public class CsrfTokenElementProcessorException extends RuntimeException {
    public CsrfTokenElementProcessorException(String message) {
        super(message);
    }
}
