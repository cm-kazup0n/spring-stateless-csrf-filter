package jp.classmethod.spring_stateless_csrf_filter.session;

import java.util.Optional;

public interface Session<E extends Session> {

    E put(String key, String value);

    Optional<String> get(String key);

}
