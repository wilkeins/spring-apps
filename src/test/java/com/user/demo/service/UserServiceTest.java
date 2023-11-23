package com.user.demo.service;

import com.user.demo.config.AppConfig;
import com.user.demo.dto.PhoneDTO;
import com.user.demo.dto.UserDTO;
import com.user.demo.dto.UserResponseDTO;
import com.user.demo.exceptions.CustomException;
import com.user.demo.helper.PhoneConverter;
import com.user.demo.model.Phone;
import com.user.demo.model.Users;
import com.user.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenService tokenService;
    @Mock
    private AppConfig appConfig;
    @Mock
    private PhoneConverter phoneConverter;

    @InjectMocks
    private UserService userService;

    @Test
    public void registerUser_Successful() {
        UserDTO userDTO = new UserDTO("Test Name", "test@test.com", "Password123", new HashSet<>());
        Users user = new Users();
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(Users.class))).thenReturn(user);
        when(appConfig.getPasswordPattern()).thenReturn("Password123");
        when(tokenService.generateToken(any(Users.class))).thenReturn("dummyToken");
        UserResponseDTO result = userService.registerUser(userDTO);
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    public void registerUser_WithEmailAlreadyExists_ThrowsCustomException() {
        UserDTO userDTO = new UserDTO("Test Name", "exists@test.com", "Password123", new HashSet<>());
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(CustomException.class, () -> userService.registerUser(userDTO));

        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    public void registerUser_WithInvalidPassword_ThrowsCustomException() {
        UserDTO userDTO = new UserDTO("Test Name", "test@test.com", "weak", new HashSet<>());
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(appConfig.getPasswordPattern()).thenReturn("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");

        assertThrows(CustomException.class, () -> userService.registerUser(userDTO));

        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    public void registerUser_SuccessfulWithPhones() {
        Set<PhoneDTO> phones = new HashSet<>();
        phones.add(new PhoneDTO("1234567", "1", "57"));
        UserDTO userDTO = new UserDTO("Test Name", "test@test.com", "Password123", phones);

        Users user = new Users();
        Phone phone = new Phone();
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(Users.class))).thenReturn(user);
        when(appConfig.getPasswordPattern()).thenReturn("Password123");
        when(tokenService.generateToken(any(Users.class))).thenReturn("dummyToken");
        when(phoneConverter.convertToEntity(any(PhoneDTO.class))).thenReturn(phone);

        UserResponseDTO result = userService.registerUser(userDTO);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(Users.class));
        verify(phoneConverter, times(phones.size())).convertToEntity(any(PhoneDTO.class));
    }







}


