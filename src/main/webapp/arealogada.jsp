<%@page import="model.UsuarioModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Área Logada</title>
</head>
<body>
<%
    UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");

    if (usuario != null && usuario.getId() != 0) {
        out.println("<h1>Bem-vindo, " + usuario.getNome() + "!</h1>");
        out.println("<a href='" + request.getContextPath() + "/logout'>Sair</a>");
    } else {
        response.sendRedirect("login.jsp");
    }
%>
</body>
</html>