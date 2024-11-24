package vn.DoThanhTai.laptopshop.controller.client;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("cart", cart);

        return "client/cart/show";
    }

    //////////////////////////////// xoa san pham trong gio hang ////////////////////////////////
    @PostMapping("/delete-cart-product/{id}")
    public String deleteCartDetail(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        long  cartDetailId = id;
        this.productService.handleRemoveCartDetail(cartDetailId, session);
        return "redirect:/cart";
    }


    //////////////////////////////// xac nhan thanh toan ////////////////////////////////
    @PostMapping("/confirm-checkout")
    public String getCheckOutPage(@ModelAttribute("cart") Cart cart) {
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
        this.productService.handleUpdateCartBeforeCheckout(cartDetails);
        return "redirect:/checkout";
    }

    //////////////////////////////// check out ////////////////////////////////
    @GetMapping("/checkout")
    public String getCheckOutPage(Model model, HttpServletRequest request) {
        // tao moi user tu session
        User currentUser = new User();// null
        HttpSession session = request.getSession(false);

        // lay id cua user tu session
        long id = (long) session.getAttribute("id");
        currentUser.setId(id); // gan id vao user

        // lay cart tu user 
        Cart cart = this.productService.fetchByUser(currentUser);

        // lay cart_detail tu cart
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();

        // tinh tong tien cua cart
        double totalPrice = 0;
        for (CartDetail cd : cartDetails) {
            totalPrice += cd.getPrice() * cd.getQuantity();
        }

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);

        return "client/cart/checkout";
    }

    //////////////////////////////// place order ////////////////////////////////
    // @PostMapping("/place-order")
    // public String handlePlaceOrder(
    //         HttpServletRequest request,
    //         @RequestParam("receiverName") String receiverName,
    //         @RequestParam("receiverAddress") String receiverAddress,
    //         @RequestParam("receiverPhone") String receiverPhone) {

    //     User currentuser = new User(); // null
    //     HttpSession session = request.getSession(false);
    //     long id = (long) session.getAttribute("id");
    //     currentuser.setId(id);

    //     this.productService.handlePlaceOrder(currentuser, session, receiverName, receiverAddress, receiverPhone);

    //     return "redirect:/thanks";
    // }
}