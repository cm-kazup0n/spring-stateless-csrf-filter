package jp.classmethod.spring_stateless_csrf_filter.session;

import jp.classmethod.spring_stateless_csrf_filter.token.Token;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import jp.classmethod.spring_stateless_csrf_filter.util.Option;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class CsrfTokenFacade {

    private SessionProvider sessionProvider;
    private TokenSigner tokenSigner;
    private String csrfTokenName;

    public CsrfTokenFacade(SessionProvider sessionProvider, TokenSigner tokenSigner, String csrfTokenName) {
        this.sessionProvider = sessionProvider;
        this.tokenSigner = tokenSigner;
        this.csrfTokenName = csrfTokenName;
    }

    public String getCsrfTokenName() {
        return csrfTokenName;
    }


    public Optional<String> populateCsrfToken(final HttpServletRequest request, final HttpServletResponse response, final boolean create) {
        return sessionProvider.get(request, create).map(session -> {
            final String token = Token.SerDe.signAndEncode(tokenSigner, Token.Builder.generate());
            sessionProvider.flush(response, session.put(csrfTokenName, token));
            return token;
        });
    }

    public Optional<Boolean> validate(final HttpServletRequest request) {
        final Optional<Token> parameter = Optional.ofNullable(request.getParameter(csrfTokenName)).flatMap(token -> Token.SerDe.decodeAndVerifyByOptional(tokenSigner, token));
        final Optional<Token> cookie = sessionProvider.get(request, false).flatMap(session -> session.get(csrfTokenName)).flatMap(token -> Token.SerDe.decodeAndVerifyByOptional(tokenSigner, token));
        return Option.map2(parameter, cookie, tuple -> tuple._1.compareSafely(tuple._2));
    }


}
