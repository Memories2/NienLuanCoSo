package vn.DoThanhTai.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.DoThanhTai.laptopshop.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    
    
}