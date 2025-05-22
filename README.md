## 🚀 CONFIGURAÇÃO DO PROJETO SPRING BOOT COM INTELLIJ IDEA 

#### 🛠️ Passo 1: Instalar o IntelliJ IDEA  
Certifique-se de ter o IntelliJ IDEA instalado em sua máquina. O IntelliJ possui duas versões:  
- **Community (Gratuita):** Com funcionalidades básicas suficientes para muitos projetos.  
- **Ultimate (Paga):** Inclui recursos avançados, especialmente úteis para projetos empresariais.  

##### 💡 Importante: Se você estiver cursando faculdade, pode solicitar gratuitamente uma licença educacional para a versão Ultimate por meio do [JetBrains Student Program](https://www.jetbrains.com/community/education/#students). Com essa licença, você terá acesso a todas as ferramentas da JetBrains sem custo enquanto for estudante. Basta criar uma conta com o e-mail institucional ou enviar um comprovante de matrícula.  

---

#### 🧑‍💻 Passo 2: Configurar o Projeto no Spring Initializr  
1. Acesse o [Spring Initializr](https://start.spring.io/).  
2. Configure as seguintes opções:  
   - **Project:** Maven  
   - **Language:** Java  
   - **Spring Boot Version:** Mantenha a versão padrão (geralmente a mais recente).  

3. No campo **Project Metadata**, personalize conforme necessário:  
   - **Group:** (ex: com.seuprojeto)  
   - **Artifact:** (ex: meu-aplicativo)  
   - **Name:** (ex: MeuAplicativo)  
   - **Description:** Uma breve descrição do projeto  
   - **Package Name:** Deixe no padrão com base no Group e Artifact escolhidos  

4. **Packaging:** Selecione **JAR**  
5. **Java Version:** Deixe configurada com a versão mais recente compatível com seu ambiente.  

---

#### 📦 Passo 3: Adicionar Dependências  
Inclua as seguintes dependências no projeto:  
- **Spring Web:** Para criação de APIs RESTful  
- **Spring Data JPA:** Para persistência de dados  
- **PostgreSQL Driver:** Para conexão com banco de dados PostgreSQL  
- **Flyway Migration:** Para controle de versionamento do banco de dados  

---

#### 📂 Passo 4: Baixar o Projeto  
1. Clique em **Generate** para baixar o projeto.  
2. Extraia o arquivo baixado e abra a pasta resultante no IntelliJ IDEA.  

<br>

## ⚙️ CONFIGURAÇÃO DO `application.properties`

A primeira configuração importante do projeto é o arquivo `application.properties`, geralmente localizado em:  
`src > main > resources`

As propriedades deste arquivo variam de acordo com o banco de dados que você está utilizando. Abaixo está uma configuração básica para o **PostgreSQL**:

```properties
spring.application.name=crud

# ===============================
# = DATA SOURCE (PostgreSQL)
# ===============================
spring.datasource.url=jdbc:postgresql://localhost:5432/edereco-API
spring.datasource.username=postgres
spring.datasource.password=123456
spring.datasource.driver-class-name=org.postgresql.Driver

# ===============================
# = JPA/HIBERNATE
# ===============================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

# ===============================
# = API CONFIG
# ===============================
app.viacep.url=https://viacep.com.br/ws/
```

#### 🔧 Explicação das Propriedades

**spring.datasource.url**
> Define a URL de conexão com o banco de dados.

**spring.datasource.username**
> Define o nome de usuário para acessar o banco de dados.

**spring.datasource.password**
> Define a senha correspondente ao usuário do banco.

##### 💡 Importante: Certifique-se de alterar os placeholders (HOST, PORT, DATABASE_NAME, SEU_USUARIO, SUA_SENHA) para os valores corretos do seu ambiente antes de rodar o projeto.

<br>

## 🗂️ ESTRUTURA DO PROJETO E ORDEM DE CRIAÇÃO

A seguir está a ordem recomendada para a criação das camadas principais do projeto:

#### **1. Model (Entidades)**  
#### **Motivo:**  
O Model representa a estrutura dos dados no sistema. É a base para definir como o banco de dados e as camadas superiores vão se comportar.

#### **O que fazer:**  
- Defina as classes que representam as tabelas do banco de dados.  
- Inclua as anotações do JPA (`@Entity`, `@Table`, `@Id`, etc.).  

#### **Exemplo:**  
```java
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
```

---

#### **2. DTOs (Data Transfer Objects)**  
#### **Motivo:**  
Facilitar a transferência de dados entre a camada de controle e a lógica de negócio, evitando expor diretamente a entidade do banco de dados.

#### **O que fazer:**  
- Crie classes simples com os atributos necessários para requests e responses.
- Evite incluir lógica nas classes DTOs.

#### **Exemplo:**  
```java
package com.shaulin.crud.dtos;

import lombok.Builder;

@Builder
public record EnderecoDTO(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        String estado,
        String regiao,
        String ibge,
        String gia,
        String ddd,
        String siafi
) {}
```

---

#### **3. Repositories**  
#### **Motivo:**  
São responsáveis pelo acesso ao banco de dados. Dependem diretamente das entidades definidas no **Model**.

#### **O que fazer:**  
- Crie interfaces que estendem ```JpaRepository``` para operações padrão de persistência.

#### **Exemplo:**  
```java
package com.shaulin.crud.repositories;

import com.shaulin.crud.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {}
```

---

#### **4. Service**  
#### **Motivo:**  
A camada de Service contém a lógica de negócio do sistema, intermediando as operações entre o Controller e o Repository. Ela também pode integrar com APIs externas (como a ViaCEP no exemplo).

#### **O que fazer:**  
- Implemente regras de negócio, validações e transformações de dados.
- Centralize chamadas a APIs externas ou outros serviços.
- Utilize a anotação @Service para identificação pelo Spring.

#### **Exemplo:**  
```java
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
```

---

#### **5. Controller**  
#### **Motivo:**  
A Controller gerencia as requisições HTTP e direciona para os serviços necessários.

#### **O que fazer:**  
- Crie endpoints RESTful que recebam os dados, validem e deleguem as operações aos serviços.

#### **Exemplo:**  
```java
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
```

---

#### **📋 Resumo da Ordem:**

1. Model (Entidades)
2. DTOs (Data Transfer Objects)
3. Repositories
4. Service
5. Controller

<br>

## 🛠️ MIGRAÇÕES DE BANCO DE DADOS COM FLYWAY

#### 🚀 **O que é Flyway?**
O Flyway é uma ferramenta que permite gerenciar a evolução do banco de dados de forma controlada, através de scripts versionados que são executados automaticamente quando o projeto inicia.  

#### 📄 **Exemplo de Script de Migração**

Aqui está um exemplo simples de um script para criar a tabela `product`:  

**Arquivo:** `V1_create_table_endereco.SQL`

```sql
CREATE TABLE endereco (
    cep VARCHAR(9) PRIMARY KEY,
    logradouro VARCHAR(255),
    complemento VARCHAR(255),
    bairro VARCHAR(255),
    localidade VARCHAR(255),
    uf VARCHAR(2),
    estado VARCHAR(100),
    regiao VARCHAR(100),
    ibge VARCHAR(20),
    gia VARCHAR(20),
    ddd VARCHAR(5),
    siafi VARCHAR(10)
);

-- Índice para melhorar consultas por UF (opcional)
CREATE INDEX idx_endereco_uf ON endereco(uf);
```

#### 💡 **Nomeação dos Arquivos:**

A convenção para nomear os arquivos de migração no Flyway é:
- ```V<versão>__<descrição>.sql```
- ```Exemplo: V1_create_table_endereco.SQL```

#### 📝 **Como Funciona?**

- Quando o projeto é executado, o Flyway verifica os scripts de migração na pasta padrão (```src/main/resources/db/migration```).
- Ele aplica os scripts que ainda não foram executados, mantendo um histórico de migrações.

#### 🔧 **Dicas**

- **Organize os scripts:** Mantenha cada script de alteração de banco em arquivos separados com uma versão clara.
- **Versão sequencial:** A versão (```V1```, ```V2```, etc.) deve ser incrementada conforme você adiciona novas migrações.
- **Evite alterar scripts já executados:** Crie novos scripts para modificações adicionais.

<br>

## 📚 _References_

> Fontes recomendadas para ampliar seu conhecimento..

1. [Vídeo Tutorial: CRIANDO UM CRUD COM SPRING BOOT | API REST com PostgreSQL e Flyway](https://www.youtube.com/watch?v=ePGAm7mMFFg)
