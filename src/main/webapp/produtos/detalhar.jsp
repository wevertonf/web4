<%@page import="model.ProdutoModel"%>
<%@page import="model.UsuarioModel"%> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Detalhes do Produto</title>

</head>
<body>
    <h1>Detalhes do Produto</h1>
    
    <%
        ProdutoModel produto = (ProdutoModel) request.getAttribute("produto");
        UsuarioModel usuarioLogado = (UsuarioModel) session.getAttribute("usuario");
        
        if (produto != null) {
    %>
        <div class="detalhes">
            <div class="detalhe-item">
                <span class="detalhe-label">ID:</span>
                <%= produto.getId() %>
            </div>
            <div class="detalhe-item">
                <span class="detalhe-label">Nome:</span>
                <%= produto.getNome() %>
            </div>
            <div class="detalhe-item">
                <span class="detalhe-label">Preço:</span>
                R$ <%= String.format("%.2f", produto.getPreco()).replace(".", ",") %>
            </div>
            <div class="detalhe-item">
                <span class="detalhe-label">Usuário:</span>
                <% if (produto.getUsuario() != null) { %>
                    <a href="<%= request.getContextPath() %>/produtos/usuario?id=<%= produto.getUsuario().getId() %>">
                        <%= produto.getUsuario().getNome() %>
                    </a>
                <% } else { %>
                    Não associado
                <% } %>
            </div>
        </div>
        
        <div>
            <a href="<%= request.getContextPath() %>/produtos" class="btn btn-secondary">Voltar</a>
            
            <% if (usuarioLogado != null && produto.getUsuario() != null && 
                   produto.getUsuario().getId() == usuarioLogado.getId()) { %>
                <a href="<%= request.getContextPath() %>/produtos/editar?id=<%= produto.getId() %>" class="btn btn-warning">Editar</a>
                <a href="<%= request.getContextPath() %>/produtos/deletar?id=<%= produto.getId() %>" 
                   class="btn btn-danger"
                   onclick="return confirm('Tem certeza que deseja excluir este produto?')">Excluir</a>
            <% } %>
        </div>
    <%
        } else {
    %>
        <p>Produto não encontrado.</p>
        <a href="<%= request.getContextPath() %>/produtos" class="btn btn-secondary">Voltar</a>
    <%
        }
    %>
</body>
</html>