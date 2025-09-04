<%@page import="model.UsuarioModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Editar Usuário</title>
</head>
<body>
    <h1>Editar Usuário</h1>
    
    <%
        UsuarioModel usuario = (UsuarioModel) request.getAttribute("usuario");
        
        if (usuario != null && usuario.getId() != 0) {
    %>
        <form method="POST" action="<%= request.getContextPath() %>/atualizar?id=<%= usuario.getId() %>">
            <label for="nome">Nome:</label>
            <input type="text" id="nome" name="nome" value="<%= usuario.getNome() %>" required>
            <br><br>
            
            <%-- <label for="email">Email:</label>
            <input type="email" id="email" name="email" value="<%= usuario.getEmail() %>" required>
            <br><br> --%>
            
            <!-- Não mostramos a senha no editar para segurança -->
            <button type="submit">Atualizar</button>
            <a href="<%= request.getContextPath() %>/listarUsuarios">Cancelar</a>
        </form>
    <%
        } else {
    %>
        <p>Usuário não encontrado.</p>
        <a href="<%= request.getContextPath() %>/listarUsuarios">Voltar para lista</a>
    <%
        }
    %>
</body>
</html>