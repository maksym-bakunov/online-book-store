package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.user.UserDto;
import com.example.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.example.onlinebookstore.exception.RegistrationException;
import com.example.onlinebookstore.model.User;
import java.util.List;

public interface UserService {
    UserDto register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException;

    List<User> getAllUsers();

    void saveAll(List<User> userList);
}
