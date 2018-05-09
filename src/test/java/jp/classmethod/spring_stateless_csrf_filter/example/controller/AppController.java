package jp.classmethod.spring_stateless_csrf_filter.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class AppController {


    @RequestMapping(method = RequestMethod.GET, path = "/")
   public String index(){
        return "index";
    }

}
