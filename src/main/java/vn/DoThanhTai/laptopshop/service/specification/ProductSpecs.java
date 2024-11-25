package vn.DoThanhTai.laptopshop.service.specification;

import org.springframework.data.jpa.domain.Specification;

import vn.DoThanhTai.laptopshop.domain.Product;
import vn.DoThanhTai.laptopshop.domain.Product_;

public class ProductSpecs {
    public static  Specification<Product> nameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Product_.NAME), "%" + name + "%");
    }
}
