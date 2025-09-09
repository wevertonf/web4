package controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;	

import dao.UsuarioDao;
import model.UsuarioModel;
import util.SenhaUtil;

	//Post -> UsuarioController (criando um novo usuário)
	//GET -> listarUsuario -> busca todos
	//GET -> detalhar?id=num -> busca só um
	//GET -> editar?id=num -> busca um usuário p/ editar
	//Post -> atualizar?id=num -> atualiza o usuário
	//GET -> apagar?id=num -> deleta um usuário

@WebServlet(urlPatterns = {"/UsuarioController", "/listarUsuarios", "/detalhar", "/editar", "/atualizar", "/deletar"})
public class UsuarioController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UsuarioController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String acao = request.getServletPath();
        
        if (acao.equals("/detalhar")) {
            // Pegar o id da url
            String idParam = request.getParameter("id");
            
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    int id = Integer.parseInt(idParam);
                    UsuarioModel uModel = UsuarioDao.buscarPorId(id);
                    
                    if (uModel != null && uModel.getId() != 0) {
                        request.setAttribute("usuario", uModel);
                        request.getRequestDispatcher("detalhar.jsp").forward(request, response);
                        return; // Importante: return para não continuar executando
                    }
                } catch (NumberFormatException e) {
                	request.setAttribute("erro", "ID inválido!");
                }
            } else {
            	request.setAttribute("erro", "ID não informado! (DETALHAR)");
            }
            
            response.sendRedirect("listarUsuarios");// Se não encontrou ou ID inválido
            return;
        }
        
        if (acao.equals("/editar")) {
            String idParam = request.getParameter("id");
            
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    int id = Integer.parseInt(idParam);
                    UsuarioModel uModel = UsuarioDao.buscarPorId(id);
                    
                    if (uModel != null && uModel.getId() != 0) {
                        request.setAttribute("usuario", uModel);
                        request.getRequestDispatcher("editar.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                	request.setAttribute("erro", "ID do usuário inválido! (EDITAR)");
                }
            } else {
                request.setAttribute("erro", "ID não informado para edição!");
            }
            response.sendRedirect("listarUsuarios");
            return;
        }
        
        if (acao.equals("/deletar")) {
            String idParam = request.getParameter("id");
            
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    int id = Integer.parseInt(idParam);
                    
                    // Verificar se o usuário existe antes de deletar
                    UsuarioModel usuarioExistente = UsuarioDao.buscarPorId(id);
                    if (usuarioExistente != null && usuarioExistente.getId() != 0) {
                        boolean resultado = UsuarioDao.deletar(id);
                        if (resultado) {
                            request.setAttribute("mensagem", "Usuário excluído com sucesso!");
                        } else {
                            request.setAttribute("mensagem", "Erro ao excluir usuário!");
                        }
                    } else {
                        request.setAttribute("mensagem", "Usuário não encontrado!");
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("mensagem", "ID inválido!");
                } catch (Exception e) {
                    request.setAttribute("mensagem", "Erro ao excluir usuário: " + e.getMessage());
                }
            } else {
                request.setAttribute("mensagem", "ID não informado!");
            }
            
           
        }
        
        List<UsuarioModel> listaDeUsuarios = UsuarioDao.buscar();// Para todas as outras ações (incluindo /listarUsuarios)
        request.setAttribute("listaDeUsuarios", listaDeUsuarios);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("listar_usuarios.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        
        String acao = request.getServletPath();
        
        UsuarioModel novoUsuario = new UsuarioModel();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        
        boolean resultado = false;
        String mensagem = "";
        
        if (acao.equals("/atualizar")) {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    int id = Integer.parseInt(idParam);
                    novoUsuario.setId(id);
                    UsuarioModel usuarioExistente = UsuarioDao.buscarPorId(id);// Para atualização, verificar se precisa atualizar a senha
                    if (usuarioExistente != null && usuarioExistente.getId() != 0) {
                        novoUsuario.setSenha(usuarioExistente.getSenha()); // Mantém a senha existente
                    }
                    resultado = UsuarioDao.atualizar(novoUsuario);
                    mensagem = "Usuário atualizado com sucesso!";
                } catch (NumberFormatException e) {
                    mensagem = "ID inválido!";
                }
            } else {
                mensagem = "ID não informado!";
            }
        } else {// Inserção de novo usuário
            
            if (senha != null && !senha.isEmpty()) {
                String senhaCripto = SenhaUtil.hashSenha(senha);
                novoUsuario.setSenha(senhaCripto);
                resultado = UsuarioDao.inserir(novoUsuario);
                mensagem = "Usuário adicionado com sucesso!";
            } else {
                mensagem = "Senha não informada!";
            }
        }
        
        request.setAttribute("mensagem", mensagem);
        
        RequestDispatcher view = request.getRequestDispatcher("resposta.jsp");
        view.forward(request, response);
    }
}