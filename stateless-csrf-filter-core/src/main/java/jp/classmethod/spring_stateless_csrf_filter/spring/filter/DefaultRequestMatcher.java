package jp.classmethod.spring_stateless_csrf_filter.spring.filter;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * デフォルトのリクエストマッチャー
 */
public class DefaultRequestMatcher implements RequireCsrfProtectionRequestMatcher {


    /**
     * マッチ対象のHTTPメソッド(デフォルト)
     */
    private static final Set<HttpMethod> DEFAULT_METHODS;

    static {
        Set<HttpMethod> methods = new HashSet<>();
        methods.add(HttpMethod.POST);
        methods.add(HttpMethod.PATCH);
        methods.add(HttpMethod.PUT);
        methods.add(HttpMethod.DELETE);
        DEFAULT_METHODS = Collections.unmodifiableSet(methods);
    }

    /**
     * マッチ対象のメソッド
     */
    private final Set<HttpMethod> methodsToMatch;
    /**
     * マッチ対象のパス(antのパスマッチャーの記法で指定できる)
     */
    private final Set<String> pathPatternsToMatch;
    private final AntPathMatcher matcher = new AntPathMatcher();
    {
        matcher.setCachePatterns(true);
    }

    /**
     *
     * @param methodsToMatch  マッチ対象のメソッド
     * @param pathPatternsToMatch マッチ対象のパス(antのパスマッチャーの記法で指定できる)
     */
    public DefaultRequestMatcher(Set<HttpMethod> methodsToMatch, Set<String> pathPatternsToMatch) {
        Assert.notEmpty(methodsToMatch, "methodsToMatch needs to be not empty");
        Assert.notEmpty(pathPatternsToMatch, "pathPatternsToMatch needs to be not empty");

        this.methodsToMatch = Collections.unmodifiableSet(methodsToMatch);
        this.pathPatternsToMatch = Collections.unmodifiableSet(pathPatternsToMatch);
    }

    /**
     * @param pathPatternsToMatch マッチ対象のパス(antのパスマッチャーの記法で指定できる)
     */
    public DefaultRequestMatcher(Set<String> pathPatternsToMatch) {
        this(DEFAULT_METHODS, pathPatternsToMatch);
    }


    @Override
    public boolean matches(HttpServletRequest request) {
        if (!methodsToMatch.contains(HttpMethod.valueOf(request.getMethod()))) {
            return false;
        }
        for (String pattern : pathPatternsToMatch) {
            if (matcher.match(pattern, request.getRequestURI())) {
                return true;
            }
        }

        return false;
    }


    public static class Builder {

        private final Set<HttpMethod> methodsToMatch;
        private final Set<String> pathPatternsToMatch;

        private Builder() {
            methodsToMatch = new HashSet<>();
            pathPatternsToMatch = new HashSet<>();
        }

        /**
         * フルカスタマイズしたマッチャーを生成するビルダー
         * @return ビルダー
         */
        public static Builder custom() {
            return new Builder();
        }

        /**
         * パスだけを指定するビルダー
         * @param pathPatterns マッチするパターン
         * @return ビルダー
         */
        public static RequireCsrfProtectionRequestMatcher create(String... pathPatterns) {
            return new DefaultRequestMatcher(new HashSet<>(Arrays.asList(pathPatterns)));
        }

        /**
         * マッチするメソッドを追加する
         * @param methods
         * @return
         */
        public Builder addMethods(HttpMethod... methods) {
            for (HttpMethod method : methods) {
                methodsToMatch.add(method);
            }
            return this;
        }

        /**
         * マッチするパターンを追加する
         * @param pattern
         * @return
         */
        public Builder addPattern(String pattern) {
            pathPatternsToMatch.add(pattern);
            return this;
        }

        /**
         * マッチャーを生成する
         * @return
         */
        public RequireCsrfProtectionRequestMatcher build() {
            return new DefaultRequestMatcher(methodsToMatch, pathPatternsToMatch);
        }


    }

}
