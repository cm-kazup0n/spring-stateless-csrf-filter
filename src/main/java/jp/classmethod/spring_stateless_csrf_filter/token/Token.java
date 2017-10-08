package jp.classmethod.spring_stateless_csrf_filter.token;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Token {


    private final String secret;
    private final String nonce;

    Token(String secret, String nonce){
        this.secret = secret;
        this.nonce = nonce;
    }

    public String getMessage(){
        return nonce + "-" + secret;
    }

    public String getSecret() {
        return secret;
    }

    public static String signAndEncode(TokenSigner signer, final Token token){
        return signer.sign(token) + "-" + token.getMessage();
    }

    public static Token decodeAndVerify(TokenSigner signer, final String raw){
        String[] parts = raw.split("-");
        if(parts.length != 3){
            throw new IllegalArgumentException("Invalid raw token: " + raw);
        }
        final String sign = parts[0];
        final String nonce = parts[1];
        final String secret = parts[2];
        final Token token = new Token(secret, nonce);
        signer.verify(token, sign);
        return token;
    }


    public static final Token generate(){
        byte[] bytes = new byte[12];
        try {
            SecureRandom.getInstanceStrong().nextBytes(bytes);
            return generate(new String(bytes, StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new TokenGenerationException(e);
        }
    }

    public static final Token generate(final String payload){
        final String nonce = Long.toString(System.currentTimeMillis());
        return new Token(payload, nonce);
    }
}
