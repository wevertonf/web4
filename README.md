# 🌐 Web4 - CRUDagem de Usuários e Produtos

Desenvolvido em Java com Servlets, JSP e MySQL para CRUDinagem de usuários e produtos.

## 🎯 Funcionalidades

### 👤 Usuários
- ✅ Cadastro de novos usuários com criptografia de senha (BCrypt)
- ✅ Autenticação e login
- ✅ Listagem de todos os usuários
- ✅ Detalhamento de usuário (id)
- ✅ Edição de dados cadastrais
- ✅ Deletagem de usuários

### 📦 Gerenciamento de Produtos
- ✅ Cadastro de produtos associados aos usuários
- ✅ Listagem de todos os produtos
- ✅ Listagem de produtos por usuário específico
- ✅ Detalhamento de produtos
- ✅ Edição de produtos (somente pelo proprietário)
- ✅ Exclusão de produtos (somente pelo proprietário)

### 🔐 Controle de Acesso
- Usuários logados podem criar e gerenciar seus produtos
- Usuários não logados podem apenas visualizar produtos
- Validação de propriedade para edição/exclusão

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java EE** - Plataforma de desenvolvimento enterprise
- **Servlets** - Controladores da aplicação
- **JSP** - Páginas dinâmicas
- **JDBC** - Conexão com banco de dados
- **BCrypt** - Criptografia de senhas

### Banco de Dados
- **MySQL** - Sistema de gerenciamento de banco de dados relacional

### Ferramentas e Servidores
- **Apache Tomcat 9.0** - Servidor web
- **Eclipse IDE** - Ambiente de desenvolvimento
- **Maven** - Gerenciamento de dependências
- **Git** - Controle de versão

## 📁 Estrutura do Projeto

```
web4/
├── src/main/java/
│   ├── controller/
│   │   ├── UsuarioController.java      # Controller web (JSP)
│   │   ├── ProdutoController.java      # Controller web (JSP)
│   │   ├── UsuarioAPIController.java   # API REST (JSON)
│   │   └── ProdutoAPIController.java   # API REST (JSON)
│   ├── dao/
│   │   ├── UsuarioDao.java             # Acesso a dados de usuários
│   │   └── ProdutoDao.java             # Acesso a dados de produtos
│   ├── model/
│   │   ├── UsuarioModel.java           # Modelo de usuário
│   │   └── ProdutoModel.java           # Modelo de produto
│   └── util/
│       └── SenhaUtil.java              # Utilitários de senha
├── src/main/webapp/
│   ├── produtos/
│   │   ├── listar.jsp
│   │   ├── criar.jsp
│   │   ├── editar.jsp
│   │   └── detalhar.jsp
│   ├── WEB-INF/
│   │   └── web.xml
│   ├── login.jsp
│   ├── registrar.jsp
│   ├── detalhar.jsp
│   ├── editar.jsp
│   ├── listar_usuarios.jsp
│   ├── arealogada.jsp
│   ├── resposta.jsp
│   └── index.jsp
└──
```

## 🚀 Como Executar o Projeto

### Pré-requisitos
- Java JDK 8 ou superior
- Apache Tomcat 9.0 ou superior
- MySQL 5.7 ou superior
- Eclipse IDE ou outra IDE Java
- Maven (opcional, mas recomendado)

### Configuração do Banco de Dados

```sql
-- Criar banco de dados
CREATE DATABASE web4;

-- Usar banco de dados
USE web4;

-- Tabela de usuários
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL
);

-- Tabela de produtos
CREATE TABLE produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    nome VARCHAR(255) NOT NULL,
    preco DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE SET NULL ON UPDATE CASCADE
);
```

### Configuração da Aplicação

1. **Importar projeto no Eclipse:**
   - File → Import → Existing Projects into Workspace
   - Selecionar a pasta do projeto

2. **Configurar Tomcat:**
   - Adicionar servidor Tomcat no Eclipse
   - Adicionar projeto ao servidor

3. **Configurar conexão com banco:**
   - Verificar classe ConexaoDAO com dados do seu MySQL

### Deploy e Execução

1. **Iniciar Tomcat** no Eclipse
2. **Acessar aplicação:**
   ```
   http://localhost:8080/web4/
   ```

## 🌐 Endpoints da API REST

### Usuários
- **GET** `/api/usuarios` - Listar todos usuários
- **GET** `/api/usuarios/{id}` - Buscar usuário por ID
- **POST** `/api/usuarios` - Criar novo usuário
- **PUT** `/api/usuarios/{id}` - Atualizar usuário
- **DELETE** `/api/usuarios/{id}` - Excluir usuário

### Produtos
- **GET** `/api/produtos` - Listar todos produtos
- **GET** `/api/produtos/{id}` - Buscar produto por ID
- **GET** `/api/produtos/usuario/{id}` - Listar produtos de usuário
- **POST** `/api/produtos` - Criar novo produto
- **PUT** `/api/produtos/{id}` - Atualizar produto
- **DELETE** `/api/produtos/{id}` - Excluir produto

### Exemplo de requisição POST para criar usuário:

```json
POST /api/usuarios
Content-Type: application/json

{
  "nome": "Piroca da Rola Pinto",
  "email": "prp@email.com",
  "senha": "123456"
}
```

## 🔧 Dependências Principais

```xml
<dependencies>
    <!-- Servlet API -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>4.0.1</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- JSP API -->
    <dependency>
        <groupId>javax.servlet.jsp</groupId>
        <artifactId>javax.servlet.jsp-api</artifactId>
        <version>2.3.3</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- JSTL -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>
        <version>1.2</version>
    </dependency>
    
    <!-- MySQL Connector -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
    
    <!-- BCrypt para senhas -->
    <dependency>
        <groupId>org.mindrot</groupId>
        <artifactId>jbcrypt</artifactId>
        <version>0.4</version>
    </dependency>
    
    <!-- JSON Library -->
    <dependency>
        <groupId>org.json</groupId>
        <artifactId>json</artifactId>
        <version>20250517</version>
    </dependency>
</dependencies>
```

## 🎯 Arquitetura do Sistema

### Padrão MVC (Model-View-Controller)
```
Model (DAO/Model) ←→ Controller (Servlet) ←→ View (JSP/API)
```

### Segurança
- ✅ Senhas criptografadas com BCrypt
- ✅ Validação de sessão
- ✅ Controle de acesso por permissões
- ✅ Proteção contra acesso não autorizado

### Boas Práticas Implementadas
- Separação de responsabilidades
- Tratamento de exceções
- Validação de dados
- Código reutilizável
- Estrutura organizada

## 📊 Diagrama de Entidades

```
Usuarios
┌─────────────┐
│ id (PK)     │
│ nome        │
│ email       │
│ senha       │
└─────────────┘
       │
       │ (1:N)
       ▼
Produtos
┌─────────────┐
│ id (PK)     │
│ usuario_id  │
│ nome        │
│ preco       │
└─────────────┘
```

## 🎨 Telas do Sistema

### Tela de Login
- Formulário de autenticação
- Validação de credenciais
- Redirecionamento pós-login

### Área Logada
- Principal do usuário
- Acesso às funcionalidades restritas
- Opção de logout

### Gerenciamento de Produtos
- Listagem com filtros (PENDENTE)
- Formulários de cadastro/edição
- Confirmação de exclusão

## 🚨 Tratamento de Erros

O sistema implementa tratamento completo de erros:
- Validação de entrada de dados
- Tratamento de exceções
- Mensagens agressivas ao usuário
- Logs para desenvolvedor

## 📈 Próximas Melhorias

- [ ] Implementação de paginação
- [ ] Sistema de busca avançada
- [ ] Upload de imagens para produtos
- [ ] Testes unitários automatizados
- [ ] Documentação da API com Swagger (será!?)

## 👨‍💻 Desenvolvedor

**Weverton F. Mesquita**
- GitHub: [@wevertonf](https://github.com/wevertonf)
- Email: wevertoff@yahoo.com

## 📄 Licença

Este projeto é desenvolvido para fins educacionais e não possui licença específica. Sinta-se livre para estudar e modificar o código.

## 🙏 Agradecimentos

- Professor Ederson Bastiani e colegas da disciplina de Programação Web IV
- Comunidade de desenvolvedores Java
- Documentação oficial das tecnologias utilizadas
- Cristiano Ronaldo pela mentalidade de vencedor
- Juan Pablo Vojvoda pela mentalidade de insistência e teimosia
- Agente GPT Qwen3-Coder

## 🆘 Suporte

Para dúvidas ou problemas com a execução do projeto, entre em contato através do GitHub Issues ou envie um email.

## 📤 Como enviar para o GitHub

### 1. **Adicionar README.md ao projeto**

1. No Eclipse, clique com botão direito no projeto `web4`
2. **New → Other → General → File**
3. **File name**: `README.md`
4. Cole o conteúdo acima
5. **Finish**

### 2. **Commit e Push**

1. Clique com botão direito no projeto
2. **Team → Commit**
3. Adicione mensagem: "Add README.md documentation"
4. **Commit and Push**

### 3. **Verificar no GitHub**

Acesse: `https://github.com/wevertonf/web4`

## ✅ Benefícios do README.md

- **Documentação clara** do projeto
- **Instruções de instalação** detalhadas
- **Referência rápida** para desenvolvedores
- **Profissionalismo** no GitHub
- **Melhor visibilidade** do seu trabalho
