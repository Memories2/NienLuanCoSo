package vn.DoThanhTai.laptopshop.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import vn.DoThanhTai.laptopshop.domain.User;
import vn.DoThanhTai.laptopshop.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        model.addAttribute("username", "tai");
        return this.userService.getUserName();
    }

    @GetMapping("/admin/user")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User()); // Bắt buộc phải có để tránh lỗi
        model.addAttribute("username", "tai");
        return "admin/user/create";
    }

    @PostMapping("/admin/user/create")
    public String postCreateUser(Model model, @ModelAttribute("newUser") User newUser) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>"+newUser);
        
        return  "hello";
    }
    
}
