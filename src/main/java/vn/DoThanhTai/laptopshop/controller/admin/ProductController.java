package vn.DoThanhTai.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.DoThanhTai.laptopshop.domain.Product;
import vn.DoThanhTai.laptopshop.service.ProductService;
import vn.DoThanhTai.laptopshop.service.UploadService;

@Controller
public class ProductController {

    private final UploadService uploadService;
    private final ProductService productService;

    ProductController(UploadService uploadService, ProductService productService) {
        this.uploadService = uploadService;
        this.productService = productService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model) {
        List<Product> products = this.productService.getAllProduct();
        model.addAttribute("products", products);
        return "admin/product/show";
    }

    //////////////////////////////// CREATE PRODUCT ////////////////////////////////

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String postCreateProduct(Model model, @ModelAttribute("newProduct") @Valid Product newProduct, BindingResult newProductBindingResult, @RequestParam("hoidanitFile") MultipartFile file) {

        //validate
        if(newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }

        //upload file
        String nameProduct = this.uploadService.handleSaveUploadFile(file, "product");
        newProduct.setImage(nameProduct);

        //save product
        this.productService.handleSaveProduct(newProduct);

        return "redirect:/admin/product";
    }
}
