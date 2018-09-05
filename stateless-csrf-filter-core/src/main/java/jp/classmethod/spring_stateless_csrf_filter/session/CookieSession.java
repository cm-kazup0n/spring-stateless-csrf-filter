package jp.classmethod.spring_stateless_csrf_filter.session;

import jp.classmethod.spring_stateless_csrf_filter.token.Token;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Cookieに情報を保存するセッションの実装
 */
public class CookieSession implements Session {

    private final Map<String, String> values;

    private CookieSession(Map<String, String> values) {
        this.values = Collections.unmodifiableMap(values);
    }

    /**
     * 空のセッションを生成する
     * @return Session
     */
    public static Session create() {
        return new CookieSession(Collections.emptyMap());
    }


    @Override
    public Session put(String key, String value) {
        Map<String, String> copy = new HashMap<>(values);
        copy.put(key, value);
        return new CookieSession(copy);
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(values.get(key));
    }


    public static class SerDe {

        private SerDe() {
        }

        public static String serialize(TokenSigner signer, CookieSession session) {
            final List<String> values = new ArrayList<>(session.values.size());
            for (Map.Entry<String, String> entry : session.values.entrySet()) {
                final String key = URLEncodeSupport.encode(entry.getKey());
                final String val = URLEncodeSupport.encode(entry.getValue());
                values.add(key + "=" + val);
            }
            final Token token = Token.Builder.generate(String.join("&", values));
            return Token.SerDe.signAndEncode(signer, token);
        }

        public static CookieSession deserialize(TokenSigner signer, String raw) {
            final Token token = Token.SerDe.decodeAndVerify(signer, raw);
            final Map<String, String> values;
            values = Stream
                    .of(token.getSecret().split("&"))
                    .map(s -> s.split("="))
                    .collect(Collectors.toMap(
                            kv -> URLEncodeSupport.decode(kv[0]),
                            kv -> URLEncodeSupport.decode(kv[1])
                    ));
            return new CookieSession(values);
        }
    }

}
