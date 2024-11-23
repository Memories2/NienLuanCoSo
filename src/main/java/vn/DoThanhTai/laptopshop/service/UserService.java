package vn.DoThanhTai.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.DoThanhTai.laptopshop.domain.Role;
import vn.DoThanhTai.laptopshop.domain.User;
import vn.DoThanhTai.laptopshop.domain.dto.RegisterDTO;
import vn.DoThanhTai.laptopshop.repository.UserRepository;
import vn.DoThanhTai.laptopshop.repository.RoleRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public String getUserName() {
        return "hello";
    }

    public User handleSaveUser(User user) {
        return this.userRepository.save(user);

    }

    public List<User> handleGetAllUsers() {
        return this.userRepository.findAll();
    }

    public User handleGetUserById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public User handleUpdateUser(User user) {
        return this.userRepository.save(user);
    }
  
    public void handleDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public Role getRoleByName(String name) {
        return this.roleRepository.findByName(name);
    }

    //////////////////////////////// Register ////////////////////////////////
    public User registerDTOtoUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setFullName(registerDTO.getFirstName() + " " + registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        user.setPhone(registerDTO.getPhone());
        user.setAddress(registerDTO.getAddress());
        return user;
    }


    //////////////////////////////// Check Email is exist////////////////////////////////
    public boolean checkEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    //////////////////////////////// Get User by Email ////////////////////////////////
    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

 
}

