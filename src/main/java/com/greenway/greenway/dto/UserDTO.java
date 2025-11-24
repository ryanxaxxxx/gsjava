package com.greenway.greenway.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message = "user.name.notblank")
    private String name;

    @Email(message = "user.email.invalid")
    @NotBlank(message = "user.email.notblank")
    private String email;

    private String companyEmail;

    private String address;

    private String transportMode;

    private Integer points;

    @NotBlank(message = "user.phone.notblank")
    @Pattern(regexp = "\\+?[0-9]{10,15}", message = "user.phone.invalid")
    private String phone;

    @NotBlank(message = "user.password.notblank")
    private String password;
}
