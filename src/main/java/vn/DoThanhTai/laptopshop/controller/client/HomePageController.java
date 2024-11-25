package vn.DoThanhTai.laptopshop.controller.client;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vn.DoThanhTai.laptopshop.domain.Product;
import vn.DoThanhTai.laptopshop.domain.Role;
import vn.DoThanhTai.laptopshop.domain.User;
import vn.DoThanhTai.laptopshop.domain.dto.RegisterDTO;
import vn.DoThanhTai.laptopshop.service.ProductService;
import vn.DoThanhTai.laptopshop.service.UserService;
import vn.DoThanhTai.laptopshop.service.OrderService;
import vn.DoThanhTai.laptopshop.domain.Order;


@Controller
public class HomePageController {

    private final ProductService productService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OrderService orderService;

    public HomePageController(ProductService productService, UserService userService, PasswordEncoder passwordEncoder, OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        // List<Product> products = productService.getAllProduct();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> products = this.productService.getAllProduct(pageable);
        List<Product> productList = products.getContent();
        model.addAttribute("products", productList);
       // HttpSession session = request.getSession(false);
        return "client/homepage/show";
    }

    //////////////////////////////// Register ////////////////////////////////
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerUser") @Valid RegisterDTO registerUser, BindingResult bindingResult) {

        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println( ">>>>>>>"+error.getDefaultMessage());
        }
        if(bindingResult.hasErrors()){
            return "client/auth/register";
        }

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
    //////////////////////////////// Deny ////////////////////////////////
    @GetMapping("/access-deny")
    public String getDenyPage() {
        return "client/auth/deny";
    }

    //////////////////////////////// Order history ////////////////////////////////
    @GetMapping("/order-history")
    public String getOrderHistoryPage(Model model, HttpServletRequest request) {
        // tao user
        User currentUser  = new User();
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        // set id cho user
        currentUser .setId(id);

        // lay order cua user
        List<Order> orders = this.orderService.fetchOrderByUser(currentUser);
        model.addAttribute("orders", orders);


        return "client/cart/order-history";
    }
}
