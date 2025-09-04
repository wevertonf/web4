<%@page import="model.ProdutoModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Editar Produto</title>

</head>
<body>
    <h1>Editar Produto</h1>
    
    <%
        ProdutoModel produto = (ProdutoModel) request.getAttribute("produto");
        String erro = (String) request.getAttribute("erro");
        
        if (erro != null && !erro.isEmpty()) {
    %>
        <p class="erro"><%= erro %></p>
    <%
        }
        
        if (produto != null) {
    %>
        <form method="POST" action="<%= request.getContextPath() %>/produtos/atualizar?id=<%= produto.getId() %>">
            <div class="form-group">
                <label for="nome">Nome do Produto:</label>
                <input type="text" id="nome" name="nome" value="<%= produto.getNome() %>" required>
            </div>
            
            <div class="form-group">
                <label for="preco">Preço (R$):</label>
                <input type="number" id="preco" name="preco" step="0.01" min="0" 
                       value="<%= String.format("%.2f", produto.getPreco()).replace(",", ".") %>" required>
            </div>
            
            <button type="submit" class="btn">Atualizar Produto</button>
            <a href="<%= request.getContextPath() %>/produtos" class="btn btn-secondary">Cancelar</a>
        </form>
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