package vn.DoThanhTai.laptopshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.DoThanhTai.laptopshop.domain.User;

@Repository
public interface UserRepository extends  JpaRepository<User, Long> {

   User save(User user);

   List<User> findAll();
   
   Optional<User> findById(Long id);
   
   void deleteById(Long id);

   boolean existsByEmail(String email);

   User findByEmail(String email);
}
    
