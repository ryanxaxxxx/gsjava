package com.greenway.greenway.controller;

import com.greenway.greenway.dto.UserDTO;
import com.greenway.greenway.model.User;
import com.greenway.greenway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ViewController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            model.addAttribute("username", auth.getName());
        } else {
            model.addAttribute("username", "Usuário");
        }
        return "home";
    }

    @GetMapping("/usuarios/cadastrar")
    public String cadastrarUsuario(Model model) {
        // Criar um objeto User vazio para o formulário
        User usuario = new User();
        model.addAttribute("usuario", usuario);
        return "form_usuario";
    }

    @PostMapping("/usuarios/salvar")
    public String salvarUsuario(@ModelAttribute User usuario, RedirectAttributes redirectAttributes) {
        try {
            // Converter User para UserDTO
            UserDTO userDTO = new UserDTO();
            userDTO.setName(usuario.getName());
            userDTO.setEmail(usuario.getEmail());
            userDTO.setPassword(usuario.getPassword());
            userDTO.setPhone(usuario.getPhone() != null ? usuario.getPhone() : "00000000000");
            userDTO.setPoints(0); // Pontos iniciais
            
            userService.create(userDTO);
            redirectAttributes.addFlashAttribute("success", "Usuário cadastrado com sucesso!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao cadastrar usuário: " + e.getMessage());
            return "redirect:/usuarios/cadastrar";
        }
    }
}
