package jp.classmethod.spring_stateless_csrf_filter.example_thymeleaf3.model.repository;

import jp.classmethod.spring_stateless_csrf_filter.example_thymeleaf3.infra.EntityStore;
import jp.classmethod.spring_stateless_csrf_filter.example_thymeleaf3.model.entity.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class UserRepository implements InitializingBean {

    @Autowired
    private EntityStore<User> userEntityStore;


    public String save(User user) {
        userEntityStore.save(user.getId(), user);
        return user.getId();
    }

    public Optional<User> findById(String id) {
        return userEntityStore.findById(id);
    }


    public Collection<User> all() {
        return userEntityStore.all();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        save(new User("john"));
        save(new User("paul"));
        save(new User("george"));
        save(new User("ringo"));
    }
}
