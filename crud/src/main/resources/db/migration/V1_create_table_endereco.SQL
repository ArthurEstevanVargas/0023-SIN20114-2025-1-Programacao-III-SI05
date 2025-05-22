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