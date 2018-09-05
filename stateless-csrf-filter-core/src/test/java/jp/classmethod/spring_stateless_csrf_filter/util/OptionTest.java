package jp.classmethod.spring_stateless_csrf_filter.util;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void map2(){
        assertFalse(
                Option.map2(Optional.<String>empty(), Optional.<String>empty(), (tup) -> {
                    throw new AssertionError();
                }).isPresent());

         assertFalse(
                Option.map2(Optional.of("present"), Optional.<String>empty(), (tup) -> {
                    throw new AssertionError();
                }).isPresent());

         assertEquals("ab", Option.map2(Optional.of("a"), Optional.of("b"), (tup)-> tup._1 + tup._2).get());
    }


}