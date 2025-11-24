package com.greenway.greenway.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "user.name.notblank")
    private String name;

    @Email(message = "user.email.invalid")
    @Column(unique = true, nullable = false)
    private String email;

    private String companyEmail;

    private String address;

    private String transportMode;

    @Column(nullable = false)
    private Integer points;

    @Column(nullable = false)
    private String password;

    private String role;

    @NotBlank(message = "user.phone.notblank")
    @Pattern(regexp = "\\+?[0-9]{10,15}", message = "user.phone.invalid")
    private String phone;
}
