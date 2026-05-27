package com.example.eureka;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EurekaRootRedirectController {

    @GetMapping({"/eureka", "/eureka/"})
    public String redirectToDashboard() {
        return "redirect:/";
    }
}