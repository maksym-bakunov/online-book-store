package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.UserDto;
import com.example.onlinebookstore.dto.UserRegistrationRequestDto;
import com.example.onlinebookstore.exception.RegistrationException;

public interface UserService {
    UserDto register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException;
}
