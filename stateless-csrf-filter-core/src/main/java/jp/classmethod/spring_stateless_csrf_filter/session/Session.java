package jp.classmethod.spring_stateless_csrf_filter.session;

import java.util.Optional;

/**
 * KV形式で情報を保存するセッション
 * Immutableなのでputすると新たなインスタンスが生成される
 */
public interface Session {

    /**
     * keyに紐づけてvalueを保存する
     * @param key キー
     * @param value 値
     * @return 値を追加したSession
     */
    Session put(String key, String value);

    /**
     * 値を取得する
     * @param key
     * @return 値のOption
     */
    Optional<String> get(String key);

}
