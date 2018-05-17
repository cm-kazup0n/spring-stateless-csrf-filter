package jp.classmethod.spring_stateless_csrf_filter.example_thymeleaf2.infra;

import java.util.Collection;
import java.util.Optional;

public interface EntityStore<E> {


    void save(String id, E e);

    Optional<E> findById(String id);


    Collection<E> all();

}
