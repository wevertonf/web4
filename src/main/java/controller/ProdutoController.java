package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ProdutoDao;
import dao.UsuarioDao;
import model.ProdutoModel;
import model.UsuarioModel;

@WebServlet(urlPatterns = {"/produtos", "/produtos/criar", "/produtos/salvar", "/produtos/detalhar", "/produtos/editar", "/produtos/atualizar", "/produtos/deletar","/produtos/usuario"})
public class ProdutoController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ProdutoController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String acao = request.getServletPath();
        HttpSession session = request.getSession();
        UsuarioModel usuarioLogado = (UsuarioModel) session.getAttribute("usuario");
        
        
        if (acao.equals("/produtos")) {
            
            List<ProdutoModel> produtos = ProdutoDao.buscarTodos();// ArrayList nas buscas por padrão!
            request.setAttribute("produtos", produtos);
            request.setAttribute("titulo", "Todos os Produtos");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/produtos/listar.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        if (acao.equals("/produtos/usuario")) {
            
            String usuarioIdParam = request.getParameter("id");
            if (usuarioIdParam != null && !usuarioIdParam.isEmpty()) {
                try {
                    int usuarioId = Integer.parseInt(usuarioIdParam);
                    UsuarioModel usuario = UsuarioDao.buscarPorId(usuarioId);
                    if (usuario != null && usuario.getId() != 0) {
                        List<ProdutoModel> produtos = ProdutoDao.buscarPorUsuarioId(usuarioId);
                        request.setAttribute("produtos", produtos);
                        request.setAttribute("usuario", usuario);
                        request.setAttribute("titulo", "Produtos de " + usuario.getNome());
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/produtos/listar.jsp");
                        dispatcher.forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("erro", "ID de usuário inválido");
                }
            }
            response.sendRedirect(request.getContextPath() + "/produtos");
            return;
        }
        
        if (acao.equals("/produtos/detalhar")) {
        
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    int id = Integer.parseInt(idParam);
                    ProdutoModel produto = ProdutoDao.buscarPorId(id);
                    if (produto != null) {
                        request.setAttribute("produto", produto);
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/produtos/detalhar.jsp");
                        dispatcher.forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("erro", "ID inválido");
                }
            }
            response.sendRedirect(request.getContextPath() + "/produtos");
            return;
        }
        
        
        if (usuarioLogado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        if (acao.equals("/produtos/criar")) {
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/produtos/criar.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        if (acao.equals("/produtos/editar")) {
            
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    int id = Integer.parseInt(idParam);
                    
                    if (ProdutoDao.verificarPropriedade(id, usuarioLogado.getId())) {
                        ProdutoModel produto = ProdutoDao.buscarPorId(id);
                        if (produto != null) {
                            request.setAttribute("produto", produto);
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/produtos/editar.jsp");
                            dispatcher.forward(request, response);
                            return;
                        }
                    } else {
                        request.setAttribute("erro", "Você não tem permissão para editar este produto");
                        response.sendRedirect(request.getContextPath() + "/produtos");
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("erro", "ID inválido");
                }
            }
            response.sendRedirect(request.getContextPath() + "/produtos");
            return;
        }
        
        if (acao.equals("/produtos/deletar")) {//VERIFICAR SE TÁ BOM MSM
            
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    int id = Integer.parseInt(idParam);
                    
                    if (ProdutoDao.verificarPropriedade(id, usuarioLogado.getId())) {
                        boolean resultado = ProdutoDao.deletar(id);
                        if (resultado) {
                            request.setAttribute("mensagem", "Produto excluído com sucesso!");
                        } else {
                            request.setAttribute("erro", "Erro ao excluir produto");
                        }
                    } else {
                        request.setAttribute("erro", "Você não tem permissão para excluir este produto");
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("erro", "ID inválido");
                }
            }
            
            
            response.sendRedirect(request.getContextPath() + "/produtos");
            return;
        }       
       
        response.sendRedirect(request.getContextPath() + "/produtos");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String acao = request.getServletPath();
        HttpSession session = request.getSession();
        UsuarioModel usuarioLogado = (UsuarioModel) session.getAttribute("usuario");
        
        
        if (usuarioLogado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        if (acao.equals("/produtos/salvar")) {
            
            String nome = request.getParameter("nome");
            String precoParam = request.getParameter("preco");
            
            if (nome != null && !nome.isEmpty() && precoParam != null && !precoParam.isEmpty()) {
                try {
                    double preco = Double.parseDouble(precoParam);
                    
                    ProdutoModel produto = new ProdutoModel();
                    produto.setUsuarioId(usuarioLogado.getId());
                    produto.setNome(nome);
                    produto.setPreco(preco);
                    
                    boolean resultado = ProdutoDao.inserir(produto);
                    if (resultado) {
                        request.setAttribute("mensagem", "Produto cadastrado com sucesso!");
                    } else {
                        request.setAttribute("erro", "Erro ao cadastrar produto");
                    }
                    
                } catch (NumberFormatException e) {
                    request.setAttribute("erro", "Preço inválido");
                }
            } else {
                request.setAttribute("erro", "Preencha todos os campos");
            }
            
            response.sendRedirect(request.getContextPath() + "/produtos");
            return;
        }
        
        if (acao.equals("/produtos/atualizar")) {
          
            String idParam = request.getParameter("id");
            String nome = request.getParameter("nome");
            String precoParam = request.getParameter("preco");
            
            if (idParam != null && !idParam.isEmpty() && 
                nome != null && !nome.isEmpty() && 
                precoParam != null && !precoParam.isEmpty()) {
                
                try {
                    int id = Integer.parseInt(idParam);
                    double preco = Double.parseDouble(precoParam);
                    
                    
                    if (ProdutoDao.verificarPropriedade(id, usuarioLogado.getId())) {
                        ProdutoModel produto = new ProdutoModel();
                        produto.setId(id);
                        produto.setNome(nome);
                        produto.setPreco(preco);
                        
                        boolean resultado = ProdutoDao.atualizar(produto);
                        if (resultado) {
                            request.setAttribute("mensagem", "Produto atualizado com sucesso!");
                        } else {
                            request.setAttribute("erro", "Erro ao atualizar produto");
                        }
                    } else {
                        request.setAttribute("erro", "Você não tem permissão para editar este produto");
                    }
                    
                } catch (NumberFormatException e) {
                    request.setAttribute("erro", "Dados inválidos");
                }
            } else {
                request.setAttribute("erro", "Preencha todos os campos");
            }
            
            response.sendRedirect(request.getContextPath() + "/produtos");
            return;
        }
        
        
        response.sendRedirect(request.getContextPath() + "/produtos");
    }
}