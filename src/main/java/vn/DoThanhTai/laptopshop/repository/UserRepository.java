package vn.DoThanhTai.laptopshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.DoThanhTai.laptopshop.domain.Product;
import vn.DoThanhTai.laptopshop.domain.User;

@Repository
public interface UserRepository extends  JpaRepository<User, Long> {

   User save(User user);
   
   Page<User> findAll(Pageable page);
   
   Optional<User> findById(Long id);
   
   void deleteById(Long id);

   boolean existsByEmail(String email);

   User findByEmail(String email);
}
    
