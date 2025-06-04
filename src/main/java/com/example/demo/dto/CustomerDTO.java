package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {

    private Long id;

    @Email(message = "Nieprawidłowy email")
    @NotBlank(message = "Email nie może być pusty")
    private String email;

    @Size(min = 6)
    private String password;

    @NotBlank(message = "Imię nie może być puste")
    private String name;


}
