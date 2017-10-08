package jp.classmethod.spring_stateless_csrf_filter.session;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SessionProvider<E extends Session> {

    Optional<E> get(HttpServletRequest  request, boolean create);

    void flush(HttpServletResponse response, E session);

}
