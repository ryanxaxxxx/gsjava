package com.greenway.greenway.service;

import com.greenway.greenway.model.User;
import com.greenway.greenway.dto.UserDTO;
import com.greenway.greenway.mapper.UserMapper;
import com.greenway.greenway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // CREATE USER
    @CacheEvict(value = "users", allEntries = true)
    public UserDTO create(UserDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("user.email.duplicate");
        }

        User user = mapper.toEntity(dto);

        user.setPoints(0);
        // Criptografa a senha antes de salvar
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            throw new RuntimeException("user.password.required");
        }
        
        // Define role padrão se não fornecida
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        user = userRepository.save(user);

        return mapper.toDto(user);
    }

    // FIND ALL USERS (com paginação)
    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(mapper::toDto);
    }

    // FIND ALL USERS (sem paginação - para compatibilidade)
    @Cacheable(value = "users", key = "'all'")
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(mapper::toDto)
                .toList();
    }

    // FIND USER BY ID
    @Cacheable(value = "users", key = "#id")
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user.notfound"));
        return mapper.toDto(user);
    }

    // UPDATE USER
    @CacheEvict(value = "users", key = "#id")
    public UserDTO update(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user.notfound"));

        mapper.updateEntityFromDto(dto, user); // mapeia name, email, phone, address, transportMode

        user = userRepository.save(user);

        return mapper.toDto(user);
    }

    // DELETE USER
    @CacheEvict(value = "users", key = "#id")
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("user.notfound");
        }
        userRepository.deleteById(id);
    }
}
