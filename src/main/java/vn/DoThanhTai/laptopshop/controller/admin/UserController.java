package vn.DoThanhTai.laptopshop.controller.admin;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import vn.DoThanhTai.laptopshop.service.UserService;

@Controller
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getMethodName() {
        return this.userService.getUserName();
    }

}
