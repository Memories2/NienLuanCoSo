package vn.DoThanhTai.laptopshop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.DoThanhTai.laptopshop.domain.User;
import vn.DoThanhTai.laptopshop.service.UploadService;
import vn.DoThanhTai.laptopshop.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserController {

    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UploadService uploadService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    ///////////////////////// All User View //////////////////////////
    @GetMapping("/admin/user")
    public String getHomePage(Model model) {
        List<User> users = this.userService.handleGetAllUsers();
        model.addAttribute("users", users);
        return "/admin/user/show";
    }

    ///////////////////////// Create User //////////////////////////
    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User()); // Bắt buộc phải có để tránh lỗi
        return "admin/user/create";
    }

    @PostMapping("/admin/user/create")
    public String postCreateUser(Model model, @ModelAttribute("newUser") User newUser, @RequestParam("hoidanitFile") MultipartFile file) {
        
   
        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        String hashPassWord = this.passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashPassWord);
        newUser.setAvatar(avatar);
        this.userService.handleSaveUser(newUser);
        return "redirect:/admin/user"; // Chuyển hướng về trang chủ Cách 1 sử dụng URL
        // return "redirect:show"; // Chuyển hướng về trang show Cách 2 sử dụng tên file
    }

    ///////////////////////// View User Detail //////////////////////////
    @GetMapping("/admin/user/view/{id}") // Get xem chi tiết một user
    public String getViewUserPage(Model model, @PathVariable("id") Long id) {
        User user = this.userService.handleGetUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }

    ///////////////////////// Update User //////////////////////////

    @GetMapping("/admin/user/update/{id}")
    public String getUpdaterUserPage(Model model, @PathVariable("id") Long id) {
        User currentUser = this.userService.handleGetUserById(id);
        model.addAttribute("currentUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("currentUser") User currentUser) {
        User oldUser = this.userService.handleGetUserById(currentUser.getId());
        if (oldUser != null) {
            oldUser.setFullName(currentUser.getFullName());
            oldUser.setPhone(currentUser.getPhone());
            oldUser.setAddress(currentUser.getAddress());
            this.userService.handleUpdateUser(oldUser);
        }
        return "redirect:/admin/user/show";
    }

    ///////////////////////// Delete User //////////////////////////

    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable("id") Long id) {
        User currentUser = new User();
        currentUser.setId(id);
        model.addAttribute("id", id);
        model.addAttribute("currentUser", currentUser);
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("currentUser") User currentUser) {
        User oldUser = this.userService.handleGetUserById(currentUser.getId());
        if (oldUser != null) {
            this.userService.handleDeleteUser(oldUser.getId());
        }
        return "redirect:/admin/user/show";
    }
}
