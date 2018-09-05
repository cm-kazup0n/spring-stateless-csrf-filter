package jp.classmethod.spring_stateless_csrf_filter.session;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * URLエンコード、デコード用のヘルパー
 */
final class URLEncodeSupport {

    private URLEncodeSupport() {
    }

    /**
     * sをURLエンコードする
     *
     * @param s エンコードする文字列
     * @return エンコード済みの文字列
     */
    static String encode(String s) {
        try {
            return URLEncoder.encode(s, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param s URLエンコードされた文字列
     * @return デコード済みの文字列
     */
    static String decode(String s) {
        try {
            return URLDecoder.decode(s, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


}
