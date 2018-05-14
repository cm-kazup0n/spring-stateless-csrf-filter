package jp.classmethod.spring_stateless_csrf_filter.example.controller;

import jp.classmethod.spring_stateless_csrf_filter.example.model.entity.User;
import jp.classmethod.spring_stateless_csrf_filter.example.model.repository.UserRepository;
import jp.classmethod.spring_stateless_csrf_filter.spring.interceptor.ProtectedByCsrfFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("users", userRepository.all());
        return "users/index";
    }


    @RequestMapping(method = RequestMethod.GET, path = "/{userId}")
    public String show(@PathVariable String userId, Model model) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "users/show";
        } else {
            return "redirect:index";
        }

    }


    @RequestMapping(method = RequestMethod.POST)
    @ProtectedByCsrfFilter
    public String create(@ModelAttribute UserForm userForm) {
        final String id = userRepository.save(userForm.toUser());
        logger.info("user create with id={}", id);
        return String.format("redirect:users/%s", id);
    }

}
