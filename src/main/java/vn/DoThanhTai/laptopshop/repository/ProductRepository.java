package vn.DoThanhTai.laptopshop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.DoThanhTai.laptopshop.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    
    Product save(Product product);

    List<Product> findAll();

    Product findById(long id);

    void deleteById(long id);

    Page<Product> findAll(Specification<Product> spec,Pageable page);

    Page<Product> findAll(Pageable page);

    
}

