package vn.DoThanhTai.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.DoThanhTai.laptopshop.domain.Cart;
import vn.DoThanhTai.laptopshop.domain.CartDetail;
import vn.DoThanhTai.laptopshop.domain.Product;
import vn.DoThanhTai.laptopshop.domain.User;
import vn.DoThanhTai.laptopshop.repository.CartDetailRepository;
import vn.DoThanhTai.laptopshop.repository.CartRepository;
import vn.DoThanhTai.laptopshop.repository.OrderDetailRepository;
import vn.DoThanhTai.laptopshop.repository.OrderRepository;
import vn.DoThanhTai.laptopshop.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;
    private final OrderRepository oderRepository;
    private final OrderDetailRepository oderDetailRepository;
    

    ProductService(ProductRepository productRepository, CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, UserService userService, OrderRepository oderRepository,
            OrderDetailRepository oderDetailRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
        this.oderRepository = oderRepository;
        this.oderDetailRepository = oderDetailRepository;
    }

    public Product handleSaveProduct(Product product) {
        Product product1 = this.productRepository.save(product);
        return product1;
    }

    public List<Product> getAllProduct() {
        return this.productRepository.findAll();
    }

    public Product getProductById(long id) {
        return this.productRepository.findById(id);
    }

    public void handleDeleteProduct(long id) {
        this.productRepository.deleteById(id);
    }

    //////////////////////////////// cart logic ////////////////////////////////
    public Cart fetchByUser(User user) {
        return this.cartRepository.findByUser(user);

    }
    public void handleAddProductToCart(String email, long id, HttpSession session) {
        User user = this.userService.getUserByEmail(email);

        if (user != null) {
            // check user co cart chua ? neu chua > tao moi
            Cart cart = this.cartRepository.findByUser(user);
            if (cart == null) {
                // tao moi cart
                Cart otherCart = new Cart();
                otherCart.setUser(user);
                otherCart.setSum(0);
                // save
                cart = this.cartRepository.save(otherCart);
            }

            // luu cart_detail
            // tim product by id
            Product realProduct = this.productRepository.findById(id);

            // check sản phẩm đã từng được thêm vào giỏ hàng trước đây chưa
            CartDetail oldDetail = this.cartDetailRepository.findByCartAndProduct(cart, realProduct);

            // neu chua co thi tao moi
            if (oldDetail == null) {
                CartDetail cd = new CartDetail();
                cd.setCart(cart); // set cart cho cart_detail vi cart_detail phu thuoc vao cart
                cd.setProduct(realProduct); 
                cd.setPrice(realProduct.getPrice());
                cd.setQuantity(1);
                this.cartDetailRepository.save(cd);
                // update cart sum
                int s = cart.getSum() + 1;
                cart.setSum(s);
                this.cartRepository.save(cart);
                session.setAttribute("sum", s);

            } else {
                oldDetail.setQuantity(oldDetail.getQuantity() + 1);
                // update sum cart
                this.cartDetailRepository.save(oldDetail);
            }

        }
    }
}
