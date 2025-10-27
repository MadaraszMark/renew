package hu.renew.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/store")
    public String store() {
        return "store";
    }

    @GetMapping("/messages")
    public String product() {
        return "messages";
    }

    @GetMapping("/contact")
    public String checkout() {
        return "contact";
    }

    @GetMapping("/about")
    public String blank() {
        return "about";
    }
    
    @GetMapping("/chart")
    public String chart() {
        return "chart";
    }
    
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }
}

