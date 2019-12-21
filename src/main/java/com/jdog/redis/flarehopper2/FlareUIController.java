package com.jdog.redis.flarehopper2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FlareUIController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
