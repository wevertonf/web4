<%@page import="model.UsuarioModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Detalhes do Usuário</title>
</head>
<body>
    <h1>Detalhes do Usuário</h1>
    
    <%
        UsuarioModel usuario = (UsuarioModel) request.getAttribute("usuario");
        if (usuario != null && usuario.getId() != 0) {
    %>
        <table border="1">
            <tr>
                <th>ID</th>
                <td><%= usuario.getId() %></td>
            </tr>
            <tr>
                <th>Nome</th>
                <td><%= usuario.getNome() %></td>
            </tr>
            <tr>
                <th>Email</th>
                <td><%= usuario.getEmail() %></td>
            </tr>
        </table>
        <br>
        <a href="<%= request.getContextPath() %>/listarUsuarios">Voltar para lista</a>
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