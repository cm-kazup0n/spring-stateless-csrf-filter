package jp.classmethod.spring_stateless_csrf_filter.example_thymeleaf3.infra;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemStore<E> implements EntityStore<E> {

    private final ConcurrentHashMap<String, E> items = new ConcurrentHashMap<>();

    @Override
    public void save(String id, E e) {
        items.put(id, e);
    }

    @Override
    public Optional<E> findById(String id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Collection<E> all() {
        return items.values();
    }


}
