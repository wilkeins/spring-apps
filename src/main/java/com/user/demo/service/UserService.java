package com.user.demo.service;

import com.user.demo.config.AppConfig;
import com.user.demo.dto.UserDTO;
import com.user.demo.dto.UserResponseDTO;
import com.user.demo.helper.PhoneConverter;
import com.user.demo.helper.UserServiceHelper;
import com.user.demo.model.Users;
import com.user.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserServiceHelper {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AppConfig appConfig;
    private final PhoneConverter phoneConverter;

    @Autowired
    public UserService(UserRepository userRepository, TokenService tokenService,
                       AppConfig appConfig, PhoneConverter phoneConverter) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.appConfig = appConfig;
        this.phoneConverter = phoneConverter;
    }

    public UserResponseDTO registerUser(UserDTO userDto){
        UserServiceHelper.validateUserDto(userDto, userRepository, appConfig);
        Users users = UserServiceHelper.createUserEntity(userDto, phoneConverter);
        String token = tokenService.generateToken(users);
        users.setToken(token);
        users = userRepository.save(users);
        return convertToUserResponseDTO(users);
    }
    public UserResponseDTO getUserById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        return convertToUserResponseDTO(user);
    }

    public List<UserResponseDTO> searchUsers(String name, String email, Boolean isActive) {
        List<Users> users = userRepository.findByCriteria(name, email, isActive);
        return users.stream()
                .map(this::convertToUserResponseDTO)
                .collect(Collectors.toList());
    }

}