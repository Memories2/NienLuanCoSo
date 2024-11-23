package vn.DoThanhTai.laptopshop.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
 import vn.DoThanhTai.laptopshop.service.validator.RegisterChecked;
@RegisterChecked
public class RegisterDTO {

    @Size(min=3,message = "FirstName phải có tối thiểu 3 ký tự")
    private String firstName;


    private String lastName;

    @Email(message = "Email không hợp lệ", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;


    private String password;

    @Size(min=3,message = "ConfirmPassword phải có tối thiểu 3 ký tự")
    private String confirmPassword;

    @NotNull(message = "Số điện thoại không được để trống")
    @Size(min = 10, message = "Số điện thoại phải có tối thiểu 10 ký tự")
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    
}
