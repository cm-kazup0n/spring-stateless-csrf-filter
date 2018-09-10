package jp.classmethod.spring_stateless_csrf_filter.token;

import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TokenTest {

    private final TokenSigner signer = new TokenSigner("SECRET_KEY");

    @Test
    public void testGenerate() throws Exception {
        //指定したsecretで生成される
        assertEquals("SECRET", Token.Builder.generate("SECRET").getPayload());
        //secretが同じでもnonceが異なるので、messageも異なる
        assertEquals(Token.Builder.generate("SECRET").getMessage(), Token.Builder.generate("SECRET").getMessage());

        //引数なしの場合はsercet, messageが毎回違う
        final Set<Token> tokens = IntStream.of(100)
                .boxed()
                .map(n -> Token.Builder.generate())
                .collect(Collectors.toSet());

        for (Token tokenA : tokens) {
            for (Token tokenB : tokens) {
                if (tokenA != tokenB) {
                    assertNotEquals(tokenA.getPayload(), tokenB.getPayload());
                    assertNotEquals(tokenA.getMessage(), tokenB.getMessage());
                } else {
                    assertEquals(tokenA.getPayload(), tokenB.getPayload());
                    assertEquals(tokenA.getMessage(), tokenB.getMessage());
                }
            }
        }
    }

    @Test
    public void testEncodeAndDecode() {
        final Token token = new Token("SECRET", "NONCE");
        final String encoded = Token.SerDe.signAndEncode(signer, token);
        //encode
        assertEquals("f8b28a95368841901a1a3ed9eb2fb3b737994ac0-NONCE-SECRET", encoded);

        //decode
        final Token decodedToken = Token.SerDe.decodeAndVerify(signer, encoded);
        assertEquals(token.getMessage(), decodedToken.getMessage());
        assertEquals(token.getPayload(), decodedToken.getPayload());
    }

    @Test(expected = InvalidTokenException.class)
    public void testDecodeFailWithFalseMessage() {
        Token.SerDe.decodeAndVerify(signer, "f8b28a95368841901a1a3ed9eb2fb3b737994ac0-NONCEA-SECRET");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeFailWithInvalidFormat() {
        Token.SerDe.decodeAndVerify(signer, "f8b28a95368841901a1a3ed9eb2fb3b737994ac0-NONCESECRET");
    }

    @Test
    public void testDecodeWithDashInMessage() {
        final Token token = new Token("--SECRET--", "NONCE");
        final String encoded = Token.SerDe.signAndEncode(signer, token);
        //encode
        assertEquals("8b8239a67ab7952758414019c368fce090df174e-NONCE---SECRET--", encoded);

        //decode
        final Token decodedToken = Token.SerDe.decodeAndVerify(signer, encoded);
        assertEquals(token.getMessage(), decodedToken.getMessage());
        assertEquals(token.getPayload(), decodedToken.getPayload());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nonceContainsDash() {
        new Token("---SECRET", "--NONCE");
    }


}