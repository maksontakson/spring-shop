package com.example.springshop.service;

import com.example.springshop.domain.User;
import com.example.springshop.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean save(UserDTO userDTO);
    void save(User user);
    List<UserDTO> getAll();

    User findByName(String name);

    void updateUserDTO(UserDTO userDTO);
}
