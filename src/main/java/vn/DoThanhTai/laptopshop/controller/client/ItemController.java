package vn.DoThanhTai.laptopshop.controller.client;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import vn.DoThanhTai.laptopshop.domain.Order;
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
        this.productService.handleAddProductToCart(email, productId, session,1);

        String referer = request.getHeader("Referer");

        if(referer != null && referer.contains("/products")){
            return "redirect:/products";
        }
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
        model.addAttribute("order", new Order());

        return "client/cart/checkout";
    }

    ////////////////////////////// place order ////////////////////////////////
    @PostMapping("/place-order")
    public String handlePlaceOrder(HttpServletRequest request, @ModelAttribute("order") Order order) {

        // tao moi user tu session
        User currentUser = new User(); // null

        // lay session tu request
        HttpSession session = request.getSession(false);
        
        // lay id cua user tu session
        long id = (long) session.getAttribute("id");
        currentUser.setId(id); // gan id vao user

        // handle place order
        this.productService.handlePlaceOrder(currentUser, session, order.getReceiverName(), order.getReceiverAddress(), order.getReceiverPhone());

        return "redirect:/thanks";
    }

    ////////////////////////////// thanks ////////////////////////////////
    @GetMapping("/thanks")
    public String getThankYouPage(Model model) {
        return "client/cart/thanks";
    }

    ////////////////////////////// add product from view detail ////////////////////////////////
    @PostMapping("/add-product-from-view-detail")
    public String handleAddProductFromViewDetail(@RequestParam("id") long id, @RequestParam("quantity") long quantity, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        String email = (String) session.getAttribute("email");
        this.productService.handleAddProductToCart(email, id, session, quantity);
        return "redirect:/product/detail/" + id;
    }

    //////////////////////////////// show all product khi nguoi dng click vao tat ca san pham ////////////////////////////////
    @GetMapping("/products")
    public String getProductPage(Model model, @RequestParam("page") Optional<String> pageOptional,
     @RequestParam("name") Optional<String> nameOptional,
     @RequestParam("min-price") Optional<String> minOptional,
     @RequestParam("max-price") Optional<String> maxOptional,
     @RequestParam("factory") Optional<String> factoryOptional,
     @RequestParam("target") Optional<String> targetOptional,  
     @RequestParam("price") Optional<String> priceOptional,
     @RequestParam("sort") Optional<String> sortOptional

        
     ) {
        int page = 0;
        try {
            if (pageOptional.isPresent()) {
                // convert from String to int
                page = Integer.parseInt(pageOptional.get());
            }else{
                page = 1;
            }
        }catch(Exception e){
            page = 1;
        }


        Pageable pageable = PageRequest.of(page - 1, 60);
        //Page<Product> products = this.productService.getAllProductWithSpec(pageable,name);

        String name = nameOptional.isPresent() ? nameOptional.get() : "";
        Page<Product> products = this.productService.getAllProductWithSpec(pageable,name);
            
        // case 1
        // double min = minOptional.isPresent() ? Double.parseDouble(minOptional.get())
        // : 0;
        // Page<Product> products = this.productService.getAllProductWithSpec(pageable, min);
        // case 2
        // double max = maxOptional.isPresent() ? Double.parseDouble(maxOptional.get())
        // : 0;
        // Page<Product> products = this.productService.getAllProductWithSpec(pageable, max);
        // case 3
        // String factory = factoryOptional.isPresent() ? factoryOptional.get() : "";
        // Page<Product> products = this.productService.getAllProductWithSpec(pageable,
        // factory);
        // case 4
        // List<String> factory = Arrays.asList(factoryOptional.get().split(","));
        // Page<Product> products = this.productService.getAllProductWithSpec(pageable,
        // factory);
        // case 5
        // String price = priceOptional.isPresent() ? priceOptional.get() : "";
        // Page<Product> products = this.productService.getAllProductWithSpec(pageable,
        // price);
        // case 6
        // List<String> price = Arrays.asList(priceOptional.get().split(","));
        // Page<Product> prs = this.productService.fetchProductsWithSpec(pageable,
        // price);

        List<Product> productList = products.getContent();
 

        model.addAttribute("products", productList);



        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());

        return "client/product/show";
    }
}