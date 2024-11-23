package vn.DoThanhTai.laptopshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class) // tam thoi tat di phan dang nhap
public class LaptopshopApplication {
	public static void main(String[] args) {
		SpringApplication.run(LaptopshopApplication.class, args);
	}
}