package jp.classmethod.spring_stateless_csrf_filter;

import jp.classmethod.spring_stateless_csrf_filter.token.Token;
import org.junit.Test;

public class TokenTest {
    @Test
    public void generate() throws Exception {
        Token.generate();
    }

}