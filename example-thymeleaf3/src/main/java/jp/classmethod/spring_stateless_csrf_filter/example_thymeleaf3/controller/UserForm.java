package jp.classmethod.spring_stateless_csrf_filter.example_thymeleaf3.controller;

import jp.classmethod.spring_stateless_csrf_filter.example_thymeleaf3.model.entity.User;

public class UserForm {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public User toUser() {
        return new User(name);
    }

}
