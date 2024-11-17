package vn.DoThanhTai.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.DoThanhTai.laptopshop.domain.User;
import vn.DoThanhTai.laptopshop.repository.UserRepository;
import vn.DoThanhTai.laptopshop.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
    }

    @GetMapping("/admin/user/show")
    public String getHomePage(Model model) {
        List<User> users = this.userService.handleGetAllUsers();
        model.addAttribute("users", users);
        return "/admin/user/show";
    }

    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User()); // Bắt buộc phải có để tránh lỗi
        model.addAttribute("username", "tai");
        return "admin/user/create";
    }

    @PostMapping("/admin/user/create")
    public String postCreateUser(Model model, @ModelAttribute("newUser") User newUser) {
        this.userService.handleSaveUser(newUser);
        return "redirect:/admin/user/show"; // Chuyển hướng về trang chủ Cách 1 sử dụng URL
        // return "redirect:show"; // Chuyển hướng về trang show Cách 2 sử dụng tên file
    }

    @GetMapping("/admin/user/view/{id}") // Get xem chi tiết một user
    public String getViewUserPage(Model model, @PathVariable("id") Long id) {
        User user = this.userService.handleGetUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }
}


