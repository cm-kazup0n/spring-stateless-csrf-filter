package jp.classmethod.spring_stateless_csrf_filter.token;

import org.apache.commons.codec.binary.Hex;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

/**
 * 署名付きトークン
 */
public class Token {


    /**
     * トークンのペイロード
     */
    private final String payload;

    /**
     * ナンス
     */
    private final String nonce;

    Token(String payload, String nonce) {
        if (nonce.contains("-")) {
            throw new IllegalArgumentException("nonce must not contains any `-`");
        }
        this.payload = payload;
        this.nonce = nonce;
    }

    public String getMessage() {
        return nonce + "-" + payload;
    }

    public String getPayload() {
        return payload;
    }

    /**
     * 他のトークンと比較する
     * @param other
     * @return 一致した場合true
     */
    public boolean compareSafely(Token other) {
        return TokenSigner.compareSafely(getMessage(), other.getMessage());
    }

    /**
     * シリアライザ/デシリアライザ
     */
    public static class SerDe {

        private SerDe() {
        }

        /**
         * 署名付きのトークン文字列を生成する
         * @param signer 署名
         * @param token 署名するトークン
         * @return 署名付きのトークン文字列
         */
        public static String signAndEncode(TokenSigner signer, final Token token) {
            return signer.sign(token) + "-" + token.getMessage();
        }

        /**
         * 署名付きのトークン文字列を検証,デコードする
         * @param signer 署名
         * @param raw 署名付きトークン
         * @return トークン
         * @throws InvalidTokenException トークンが不正な場合
         */
        public static Token decodeAndVerify(TokenSigner signer, final String raw) {
            //署名, ペイロード、nonceに分割する
            final String[] parts = raw.split("-", 3);
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid raw token: " + raw);
            }
            final String sign = parts[0];
            final String nonce = parts[1];
            final String payload = parts[2];
            final Token token = new Token(payload, nonce);
            signer.verify(token, sign);
            return token;
        }

        /**
         * 署名付きのトークン文字列を検証,デコードする
         * @param signer 署名
         * @param raw 署名付きトークン
         * @return トークン. 検証に失敗した場合はempty
         */
        public static Optional<Token> decodeAndVerifyByOptional(TokenSigner signer, final String raw) {
            try {
                return Optional.of(decodeAndVerify(signer, raw));
            } catch (InvalidTokenException e) {
                return Optional.empty();
            }
        }

    }

    /**
     * トークンの生成を行うビルダー
     */
    public static class Builder {

        private Builder() {
        }

        /**
         * ペイロードにランダムな値を含むトークンを生成する
         * @return トークン
         */
        public static final Token generate() {
            byte[] bytes = new byte[12];
            try {
                SecureRandom.getInstanceStrong().nextBytes(bytes);
                return generate(Hex.encodeHexString(bytes));
            } catch (NoSuchAlgorithmException e) {
                throw new TokenGenerationException(e);
            }
        }

        /**
         * 指定したペイロードでトークンを生成する
         * @param payload
         * @return トークン
         */
        public static final Token generate(final String payload) {
            final String nonce = Long.toString(System.currentTimeMillis());
            return new Token(payload, nonce);
        }
    }


}
