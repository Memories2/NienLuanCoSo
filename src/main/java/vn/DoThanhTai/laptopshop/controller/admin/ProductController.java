package vn.DoThanhTai.laptopshop.controller.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    //////////////////////////////// SHOW ALL PRODUCT  ////////////////////////////////

    @GetMapping("/admin/product")
    public String getProduct(Model model, @RequestParam("page") int page) {
        //
        Pageable pageable = PageRequest.of(page-1, 5);
        Page<Product> products = this.productService.getAllProduct(pageable);
        List<Product> productList = products.getContent();

        model.addAttribute("products", productList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        return "admin/product/show";
    }
    //////////////////////////////// VIEW DETAIL PRODUCT ////////////////////////////////
    @GetMapping("/admin/product/detail/{id}")
    public String getDetailProduct(Model model, @PathVariable("id") long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("id",id);
        return "admin/product/detail";
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

    //////////////////////////////// UPDATE PRODUCT ////////////////////////////////
    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable("id") long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("currentProduct", product);
        return "admin/product/update";
    }
    
    @PostMapping("/admin/product/update")
    public String postUpdateProduct(Model model, @ModelAttribute("currentProduct") @Valid Product product, BindingResult productBindingResult, @RequestParam("hoidanitFile") MultipartFile file) {
        if(productBindingResult.hasErrors()) {
            return "admin/product/update";
        }
        Product productUpdate = this.productService.getProductById(product.getId());
        if(productUpdate != null) {
            //upload file
            if(file != null && !file.isEmpty()) {
                //delete old image
                this.uploadService.handleDeleteImage("product", productUpdate.getImage());

                //upload new image
                String img = this.uploadService.handleSaveUploadFile(file, "product");
                productUpdate.setImage(img);
            }

            //update product
            productUpdate.setName(product.getName());
            productUpdate.setPrice(product.getPrice());
            productUpdate.setQuantity(product.getQuantity());
            productUpdate.setDetailDesc(product.getDetailDesc());
            productUpdate.setShortDesc(product.getShortDesc());
            productUpdate.setFactory(product.getFactory());
            productUpdate.setTarget(product.getTarget());
            // save product
            this.productService.handleSaveProduct(productUpdate);
        }
        return "redirect:/admin/product";
    }

    //////////////////////////////// DELETE PRODUCT ////////////////////////////////
    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable("id") long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(Model model, @ModelAttribute("newProduct") Product product) { // Post xoa san pham
        this.productService.handleDeleteProduct(product.getId());
        return "redirect:/admin/product";
    }
}
