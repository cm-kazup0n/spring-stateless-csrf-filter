package jp.classmethod.spring_stateless_csrf_filter.token;

import org.apache.commons.codec.digest.HmacUtils;

import java.nio.charset.StandardCharsets;

public class TokenSigner {

    private final String key;

    public TokenSigner(String key) {
        this.key = key;
    }

    static boolean compareSafely(String a, String b) {
        final byte[] digesta = a.getBytes(StandardCharsets.UTF_8);
        final byte[] digestb = b.getBytes(StandardCharsets.UTF_8);

        if (digesta.length != digestb.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < digesta.length; i++) {
            result |= digesta[i] ^ digestb[i];
        }
        return result == 0;
    }

    String sign(final Token token) {
        return HmacUtils.hmacSha1Hex(key, token.getMessage());
    }

    void verify(final Token token, String sign) {
        final String expected = sign(token);
        if (!compareSafely(expected, sign)) {
            throw new InvalidTokenException();
        }
    }

}
