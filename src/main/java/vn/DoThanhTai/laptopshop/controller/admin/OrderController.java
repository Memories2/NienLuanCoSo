package vn.DoThanhTai.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    
    @GetMapping("/admin/order")
    public String showOrder() {
        return "admin/order/show";
    }
}
