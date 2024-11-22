package vn.DoThanhTai.laptopshop.controller.client;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import vn.DoThanhTai.laptopshop.domain.Product;
import vn.DoThanhTai.laptopshop.domain.Role;
import vn.DoThanhTai.laptopshop.domain.User;
import vn.DoThanhTai.laptopshop.domain.dto.RegisterDTO;
import vn.DoThanhTai.laptopshop.service.ProductService;
import vn.DoThanhTai.laptopshop.service.UserService;



@Controller
public class HomePageController {

    private final ProductService productService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public HomePageController(ProductService productService, UserService userService, PasswordEncoder passwordEncoder) {
        this.productService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> products = productService.getAllProduct();
        model.addAttribute("products", products);
        return "client/homepage/show";
    }

    //////////////////////////////// Register ////////////////////////////////
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerUser") RegisterDTO registerUser) {
        User user = this.userService.registerDTOtoUser(registerUser);
        // Hash password
        String hashedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        // Set role
        Role role = this.userService.getRoleByName("USER");
        user.setRole(role);
        // Save user
        this.userService.handleSaveUser(user);
        return "redirect:/login";
    }

    //////////////////////////////// Login ////////////////////////////////
    @GetMapping("/login")
    public String getLoginPage() {
        return "client/auth/login";
    }
}
