package com.example.onlinebookstore.service.impl;

import com.example.onlinebookstore.dto.user.UserDto;
import com.example.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.exception.RegistrationException;
import com.example.onlinebookstore.mapper.UserMapper;
import com.example.onlinebookstore.model.Role;
import com.example.onlinebookstore.model.User;
import com.example.onlinebookstore.repository.RoleRepository;
import com.example.onlinebookstore.repository.UserRepository;
import com.example.onlinebookstore.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String CAN_NOT_REGISTER_USER = "Can't register user";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(userRegistrationRequestDto.getEmail())) {
            throw new RegistrationException(CAN_NOT_REGISTER_USER);
        }

        User user = userMapper.toModel(userRegistrationRequestDto);
        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.getPassword()));
        Role role = roleRepository.findByName(Role.RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException("Can't find a role "
                        + Role.RoleName.USER));
        user.setRoles(Set.of(role));

        return userMapper.toDto(userRepository.save(user));
    }
}
