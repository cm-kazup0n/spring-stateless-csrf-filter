package jp.classmethod.spring_stateless_csrf_filter.token;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenSignerTest {

    private final TokenSigner signer = new TokenSigner("SECRET_KEY");

    private final String preDefinedSign = "f8b28a95368841901a1a3ed9eb2fb3b737994ac0";
    private final Token predefinedToken = new Token("SECRET", "NONCE");

    @Test
    public void testSign(){
        //生成する署名が期待値と同じ
        assertEquals(preDefinedSign, signer.sign(predefinedToken));

        final Token tokenA = Token.generate();
        final Token tokenB = Token.generate();
        //同じトークンには同じ署名が生成される
        assertEquals(signer.sign(tokenA), signer.sign(tokenA));
        //異なるトークには異なる署名が生成される
        assertNotEquals(signer.sign(tokenB), signer.sign(tokenA));
    }

    @Test
    public void testVerify_有効な署名(){
        signer.verify(predefinedToken, preDefinedSign);
    }

    @Test(expected = InvalidTokenException.class)
    public void testVerify_無効な署名(){
        signer.verify(Token.generate(), preDefinedSign);
    }

    @Test
    public void testCompareSafely(){
        //文字列長が異なる
        signer.compareSafely("abc", "abcd");
        //一致する
        signer.compareSafely("abc", "abc");
        //一致しない
        signer.compareSafely("abce", "abcd");
    }



}