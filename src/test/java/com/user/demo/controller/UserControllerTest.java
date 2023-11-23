package com.user.demo.controller;

import com.user.demo.dto.UserDTO;
import com.user.demo.dto.UserResponseDTO;
import com.user.demo.exceptions.CustomException;
import com.user.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static final String USER_JSON = """
        {
            "name": "Juan Rodriguez",
            "email": "aaaaaaa@dominio.cl",
            "password": "Prueba123$",
            "phones": [
                {
                    "number": "1234567",
                    "citycode": "1",
                    "countrycode": "57"
                }
            ]
        }
        """;

    @Test
    public void createUser_Successful() throws Exception {
        UserResponseDTO userResponseDTO = new UserResponseDTO(
                UUID.randomUUID(),
                "Juan Rodriguez",
                "aaaaaaa@dominio.cl",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "token",
                true
        );
        given(userService.registerUser(any(UserDTO.class))).willReturn(userResponseDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Juan Rodriguez"))
                .andExpect(jsonPath("$.email").value("aaaaaaa@dominio.cl"))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.modified").exists())
                .andExpect(jsonPath("$.lastLogin").exists())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.isActive").value(true));

        verify(userService).registerUser(any(UserDTO.class));
    }

    @Test
    public void createUser_EmailAlreadyExists() throws Exception {
        given(userService.registerUser(any(UserDTO.class)))
                .willThrow(new CustomException("El correo ya registrado", HttpStatus.CONFLICT));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("El correo ya registrado"));

        verify(userService).registerUser(any(UserDTO.class));
    }

}

