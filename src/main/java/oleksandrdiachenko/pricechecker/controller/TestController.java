package oleksandrdiachenko.pricechecker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/")
    public String testMethod() {
        return "test";
    }

}
