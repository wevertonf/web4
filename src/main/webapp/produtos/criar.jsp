<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cadastrar Produto</title>

</head>
<body>
    <h1>Cadastrar Novo Produto</h1>
    
    <!-- P/ erros; Ver se aparece msm -->
    <%
        String erro = (String) request.getAttribute("erro");
        if (erro != null && !erro.isEmpty()) {
    %>
        <p class="erro"><%= erro %></p>
    <%
        }
    %>
    
    <form method="POST" action="<%= request.getContextPath() %>/produtos/salvar">
        <div class="form-group">
            <label for="nome">Nome do Produto:</label>
            <input type="text" id="nome" name="nome" required>
        </div>
        
        <div class="form-group">
            <label for="preco">Preço (R$):</label>
            <input type="number" id="preco" name="preco" step="0.01" min="0" required>
        </div>
        
        <button type="submit" class="btn">Cadastrar Produto</button>
        <a href="<%= request.getContextPath() %>/produtos" class="btn btn-secondary">Cancelar</a>
    </form>
</body>
</html>