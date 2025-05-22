package com.shaulin.crud.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "endereco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {

    @Id
    @Column(length = 9)
    private String cep;

    @Column(length = 255)
    private String logradouro;

    @Column(length = 255)
    private String complemento;

    @Column(length = 255)
    private String bairro;

    @Column(length = 255)
    private String localidade;

    @Column(length = 2)
    private String uf;

    @Column(length = 100)
    private String estado;

    @Column(length = 100)
    private String regiao;

    @Column(length = 20)
    private String ibge;

    @Column(length = 20)
    private String gia;

    @Column(length = 5)
    private String ddd;

    @Column(length = 10)
    private String siafi;
}