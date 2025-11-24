package com.greenway.greenway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnderecoViewController {

    @GetMapping("/enderecos")
    public String listaEnderecos() {
        return "forward:/lista-enderecos.html";
    }
}

