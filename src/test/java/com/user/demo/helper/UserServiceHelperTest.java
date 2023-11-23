package com.user.demo.helper;

import com.user.demo.config.AppConfig;
import com.user.demo.dto.PhoneDTO;
import com.user.demo.dto.UserDTO;
import com.user.demo.exceptions.CustomException;
import com.user.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceHelperTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AppConfig appConfig;

    @Test
    public void validateUserDto_EmailAlreadyExists_ThrowsCustomException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        UserDTO userDto = new UserDTO("Test Name", "existing@test.com", "Password123", new HashSet<>());

        CustomException exception = assertThrows(CustomException.class, () ->
                UserServiceHelper.validateUserDto(userDto, userRepository, appConfig)
        );

        assertEquals("El correo ya registrado", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
    }

    @Test
    public void validateUserDto_InvalidName_ThrowsIllegalArgumentException() {
        UserDTO userDto = new UserDTO("", "test@test.com", "Password123", new HashSet<>());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                UserServiceHelper.validateUserDto(userDto, userRepository, appConfig)
        );

        assertEquals("El nombre no puede estar vacío", exception.getMessage());
    }

    @Test
    public void validateUserDto_InvalidEmail_ThrowsIllegalArgumentException() {
        UserDTO userDto = new UserDTO("Test Name", "", "Password123", new HashSet<>());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                UserServiceHelper.validateUserDto(userDto, userRepository, appConfig)
        );

        assertEquals("El email no puede estar vacío", exception.getMessage());
    }

    @Test
    public void validateUserDto_InvalidPhoneNumber_ThrowsIllegalArgumentException() {
        Set<PhoneDTO> phones = Set.of(new PhoneDTO("", "1", "57"));
        UserDTO userDto = new UserDTO("Test Name", "test@test.com", "Password123", phones);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                UserServiceHelper.validateUserDto(userDto, userRepository, appConfig)
        );

        assertEquals("El número de teléfono no puede estar vacío", exception.getMessage());
    }


}

