package jp.classmethod.spring_stateless_csrf_filter.token;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenSignerTest {

    private static final String preDefinedSign = "f8b28a95368841901a1a3ed9eb2fb3b737994ac0";

    private static final TokenSigner signer = new TokenSigner("SECRET_KEY");

    private static final Token predefinedToken = new Token("SECRET", "NONCE");

    @Test
    public void testSign() {
        //生成する署名が期待値と同じ
        assertEquals(preDefinedSign, signer.sign(predefinedToken));

        final Token tokenA = Token.Builder.generate();
        final Token tokenB = Token.Builder.generate();
        //同じトークンには同じ署名が生成される
        assertEquals(signer.sign(tokenA), signer.sign(tokenA));
        //異なるトークには異なる署名が生成される
        assertNotEquals(signer.sign(tokenB), signer.sign(tokenA));
    }

    @Test
    public void testVerify_有効な署名() {
        signer.verify(predefinedToken, preDefinedSign);
    }

    @Test(expected = InvalidTokenException.class)
    public void testVerify_無効な署名() {
        signer.verify(Token.Builder.generate(), preDefinedSign);
    }

    @Test
    public void testCompareSafely() {
        //文字列長が異なる
        assertFalse(TokenSigner.compareSafely("abc", "abcd"));
        //一致する
        assertTrue(TokenSigner.compareSafely("abc", "abc"));
        //一致しない
        assertFalse(TokenSigner.compareSafely("abce", "abcd"));
    }


}