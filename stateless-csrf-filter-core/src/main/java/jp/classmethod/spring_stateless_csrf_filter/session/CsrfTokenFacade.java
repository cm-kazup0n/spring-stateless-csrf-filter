package jp.classmethod.spring_stateless_csrf_filter.session;

import jp.classmethod.spring_stateless_csrf_filter.token.Token;
import jp.classmethod.spring_stateless_csrf_filter.token.TokenSigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class CsrfTokenFacade {

    @Autowired
    private SessionProvider sessionProvider;
    @Autowired
    private TokenSigner tokenSigner;
    @Autowired
    private String csrfTokenName;


    public String getCsrfTokenName(){ return csrfTokenName;}


    public Optional<String> populateCsrfToken(final HttpServletRequest request, final boolean create) {
        return sessionProvider.get(request, create).map(session -> {
            final String token = Token.SerDe.signAndEncode(tokenSigner, Token.Builder.generate());
            session.put(csrfTokenName, token);
            return token;
        });
    }

}
