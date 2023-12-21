package com.user.demo.helper;

import com.user.demo.config.AppConfig;
import com.user.demo.dto.PhoneDTO;
import com.user.demo.dto.UserDTO;
import com.user.demo.dto.UserResponseDTO;
import com.user.demo.exceptions.CustomException;
import com.user.demo.repository.UserRepository;
import com.user.demo.model.Users;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface UserServiceHelper {
    static void validateUserDto(UserDTO userDto, UserRepository userRepository, AppConfig appConfig) {
        firstValidationUserDTO(userDto);
        if (userRepository.existsByEmail(userDto.email())) {
            throw new CustomException("El correo ya registrado", HttpStatus.CONFLICT);
        }

        String passwordPattern = appConfig.getPasswordPattern();
        if (!userDto.password().matches(passwordPattern)) {
            throw new CustomException("La contraseña no cumple con los requisitos", HttpStatus.BAD_REQUEST);
        }
    }
    static Users createUserEntity(UserDTO userDto, PhoneConverter phoneConverter) {
        Users users = new Users();
        users.setUuid(UUID.randomUUID());
        users.setName(userDto.name());
        users.setEmail(userDto.email());
        users.setPassword(encodePassword(userDto.password()));
        users.setActive(true);
        users.setLastLogin(LocalDateTime.now());

        if (userDto.phones() != null) {
            userDto.phones().forEach(phoneDTO ->
                    users.addPhone(phoneConverter.convertToEntity(phoneDTO)));
        }

        return users;
    }

    private static String encodePassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

/*    default UserResponseDTO convertToUserResponseDTO(Users users) {
        return new UserResponseDTO(
                users.getUuid(),
                users.getName(),
                users.getEmail(),
                users.getCreated(),
                users.getModified(),
                users.getLastLogin(),
                users.getToken(),
                users.isActive()
        );
    }*/
    private static void firstValidationUserDTO(UserDTO userDTO) {
        if (userDTO.name() == null || userDTO.name().isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (userDTO.email() == null || userDTO.email().isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (userDTO.password() == null || userDTO.password().isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        if (userDTO.phones() != null && !userDTO.phones().isEmpty()) {
            for (PhoneDTO phoneDTO : userDTO.phones()) {
                validatePhoneDTO(phoneDTO);
            }
        }
    }
    private static void validatePhoneDTO(PhoneDTO phoneDTO) {
        if (phoneDTO.number() == null || phoneDTO.number().isBlank()) {
            throw new IllegalArgumentException("El número de teléfono no puede estar vacío");
        }
        if (phoneDTO.citycode() == null || phoneDTO.citycode().isBlank()) {
            throw new IllegalArgumentException("El código de ciudad no puede estar vacío");
        }
        if (phoneDTO.countrycode() == null || phoneDTO.countrycode().isBlank()) {
            throw new IllegalArgumentException("El código de país no puede estar vacío");
        }
    }

    default UserResponseDTO convertToUserResponseDTO(Users users) {
        List<PhoneDTO> phoneDTOs = users.getPhones().stream()
                .map(phone -> new PhoneDTO(phone.getNumber(), phone.getCitycode(), phone.getCountrycode()))
                .collect(Collectors.toList());

        return new UserResponseDTO(
                users.getUuid(),
                users.getName(),
                users.getEmail(),
                users.getCreated(),
                users.getModified(),
                users.getLastLogin(),
                users.getToken(),
                users.isActive(),
                phoneDTOs
        );
    }

}

