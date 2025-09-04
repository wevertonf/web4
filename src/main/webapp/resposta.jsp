<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
	String mensagem = (String)request.getAttribute("mensagem");

	out.println(mensagem);
	
	
%>

<br>
<a href="<%= request.getContextPath() %>/listarUsuarios">Voltar para home</a>


</body>
</html>