<%@page import="model.ProdutoModel"%>
<%@page import="model.UsuarioModel"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= request.getAttribute("titulo") != null ? request.getAttribute("titulo") : "Produtos" %></title>

</head>
<body>
    <h1><%= request.getAttribute("titulo") != null ? request.getAttribute("titulo") : "Produtos" %></h1>
    
    <!-- Avisos severos -->
    <%
        String mensagem = (String) request.getAttribute("mensagem");
        String erro = (String) request.getAttribute("erro");
        if (mensagem != null && !mensagem.isEmpty()) {
    %>
        <p class="mensagem"><%= mensagem %></p>
    <%
        }
        if (erro != null && !erro.isEmpty()) {
    %>
        <p class="erro"><%= erro %></p>
    <%
        }
    %>
    
    <!-- p/ usuários logados -->
    <%
        UsuarioModel usuarioLogado = (UsuarioModel) session.getAttribute("usuario");
        if (usuarioLogado != null) {
    %>
        <p><a href="<%= request.getContextPath() %>/produtos/criar">Cadastrar Novo Produto</a></p>
    <%
        }
    %>
    
    
    <%
        List<ProdutoModel> produtos = (List<ProdutoModel>) request.getAttribute("produtos");
        UsuarioModel usuarioFiltro = (UsuarioModel) request.getAttribute("usuario");
        
        if (produtos != null && !produtos.isEmpty()) {
    %>
        <table>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Preço</th>
                <th>Usuário</th>
                <th>Ações</th>
            </tr>
            <%
                for (ProdutoModel produto : produtos) {
            %>
                <tr>
                    <td><%= produto.getId() %></td>
                    <td><%= produto.getNome() %></td>
                    <td class="preco">R$ <%= String.format("%.2f", produto.getPreco()).replace(".", ",") %></td>
                    <td>
                        <% if (produto.getUsuario() != null) { %>
                            <a href="<%= request.getContextPath() %>/produtos/usuario?id=<%= produto.getUsuario().getId() %>">
                                <%= produto.getUsuario().getNome() %>
                            </a>
                        <% } else { %>
                            Não associado
                        <% } %>
                    </td>
                    <td>
                        <%-- <a href="<%= request.getContextPath() %>/produtos/detalhar?id=<%= produto.getId() %>">Detalhar</a> --%>
                        <%
                            if (usuarioLogado != null && produto.getUsuario() != null && 
                                produto.getUsuario().getId() == usuarioLogado.getId()) {
                        %>
                            | <a href="<%= request.getContextPath() %>/produtos/editar?id=<%= produto.getId() %>">Editar</a>
                            | <a href="<%= request.getContextPath() %>/produtos/deletar?id=<%= produto.getId() %>" 
                                 onclick="return confirm('Tem certeza que deseja excluir este produto?')">Excluir</a>
                        <%
                            }
                        %>
                    </td>
                </tr>
            <%
                }
            %>
        </table>
    <%
        } else {
    %>
        <p>Nenhum produto encontrado.</p>
    <%
        }
    %>
    
    <br>
    <%-- <a href="<%= request.getContextPath() %>/">Voltar para home</a> --%>
    <% if (usuarioLogado != null) { %>
        <a href="<%= request.getContextPath() %>/produtos">Ver todos os produtos</a>
    <% } %>
</body>
</html>