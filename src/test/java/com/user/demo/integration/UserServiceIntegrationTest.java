package com.user.demo.integration;

import com.user.demo.config.AppConfig;
import com.user.demo.dto.PhoneDTO;
import com.user.demo.dto.UserDTO;
import com.user.demo.dto.UserResponseDTO;
import com.user.demo.exceptions.CustomException;
import com.user.demo.model.Users;
import com.user.demo.repository.UserRepository;
import com.user.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppConfig appConfig;

    @Test
    public void registerUser_Successful() {
        UserDTO userDto = new UserDTO("Test Name", "test@dominio.cl", "Password123$", new HashSet<>());
        UserResponseDTO result = userService.registerUser(userDto);
        assertNotNull(result);
        assertTrue(userRepository.existsByEmail("test@dominio.cl"));
    }

    @Test
    public void registerUser_EmailAlreadyExists() {
        Users existingUser = new Users();
        existingUser.setUuid(UUID.randomUUID());
        existingUser.setName("Existing User");
        existingUser.setEmail("exists@dominio.cl");
        existingUser.setPassword("SomeSecurePassword123");
        existingUser.setActive(true);
        userRepository.save(existingUser);
        UserDTO newUserDTO = new UserDTO("New User", "exists@dominio.cl", "AnotherSecurePassword123", new HashSet<>());
        assertThrows(CustomException.class, () -> userService.registerUser(newUserDTO));
        assertTrue(userRepository.existsByEmail("exists@dominio.cl"));
    }

    @Test
    public void registerUser_InvalidPassword_ThrowsCustomException() {
        String invalidPasswordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        ReflectionTestUtils.setField(appConfig, "passwordPattern", invalidPasswordPattern);

        UserDTO userDTO = new UserDTO("Test Name", "test@test.com", "weak", new HashSet<>());

        assertThrows(CustomException.class, () -> userService.registerUser(userDTO));
        assertFalse(userRepository.existsByEmail("test@test.com"));
    }

    @Test
    public void registerUser_InvalidPhoneData_ThrowsIllegalArgumentException() {
        Set<PhoneDTO> phones = Set.of(new PhoneDTO("", "1", "57"));
        UserDTO userDTO = new UserDTO("Test Name", "test@test.com", "ValidPass123", phones);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(userDTO));
        assertFalse(userRepository.existsByEmail("test@test.com"));
    }

}

