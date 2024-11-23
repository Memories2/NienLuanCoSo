package vn.DoThanhTai.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.DoThanhTai.laptopshop.domain.Cart;
import vn.DoThanhTai.laptopshop.domain.CartDetail;
import vn.DoThanhTai.laptopshop.domain.Product;

public interface CartDetailRepository extends JpaRepository<CartDetail,Long> {

    boolean existsByCartAndProduct(Cart cart, Product product);
    CartDetail findByCartAndProduct(Cart cart, Product product);
    
} 