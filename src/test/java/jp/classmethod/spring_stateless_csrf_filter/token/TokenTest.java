package jp.classmethod.spring_stateless_csrf_filter.token;

import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class TokenTest {

    private final TokenSigner signer = new TokenSigner("SECRET_KEY");

    @Test
    public void testGenerate() throws Exception {
        //指定したsecretで生成される
        assertEquals("SECRET", Token.generate("SECRET").getSecret());
        //secretが同じでもnonceが異なるので、messageも異なる
        assertEquals(Token.generate("SECRET").getMessage(), Token.generate("SECRET").getMessage());

        //引数なしの場合はsercet, messageが毎回違う
        final Set<Token> tokens = IntStream.of(100)
                .boxed()
                .map(n->Token.generate())
                .collect(Collectors.toSet());

        for(Token tokenA: tokens){
            for(Token tokenB: tokens){
                if(tokenA != tokenB){
                    assertNotEquals(tokenA.getSecret(), tokenB.getSecret());
                    assertNotEquals(tokenA.getMessage(), tokenB.getMessage());
                }else{
                    assertEquals(tokenA.getSecret(), tokenB.getSecret());
                    assertEquals(tokenA.getMessage(), tokenB.getMessage());
                }
            }
        }
    }

    @Test
    public void testEncodeAndDecode(){
        final Token token = new Token("SECRET", "NONCE");
        final String encoded = Token.signAndEncode(signer, token);
        //encode
        assertEquals("f8b28a95368841901a1a3ed9eb2fb3b737994ac0-NONCE-SECRET", encoded);

        //decode
        final Token decodedToken = Token.decodeAndVerify(signer, encoded);
        assertEquals(token.getMessage(), decodedToken.getMessage());
        assertEquals(token.getSecret(), decodedToken.getSecret());
    }

    @Test(expected = InvalidTokenException.class)
    public void testDecodeFailWithFalseMessage(){
        Token.decodeAndVerify(signer, "f8b28a95368841901a1a3ed9eb2fb3b737994ac0-NONCEA-SECRET");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeFailWithInvalidFormat(){
        Token.decodeAndVerify(signer, "f8b28a95368841901a1a3ed9eb2fb3b737994ac0-NONCESECRET");
    }

    @Test
    public void testDecodeWithDashInMessage(){
        final Token token = new Token("--SECRET--", "NONCE");
        final String encoded = Token.signAndEncode(signer, token);
        //encode
        assertEquals("8b8239a67ab7952758414019c368fce090df174e-NONCE---SECRET--", encoded);

        //decode
        final Token decodedToken = Token.decodeAndVerify(signer, encoded);
        assertEquals(token.getMessage(), decodedToken.getMessage());
        assertEquals(token.getSecret(), decodedToken.getSecret());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nonceContainsDash(){
        new Token("---SECRET", "--NONCE");
    }




}