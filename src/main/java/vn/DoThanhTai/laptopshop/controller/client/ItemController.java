package vn.DoThanhTai.laptopshop.controller.client;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.DoThanhTai.laptopshop.domain.Cart;
import vn.DoThanhTai.laptopshop.domain.CartDetail;
import vn.DoThanhTai.laptopshop.domain.Product;
import vn.DoThanhTai.laptopshop.domain.User;
import vn.DoThanhTai.laptopshop.service.ProductService;

@Controller
public class ItemController {

    private final ProductService productService;

    public ItemController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/detail/{id}")
    public String getProductDetailPage(Model model, @PathVariable Long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("product", product);
        return "client/product/detail";
    }

    //////////////////////////////// add to cart ////////////////////////////////
    @PostMapping("/add-product-to-cart/{id}")
    public String addProductToCart(@PathVariable long id, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");

        // lay san pham
        long productId = id;
        this.productService.handleAddProductToCart(email, productId, session);
        return "redirect:/";
    }

    //////////////////////////////// xem gio hang ////////////////////////////////
    @GetMapping("/cart")
    public String getCartPage(Model model, HttpServletRequest request) {

        // lay id cua nguoi dung tu session
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");

        // tao moi user va gan id vao user
        User user = new User();
        user.setId(id);

        // tim cart cua user
        Cart cart = this.productService.fetchByUser(user);

        //lay cart_detail cua cart
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();

        double total = 0;
        for (CartDetail cartDetail : cartDetails) {
            total += cartDetail.getPrice() * cartDetail.getQuantity();
        }

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", total);

        return "client/cart/show";
    }
}