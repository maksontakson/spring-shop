package com.example.springshop.controllers;

import com.example.springshop.domain.User;
import com.example.springshop.dto.UserDTO;
import com.example.springshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.getAll());
        return "userList";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user";
    }

    @PostMapping("/new")
    public String saveUser(UserDTO userDTO, Model model) {
        if(userService.save(userDTO)) {
            return "redirect:/users";
        } else {
            model.addAttribute("user", userDTO);
            return "user";
        }
    }

    @GetMapping("/profile")
    public String profileUser(Model model, Principal principal) {
        if(principal == null) {
            throw new RuntimeException("You are not authorized");
        }
        User user = userService.findByName(principal.getName());

        UserDTO dto = UserDTO.builder()
                    .username(user.getName())
                    .email(user.getEmail())
                    .build();
        model.addAttribute("user", dto);
        return "profile";
    }

    @PostMapping("/profile")
    public String profileUser(UserDTO userDTO, Model model, Principal principal) {
        if(principal == null){
            throw new RuntimeException("You are not authorized");
        }
        if(!userDTO.getUsername().equals(principal.getName())) {
            throw new RuntimeException("You can't change your username");
        }
        if(userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()
                && !userDTO.getPassword().equals(userDTO.getMatchingPassword())){
            model.addAttribute("user", userDTO);
            return "profile";
        }
        userService.updateUserDTO(userDTO);
        return "redirect:/users/profile";
    }
}
