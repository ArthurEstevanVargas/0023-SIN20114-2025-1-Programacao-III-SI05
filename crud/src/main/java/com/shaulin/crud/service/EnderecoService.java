package com.shaulin.crud.service;

import com.shaulin.crud.model.Endereco;
import com.shaulin.crud.repositories.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository repository;
    private final RestTemplate restTemplate;

    public Endereco buscarCep(String cep) {
        validarCep(cep);

        return repository.findById(cep)
                .orElseGet(() -> {
                    Endereco endereco = consultarViaCep(cep);
                    return repository.save(endereco);
                });
    }

    private Endereco consultarViaCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        try {
            Endereco endereco = restTemplate.getForObject(url, Endereco.class);

            if (endereco == null || endereco.getCep() == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CEP não encontrado");
            }

            // Preenche campos adicionais que não vêm da API
            endereco.setEstado(endereco.getUf()); // Simplificação - poderia mapear para nome completo
            endereco.setRegiao(obterRegiaoPorUF(endereco.getUf()));

            return endereco;

        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CEP não encontrado na API ViaCEP");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Erro ao consultar API ViaCEP: " + e.getMessage());
        }
    }

    private void validarCep(String cep) {
        if (cep == null || !cep.matches("\\d{8}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "CEP inválido. Deve conter 8 dígitos numéricos");
        }
    }

    private String obterRegiaoPorUF(String uf) {
        // Implementação simplificada - poderia ser um enum ou tabela no banco
        return switch (uf) {
            case "SP", "RJ", "MG", "ES" -> "Sudeste";
            case "PR", "SC", "RS" -> "Sul";
            case "MT", "MS", "GO", "DF" -> "Centro-Oeste";
            case "AM", "AC", "RO", "RR", "PA", "AP", "TO" -> "Norte";
            case "BA", "SE", "AL", "PE", "PB", "RN", "CE", "PI", "MA" -> "Nordeste";
            default -> "Região não identificada";
        };
    }
}