package com.entropy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/info")
    public ModelAndView info() {
        // int i = 1 / 0;
        ModelAndView mv = new ModelAndView();
        mv.setViewName("userInfo");
        mv.addObject("username", "kafka");
        return mv;
    }
}
