<%@page import="model.UsuarioModel"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
    <h1>Lista de Usuários</h1>
    
    <!-- Mostrar mensagem se existir -->
    <%-- <%
        String mensagem = (String) request.getAttribute("mensagem");
        if (mensagem != null && !mensagem.isEmpty()) {
    %>
        	<p class="mensagem"><%= mensagem %></p>
    <%
        }
    %> --%>
    
    <%
        List<UsuarioModel> usuarios = (List<UsuarioModel>) request.getAttribute("listaDeUsuarios");
        
        if (usuarios != null && !usuarios.isEmpty()) {
    %>
        <table>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Email</th>
                <th>Ações</th>
            </tr>
            <%
                for (UsuarioModel usuario : usuarios) {
            %>
                <tr>
                    <td><%= usuario.getId() %></td>
                    <td><%= usuario.getNome() %></td>
                    <td><%= usuario.getEmail() %></td>
                    <td>
                        <a href="<%= request.getContextPath() %>/detalhar?id=<%= usuario.getId() %>">Detalhar</a> |
                        <a href="<%= request.getContextPath() %>/editar?id=<%= usuario.getId() %>">Editar</a> |
                        <a href="<%= request.getContextPath() %>/deletar?id=<%= usuario.getId() %>" 
                           onclick="return confirm('Tem certeza que deseja deletar este usuário?')">Excluir</a>
                    </td>
                </tr>
            <%
                }
            %>
        </table>
    <%
        } else {
    %>
        <p>Nenhum usuário encontrado.</p>
    <%
        }
    %>
    
    <br>
    <%-- <a href="<%= request.getContextPath() %>/">Voltar para home</a> --%>
</body>
</html>