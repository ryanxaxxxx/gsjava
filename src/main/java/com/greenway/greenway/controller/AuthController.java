package com.greenway.greenway.controller;

import com.greenway.greenway.dto.LoginDTO;
import com.greenway.greenway.dto.UserDTO;
import com.greenway.greenway.security.JwtTokenProvider;
import com.greenway.greenway.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider provider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO dto) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
            );

            String token = provider.generateToken((UserDetails) auth.getPrincipal());

            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciais inválidas"));
        }
    }

    @PostMapping(value = "/register", consumes = {"application/x-www-form-urlencoded", "multipart/form-data"})
    public ResponseEntity<?> register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {
        try {
            log.info("Registrando novo usuário: name={}, email={}", name, email);
            
            // Validar campos obrigatórios
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Campo 'name' é obrigatório");
            }
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Campo 'email' é obrigatório");
            }
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Campo 'password' é obrigatório");
            }
            
            // Criar DTO
            UserDTO userDTO = new UserDTO();
            userDTO.setName(name);
            userDTO.setEmail(email);
            userDTO.setPassword(password);
            userDTO.setPhone("00000000000"); // Telefone padrão temporário (pode ser opcional depois)
            
            // Salvar usuário
            UserDTO createdUser = userService.create(userDTO);
            log.info("Usuário registrado com sucesso: id={}, email={}", createdUser.getId(), createdUser.getEmail());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Usuário cadastrado com sucesso!", "id", createdUser.getId()));
        } catch (RuntimeException e) {
            log.error("Erro ao registrar usuário", e);
            String errorMessage = e.getMessage();
            if (errorMessage.contains("duplicate")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Email já cadastrado"));
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("error", errorMessage));
        } catch (Exception e) {
            log.error("Erro ao registrar usuário", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao cadastrar usuário: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/register", consumes = {"application/json"})
    public ResponseEntity<?> registerJson(@RequestBody UserDTO userDTO) {
        try {
            log.info("Registrando novo usuário via JSON: name={}, email={}", userDTO.getName(), userDTO.getEmail());
            
            // Validar campos obrigatórios
            if (userDTO.getName() == null || userDTO.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Campo 'name' é obrigatório"));
            }
            if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Campo 'email' é obrigatório"));
            }
            if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Campo 'password' é obrigatório"));
            }
            
            // Definir telefone padrão se não fornecido
            if (userDTO.getPhone() == null || userDTO.getPhone().trim().isEmpty()) {
                userDTO.setPhone("00000000000");
            }
            
            // Salvar usuário
            UserDTO createdUser = userService.create(userDTO);
            log.info("Usuário registrado com sucesso: id={}, email={}", createdUser.getId(), createdUser.getEmail());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Usuário cadastrado com sucesso!", "id", createdUser.getId()));
        } catch (RuntimeException e) {
            log.error("Erro ao registrar usuário", e);
            String errorMessage = e.getMessage();
            if (errorMessage.contains("duplicate")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Email já cadastrado"));
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("error", errorMessage));
        } catch (Exception e) {
            log.error("Erro ao registrar usuário", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao cadastrar usuário: " + e.getMessage()));
        }
    }
}
