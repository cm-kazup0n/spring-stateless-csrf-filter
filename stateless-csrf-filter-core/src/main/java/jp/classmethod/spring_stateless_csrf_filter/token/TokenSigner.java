package jp.classmethod.spring_stateless_csrf_filter.token;

import org.apache.commons.codec.digest.HmacUtils;

import java.nio.charset.StandardCharsets;

/**
 * トークンの署名
 */
public class TokenSigner {

    /**
     * 署名時のキー
     */
    private final String key;

    public TokenSigner(String key) {
        this.key = key;
    }

    /**
     * a,bを比較する。２つの文字列の長さ違いによらず比較時間は一定になる
     *
     * @param a
     * @param b
     * @return 一致している場合 true
     */
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

    /**
     * @param token 署名対象のトークン
     * @return 署名
     */
    String sign(final Token token) {
        return HmacUtils.hmacSha1Hex(key, token.getMessage());
    }

    /**
     * tokenから署名を生成し, signと比較する
     *
     * @param token
     * @param sign
     * @throws InvalidTokenException 検証に失敗した場合
     */
    void verify(final Token token, String sign) {
        final String expected = sign(token);
        if (!compareSafely(expected, sign)) {
            throw new InvalidTokenException("Invalid token.");
        }
    }

}
