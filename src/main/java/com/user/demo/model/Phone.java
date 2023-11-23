package com.user.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@Table(name = "phone")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de teléfono no puede estar vacío")
    @Pattern(regexp = "^[0-9]+$", message = "El número de teléfono debe ser numérico")
    @Size(min = 7, max = 10, message = "El número de teléfono debe tener entre 7 y 10 dígitos")
    @Column(nullable = false)
    private String number;

    @NotBlank(message = "El código de ciudad no puede estar vacío")
    @Column(nullable = false)
    private String citycode;

    @NotBlank(message = "El código de país no puede estar vacío")
    @Column(nullable = false)
    private String countrycode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private Users users;

}

