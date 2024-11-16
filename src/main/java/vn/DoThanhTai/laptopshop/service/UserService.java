package vn.DoThanhTai.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.DoThanhTai.laptopshop.domain.User;
import vn.DoThanhTai.laptopshop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
  
    
}
