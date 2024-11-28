package vn.DoThanhTai.laptopshop.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import vn.DoThanhTai.laptopshop.domain.User;
import vn.DoThanhTai.laptopshop.service.UploadService;
import vn.DoThanhTai.laptopshop.service.UserService;

@Controller
public class ProfileController {
    
    private UserService userService;
    private UploadService uploadService;
    

    public ProfileController (UserService userService, UploadService uploadService){
        this.userService = userService;
        this.uploadService = uploadService;
    }

    ///////////////////////// Profile View //////////////////////////
    @GetMapping("profile/{id}")
    public String getProfilePage(Model model, @PathVariable("id") Long id) {
        User currentUser = this.userService.handleGetUserById(id);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("id", id);
        return "client/profile/profile";
    }

    @PostMapping("/profile/update")
    public String postUpdateProfile(Model model, @ModelAttribute("currentUser") User currentUser, @RequestParam("hoidanitFile") MultipartFile file, HttpSession session) {
        User oldUser = this.userService.handleGetUserById(currentUser.getId());
        if (oldUser != null) {
            //upload file
            if(file != null && !file.isEmpty()) {
                //delete old image
                this.uploadService.handleDeleteImage("avatar", oldUser.getAvatar());
                //upload new image
                String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
                oldUser.setAvatar(avatar);
            }
            //update session
            session.setAttribute("avatar", oldUser.getAvatar());
            //update user
            oldUser.setFullName(currentUser.getFullName());
            oldUser.setPhone(currentUser.getPhone());
            oldUser.setAddress(currentUser.getAddress());
            this.userService.handleUpdateUser(oldUser);
        }
        return "redirect:/profile/" + currentUser.getId();
    }
    
}
