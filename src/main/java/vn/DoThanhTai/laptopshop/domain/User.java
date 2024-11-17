package vn.DoThanhTai.laptopshop.domain;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity // Biến class này thành một entity (một bảng trong database)
@Table(name = "users") // Đặt tên bảng trong database
public class User {

    @Id // Đánh dấu thuộc tính id là primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng giá trị của id
    private long id;
    private String email;
    private String password;
    private String fullName;
    private String address;
    private String phone;

    private String avatar;

    // kết nối quan hệ một chiều nhiều người dùng có thể có một role giống nhau
    @ManyToOne
    @JoinColumn(name = "role_id") // thêm cột role_id vào bảng users
    private Role role;

    @OneToMany(mappedBy = "user")  // Sử dụng mối quan hệ để lấy data chứ không phải tạo cột mới
    private List<Order> orders;

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", password=" + password + ", fullName=" + fullName
                + ", address=" + address + ", phone=" + phone + "]";
    }

}
