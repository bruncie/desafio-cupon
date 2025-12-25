# Desafio - Gerador de Cupons

Este repositório contém uma aplicação Spring Boot (Maven) para geração de cupons.

Resumo rápido
- Porta da aplicação: 8080
- H2 Console: /h2-console
- Swagger (OpenAPI UI): /swagger-ui.html ou /swagger-ui/index.html
- OpenAPI JSON: /v3/api-docs

Requisitos
- Java 17
- Maven (ou uso do wrapper `./mvnw`)
- Docker e Docker Compose (opcional, para rodar em container)

Instalação e execução local (sem Docker)
1. Gerar o artefato (JAR):

```bash
# macOS / zsh
./mvnw -B -DskipTests package
```

2. Executar a aplicação (jar gerado em `target/`):

```bash
java -jar target/desafio-0.0.1-SNAPSHOT.jar
```

Executando com Docker Compose (recomendado)

1. Build e subir serviços:

```bash
docker-compose up --build
```

2. Para rodar em background:

```bash
docker-compose up --build -d
```

3. Parar e remover contêineres:

```bash
docker-compose down
```

URLs importantes
- API: http://localhost:8080
- Swagger UI (interface):
  - http://localhost:8080/swagger-ui.html
  - ou http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- H2 Console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:/data/testdb`
  - User: `sa`
  - Password: (vazio)

Observações sobre H2 e persistência
- O `application.properties` foi configurado para usar H2 em arquivo: `jdbc:h2:file:/data/testdb;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE`.
- O `docker-compose.yml` monta um volume nomeado `desafio_h2_data` no caminho `/data` do container, assim os arquivos do H2 serão persistidos entre reinícios.

Nota sobre o JAR e Dockerfile
- O `Dockerfile` multi-stage assume que o build Maven produz `target/desafio-0.0.1-SNAPSHOT.jar` (conforme `pom.xml`). Se você alterar `artifactId`/`version` no `pom.xml`, atualize o `Dockerfile`.

Problemas comuns / troubleshooting
- Erro ao buildar com Docker: verifique se os arquivos `mvnw` e diretório `.mvn` não estão ignorados pelo contexto do Docker (não crie `.dockerignore` que exclua estes itens se quiser usar o wrapper dentro do contexto).
- Se o Swagger não aparecer: confirme que a dependência `springdoc-openapi` está presente no `pom.xml` (já está) e que a aplicação subiu sem erros.

Comandos úteis

```bash
# logs do container
docker-compose logs -f app

# inspecionar o volume Docker
docker volume ls
docker volume inspect desafio_h2_data

# executar um shell no container
docker-compose exec app sh
# checar /data dentro do container
ls -la /data
```
