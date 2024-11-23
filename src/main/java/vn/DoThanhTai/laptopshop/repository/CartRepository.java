package vn.DoThanhTai.laptopshop.repository;  

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.DoThanhTai.laptopshop.domain.Cart;
import vn.DoThanhTai.laptopshop.domain.User;



@Repository
public interface CartRepository extends JpaRepository <Cart, Long> {
    Cart findByUser(User user);
} 