package jp.classmethod.spring_stateless_csrf_filter.e2e.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @RequestMapping(path = "/secured", method = RequestMethod.GET)
    public String getSecured(){
        return "GET secured/index";
    }

    @RequestMapping(path = "/secured", method = RequestMethod.POST)
    public String postSecured(){
        return "POST secured/index";
    }

}
