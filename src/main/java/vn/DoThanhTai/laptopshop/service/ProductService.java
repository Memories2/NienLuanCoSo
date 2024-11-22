package vn.DoThanhTai.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.DoThanhTai.laptopshop.domain.Product;
import vn.DoThanhTai.laptopshop.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
}
