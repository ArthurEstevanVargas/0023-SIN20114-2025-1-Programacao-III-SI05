package com.shaulin.crud.controllers;

import com.shaulin.crud.dtos.EnderecoDTO;
import com.shaulin.crud.model.Endereco;
import com.shaulin.crud.service.EnderecoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/cep")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService service;

    @GetMapping("/{cep}")
    public ResponseEntity<EnderecoDTO> consultarCep(@PathVariable String cep) {
        Endereco endereco = service.buscarCep(cep);

        EnderecoDTO dto = EnderecoDTO.builder()
                .cep(endereco.getCep())
                .logradouro(endereco.getLogradouro())
                .complemento(endereco.getComplemento())
                .bairro(endereco.getBairro())
                .localidade(endereco.getLocalidade())
                .uf(endereco.getUf())
                .estado(endereco.getEstado())
                .regiao(endereco.getRegiao())
                .ibge(endereco.getIbge())
                .gia(endereco.getGia())
                .ddd(endereco.getDdd())
                .siafi(endereco.getSiafi())
                .build();

        return ResponseEntity.ok(dto);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }
}