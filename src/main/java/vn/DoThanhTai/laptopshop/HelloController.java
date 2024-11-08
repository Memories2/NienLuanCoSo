package vn.DoThanhTai.laptopshop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public String index() {
        return "Hello World from Spring Boot!";
    }

    @GetMapping("/user")
    public String user() {
        return "Hello World from user!";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Hello World from admin!";
    }
}
