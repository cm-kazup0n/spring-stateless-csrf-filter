package jp.classmethod.spring_stateless_csrf_filter.util;

import java.util.Optional;
import java.util.function.Function;

public final class Option {

    private Option() {
    }

    public static <A, B, C> Optional<C> map2(Optional<A> a, Optional<B> b, Function<Tuple<A, B>, C> mapper) {
        return a.flatMap(aa -> b.map(bb -> new Tuple<>(aa, bb))).map(mapper);
    }

    public static final class Tuple<A, B> {
        public final A _1;
        public final B _2;

        private Tuple(A a, B b) {
            this._1 = a;
            this._2 = b;
        }

    }


}
