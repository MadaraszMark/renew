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

    @GetMapping("/product")
    public String product() {
        return "product";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }

    @GetMapping("/about")
    public String blank() {
        return "about";
    }
}

