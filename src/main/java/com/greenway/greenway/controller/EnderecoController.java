package com.greenway.greenway.controller;

import com.greenway.greenway.model.Endereco;
import com.greenway.greenway.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoRepository repository;

    @PostMapping(consumes = {"application/x-www-form-urlencoded", "multipart/form-data"})
    public ResponseEntity<?> salvar(
            @RequestParam("endereco") String endereco,
            @RequestParam("numero") String numero,
            @RequestParam("cep") String cep) {
        try {
            log.info("Salvando endereço: endereco={}, numero={}, cep={}", endereco, numero, cep);
            
            // Validar se os campos obrigatórios estão preenchidos
            if (endereco == null || endereco.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Campo 'endereco' é obrigatório");
            }
            
            Endereco novoEndereco = new Endereco();
            novoEndereco.setEndereco(endereco);
            novoEndereco.setNumero(numero);
            novoEndereco.setCep(cep);
            
            Endereco enderecoSalvo = repository.save(novoEndereco);
            log.info("Endereço salvo com sucesso: id={}", enderecoSalvo.getId());
            return ResponseEntity.ok(enderecoSalvo);
        } catch (Exception e) {
            log.error("Erro ao salvar endereço", e);
            return ResponseEntity.internalServerError()
                    .body("Erro ao salvar endereço: " + e.getMessage());
        }
    }
    
    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<?> salvarJson(@RequestBody Endereco endereco) {
        try {
            log.info("Salvando endereço via JSON: endereco={}, numero={}, cep={}", 
                    endereco.getEndereco(), endereco.getNumero(), endereco.getCep());
            
            // Validar se os campos obrigatórios estão preenchidos
            if (endereco.getEndereco() == null || endereco.getEndereco().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Campo 'endereco' é obrigatório");
            }
            
            Endereco enderecoSalvo = repository.save(endereco);
            log.info("Endereço salvo com sucesso: id={}", enderecoSalvo.getId());
            return ResponseEntity.ok(enderecoSalvo);
        } catch (Exception e) {
            log.error("Erro ao salvar endereço", e);
            return ResponseEntity.internalServerError()
                    .body("Erro ao salvar endereço: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        try {
            log.info("Listando endereços com paginação: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
            Page<Endereco> enderecos = repository.findAll(pageable);
            log.info("Encontrados {} endereços (total: {})", enderecos.getNumberOfElements(), enderecos.getTotalElements());
            return ResponseEntity.ok(enderecos);
        } catch (Exception e) {
            log.error("Erro ao listar endereços", e);
            return ResponseEntity.internalServerError()
                    .body("Erro ao listar endereços: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            log.info("Buscando endereço com id: {}", id);
            Endereco endereco = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
            return ResponseEntity.ok(endereco);
        } catch (Exception e) {
            log.error("Erro ao buscar endereço", e);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestParam("endereco") String endereco,
            @RequestParam("numero") String numero,
            @RequestParam("cep") String cep) {
        try {
            log.info("Atualizando endereço id={}: endereco={}, numero={}, cep={}", id, endereco, numero, cep);
            
            Endereco enderecoExistente = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
            
            if (endereco == null || endereco.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Campo 'endereco' é obrigatório");
            }
            
            enderecoExistente.setEndereco(endereco);
            enderecoExistente.setNumero(numero);
            enderecoExistente.setCep(cep);
            
            Endereco enderecoAtualizado = repository.save(enderecoExistente);
            log.info("Endereço atualizado com sucesso: id={}", enderecoAtualizado.getId());
            return ResponseEntity.ok(enderecoAtualizado);
        } catch (Exception e) {
            log.error("Erro ao atualizar endereço", e);
            return ResponseEntity.internalServerError()
                    .body("Erro ao atualizar endereço: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = {"application/json"})
    public ResponseEntity<?> atualizarJson(@PathVariable Long id, @RequestBody Endereco endereco) {
        try {
            log.info("Atualizando endereço via JSON id={}: endereco={}, numero={}, cep={}", 
                    id, endereco.getEndereco(), endereco.getNumero(), endereco.getCep());
            
            Endereco enderecoExistente = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
            
            if (endereco.getEndereco() == null || endereco.getEndereco().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Campo 'endereco' é obrigatório");
            }
            
            enderecoExistente.setEndereco(endereco.getEndereco());
            enderecoExistente.setNumero(endereco.getNumero());
            enderecoExistente.setCep(endereco.getCep());
            
            Endereco enderecoAtualizado = repository.save(enderecoExistente);
            log.info("Endereço atualizado com sucesso: id={}", enderecoAtualizado.getId());
            return ResponseEntity.ok(enderecoAtualizado);
        } catch (Exception e) {
            log.error("Erro ao atualizar endereço", e);
            return ResponseEntity.internalServerError()
                    .body("Erro ao atualizar endereço: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            log.info("Excluindo endereço com id: {}", id);
            
            if (!repository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            
            repository.deleteById(id);
            log.info("Endereço excluído com sucesso: id={}", id);
            return ResponseEntity.ok().body("Endereço excluído com sucesso");
        } catch (Exception e) {
            log.error("Erro ao excluir endereço", e);
            return ResponseEntity.internalServerError()
                    .body("Erro ao excluir endereço: " + e.getMessage());
        }
    }
}
