package com.user.demo.service;

import com.user.demo.config.AppConfig;
import com.user.demo.dto.UserDTO;
import com.user.demo.dto.UserResponseDTO;
import com.user.demo.helper.PhoneConverter;
import com.user.demo.helper.UserServiceHelper;
import com.user.demo.model.Users;
import com.user.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

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
}