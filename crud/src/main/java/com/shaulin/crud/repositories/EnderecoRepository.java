package com.shaulin.crud.repositories;

import com.shaulin.crud.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {}