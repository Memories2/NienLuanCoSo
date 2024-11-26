package vn.DoThanhTai.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.DoThanhTai.laptopshop.domain.Cart;
import vn.DoThanhTai.laptopshop.domain.CartDetail;
import vn.DoThanhTai.laptopshop.domain.Order;
import vn.DoThanhTai.laptopshop.domain.OrderDetail;
import vn.DoThanhTai.laptopshop.domain.Product;
import vn.DoThanhTai.laptopshop.domain.User;
import vn.DoThanhTai.laptopshop.repository.CartDetailRepository;
import vn.DoThanhTai.laptopshop.domain.dto.ProductCriteriaDTO;
import vn.DoThanhTai.laptopshop.repository.CartRepository;
import vn.DoThanhTai.laptopshop.repository.OrderDetailRepository;
import vn.DoThanhTai.laptopshop.repository.OrderRepository;
import vn.DoThanhTai.laptopshop.repository.ProductRepository;
import vn.DoThanhTai.laptopshop.service.specification.ProductSpecs;
import org.springframework.data.jpa.domain.Specification;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    ProductService(ProductRepository productRepository, CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, UserService userService, OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public Product handleSaveProduct(Product product) {
        Product product1 = this.productRepository.save(product);
        return product1;
    }

    public Page<Product> getAllProduct(Pageable page) {
        return this.productRepository.findAll(page);
    }

    public Page<Product> getAllProductWithSpec(Pageable page, ProductCriteriaDTO productCriteriaDTO) {
        if (productCriteriaDTO.getTarget() == null
                && productCriteriaDTO.getFactory() == null
                && productCriteriaDTO.getPrice() == null) {
            return this.productRepository.findAll(page);
        }
        Specification<Product> combinedSpec = Specification.where(null);
        if (productCriteriaDTO.getTarget() != null && productCriteriaDTO.getTarget().isPresent()) {
            Specification<Product> currentSpecs = ProductSpecs.matchListTarget(productCriteriaDTO.getTarget().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }
        if (productCriteriaDTO.getFactory() != null && productCriteriaDTO.getFactory().isPresent()) {
            Specification<Product> currentSpecs = ProductSpecs.matchListFactory(productCriteriaDTO.getFactory().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }

        if (productCriteriaDTO.getPrice() != null && productCriteriaDTO.getPrice().isPresent()) {
            Specification<Product> currentSpecs = this.buildPriceSpecification(productCriteriaDTO.getPrice().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }
        return this.productRepository.findAll(combinedSpec,page);
    }

    // case 6
    public Specification<Product> buildPriceSpecification(List<String> price) {
        Specification<Product> combinedSpec = Specification.where(null); // disconjunction
        for (String p : price) {
            double min = 0;
            double max = 0;

            // Set the appropriate min and max based on the price range string
            switch (p) {
                case "duoi-10-trieu":
                    min = 0;
                    max = 10000000;
                    break;
                case "10-15-trieu":
                    min = 10000000;
                    max = 15000000;
                    break;
                case "15-20-trieu":
                    min = 15000000;
                    max = 20000000;
                    break;
                case "tren-20-trieu":
                    min = 20000000;
                    max = 200000000;
                    break;
            }

            if (min != 0 && max != 0) {
                Specification<Product> rangeSpec = ProductSpecs.matchMultiplePrice(min, max);
                combinedSpec = combinedSpec.or(rangeSpec);
            }
        }

        return combinedSpec;
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

    public void handleAddProductToCart(String email, long id, HttpSession session, long quantity) {
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
                cd.setQuantity(quantity);
                this.cartDetailRepository.save(cd);
                // update cart sum
                int s = cart.getSum() + 1;
                cart.setSum(s);
                this.cartRepository.save(cart);
                session.setAttribute("sum", s);

            } else {
                oldDetail.setQuantity(oldDetail.getQuantity() + quantity);
                // update sum cart
                this.cartDetailRepository.save(oldDetail);
            }

        }
    }

    public void handleRemoveCartDetail(long cartDetailId, HttpSession session) {

        // tim cart_detail theo id
        Optional<CartDetail> cartDetailOptional = this.cartDetailRepository.findById(cartDetailId);
        // check cart_detail co ton tai khong
        if (cartDetailOptional.isPresent()) {
            // lay cart_detail
            CartDetail cartDetail = cartDetailOptional.get();
            // lay cart cua cart_detail
            Cart currentCart = cartDetail.getCart();

            // delete CardDetail
            this.cartDetailRepository.deleteById(cartDetailId);

            // update Cart
            if (currentCart.getSum() > 1) {
                int s = currentCart.getSum() - 1;
                currentCart.setSum(s);

                // update cho session
                session.setAttribute("sum", s);

                // save cart
                this.cartRepository.save(currentCart);
            } else {
                // dele cart sum = 1
                this.cartRepository.deleteById(currentCart.getId());
                session.setAttribute("sum", 0);
            }
        }
    }

    public void handleUpdateCartBeforeCheckout(List<CartDetail> cartDetails) {
        // lặp qua từng cartDetail
        for (CartDetail cartDetail : cartDetails) {
            // tim cartDetail theo id tu database
            Optional<CartDetail> cdOptional = this.cartDetailRepository.findById(cartDetail.getId());
            // check cartDetail co ton tai trong optional khong
            if (cdOptional.isPresent()) {
                // lay cartDetail tu optional
                CartDetail currentCartDetail = cdOptional.get();
                // update quantity
                currentCartDetail.setQuantity(cartDetail.getQuantity());
                // save
                this.cartDetailRepository.save(currentCartDetail);
            }
        }
    }

    //////////////////////////////// oder logic ////////////////////////////////
    public void handlePlaceOrder(User user, HttpSession session, String receiverName, String receiverAddress,String receiverPhone) {

        Order order = new Order();

        // Step 1: get cart by user
        Cart cart = this.cartRepository.findByUser(user);
        if (cart != null) {
            List<CartDetail> cartDetails = cart.getCartDetails();
            if (cartDetails != null) {

                // create oder
                order.setUser(user);
                order.setReceiverName(receiverName);
                order.setReceiverAddress(receiverAddress);
                order.setReceiverPhone(receiverPhone);
                order.setStatus("PENDING");

                double sum = 0;
                for (CartDetail cd : cartDetails) {
                    sum += cd.getPrice() * cd.getQuantity();
                }
                order.setTotalPrice(sum);
                order = this.orderRepository.save(order);

                // create oderDetail
                for (CartDetail cd : cartDetails) {

                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(cd.getProduct());
                    orderDetail.setPrice(cd.getPrice());
                    orderDetail.setQuantity(cd.getQuantity());
                    this.orderDetailRepository.save(orderDetail);
                }

                // Step 2: delete cart_detail and cart
                for (CartDetail cd : cartDetails) {
                    this.cartDetailRepository.deleteById(cd.getId());
                }

                this.cartRepository.deleteById(cart.getId());

            }

            // Step 3: update session
            session.setAttribute("sum", 0);
        }
    }

}