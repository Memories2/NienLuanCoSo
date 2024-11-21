package vn.DoThanhTai.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {
    @GetMapping("/admin/product")
    public String showProduct() {
        return "admin/product/show";
    }
    
}
