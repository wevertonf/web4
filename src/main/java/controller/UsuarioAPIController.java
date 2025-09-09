package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONArray;

import dao.UsuarioDao;
import model.UsuarioModel;
import util.SenhaUtil;

@WebServlet("/api/usuarios/*")
public class UsuarioAPIController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UsuarioAPIController() {
        super();
    }

    // GET /api/usuarios           → Listar todos usuários
    // GET /api/usuarios/123       → Buscar usuário por ID
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String pathInfo = request.getPathInfo();
            
            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/usuarios → Listar todos
                listarTodosUsuarios(response, out);
            } else {
                // GET /api/usuarios/123 → Buscar por ID
                buscarUsuarioPorId(pathInfo, response, out);
            }
        } catch (Exception e) {
            enviarErro(response, out, "Erro interno do servidor", 500);
        }
    }

    // POST /api/usuarios → Criar novo usuário
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Ler dados do corpo da requisição
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            
            JSONObject json = new JSONObject(sb.toString());
            
            String nome = json.optString("nome");
            String email = json.optString("email");
            String senha = json.optString("senha");
            
            if (nome == null || nome.isEmpty() || 
                email == null || email.isEmpty() || 
                senha == null || senha.isEmpty()) {
                enviarErro(response, out, "Nome, email e senha são obrigatórios", 400);
                return;
            }
            
            // Criar usuário
            UsuarioModel usuario = new UsuarioModel();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(SenhaUtil.hashSenha(senha));
            
            boolean resultado = UsuarioDao.inserir(usuario);
            
            if (resultado) {
                JSONObject resposta = new JSONObject();
                resposta.put("sucesso", true);
                resposta.put("mensagem", "Usuário criado com sucesso");
                resposta.put("id", usuario.getId());
                
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print(resposta.toString());
            } else {
                enviarErro(response, out, "Erro ao criar usuário", 500);
            }
            
        } catch (Exception e) {
            enviarErro(response, out, "Erro ao processar requisição: " + e.getMessage(), 400);
        }
    }

    // PUT /api/usuarios/123 → Atualizar usuário
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                enviarErro(response, out, "ID do usuário é obrigatório", 400);
                return;
            }
            
            int id = extrairIdDoPath(pathInfo);
            if (id <= 0) {
                enviarErro(response, out, "ID inválido", 400);
                return;
            }
            
            // Verificar se usuário existe
            UsuarioModel usuarioExistente = UsuarioDao.buscarPorId(id);
            if (usuarioExistente == null || usuarioExistente.getId() == 0) {
                enviarErro(response, out, "Usuário não encontrado", 404);
                return;
            }
            
            // Ler dados do corpo
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            
            JSONObject json = new JSONObject(sb.toString());
            
            String nome = json.optString("nome", usuarioExistente.getNome());
            String email = json.optString("email", usuarioExistente.getEmail());
            
            // Atualizar usuário (mantendo senha existente)
            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(id);
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(usuarioExistente.getSenha());
            
            boolean resultado = UsuarioDao.atualizar(usuario);
            
            if (resultado) {
                JSONObject resposta = new JSONObject();
                resposta.put("sucesso", true);
                resposta.put("mensagem", "Usuário atualizado com sucesso");
                
                out.print(resposta.toString());
            } else {
                enviarErro(response, out, "Erro ao atualizar usuário", 500);
            }
            
        } catch (NumberFormatException e) {
            enviarErro(response, out, "ID inválido", 400);
        } catch (Exception e) {
            enviarErro(response, out, "Erro ao processar requisição: " + e.getMessage(), 400);
        }
    }

    // DELETE /api/usuarios/123 → Excluir usuário
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                enviarErro(response, out, "ID do usuário é obrigatório", 400);
                return;
            }
            
            int id = extrairIdDoPath(pathInfo);
            if (id <= 0) {
                enviarErro(response, out, "ID inválido", 400);
                return;
            }
            
            // Verificar se usuário existe
            UsuarioModel usuarioExistente = UsuarioDao.buscarPorId(id);
            if (usuarioExistente == null || usuarioExistente.getId() == 0) {
                enviarErro(response, out, "Usuário não encontrado", 404);
                return;
            }
            
            boolean resultado = UsuarioDao.deletar(id);
            
            if (resultado) {
                JSONObject resposta = new JSONObject();
                resposta.put("sucesso", true);
                resposta.put("mensagem", "Usuário excluído com sucesso");
                
                out.print(resposta.toString());
            } else {
                enviarErro(response, out, "Erro ao excluir usuário", 500);
            }
            
        } catch (NumberFormatException e) {
            enviarErro(response, out, "ID inválido", 400);
        } catch (Exception e) {
            enviarErro(response, out, "Erro ao processar requisição: " + e.getMessage(), 500);
        }
    }

    // Métodos auxiliares
    private void listarTodosUsuarios(HttpServletResponse response, PrintWriter out) {
        try {
            List<UsuarioModel> usuarios = UsuarioDao.buscar();
            JSONArray jsonArray = new JSONArray();
            
            for (UsuarioModel usuario : usuarios) {
                JSONObject json = new JSONObject();
                json.put("id", usuario.getId());
                json.put("nome", usuario.getNome());
                json.put("email", usuario.getEmail());
                jsonArray.put(json);
            }
            
            JSONObject resposta = new JSONObject();
            resposta.put("usuarios", jsonArray);
            resposta.put("total", usuarios.size());
            
            out.print(resposta.toString());
        } catch (Exception e) {
            enviarErro(null, out, "Erro ao listar usuários", 500);
        }
    }

    private void buscarUsuarioPorId(String pathInfo, HttpServletResponse response, PrintWriter out) {
        try {
            int id = extrairIdDoPath(pathInfo);
            if (id <= 0) {
                enviarErro(response, out, "ID inválido", 400);
                return;
            }
            
            UsuarioModel usuario = UsuarioDao.buscarPorId(id);
            if (usuario == null || usuario.getId() == 0) {
                enviarErro(response, out, "Usuário não encontrado", 404);
                return;
            }
            
            JSONObject json = new JSONObject();
            json.put("id", usuario.getId());
            json.put("nome", usuario.getNome());
            json.put("email", usuario.getEmail());
            
            out.print(json.toString());
            
        } catch (NumberFormatException e) {
            enviarErro(response, out, "ID inválido", 400);
        } catch (Exception e) {
            enviarErro(response, out, "Erro ao buscar usuário", 500);
        }
    }

    private int extrairIdDoPath(String pathInfo) {
        try {
            String idStr = pathInfo.substring(1); // Remove a barra inicial
            return Integer.parseInt(idStr);
        } catch (Exception e) {
            return 0;
        }
    }

    private void enviarErro(HttpServletResponse response, PrintWriter out, String mensagem, int statusCode) {
        if (response != null) {
            response.setStatus(statusCode);
        }
        
        JSONObject erro = new JSONObject();
        erro.put("sucesso", false);
        erro.put("erro", mensagem);
        
        out.print(erro.toString());
    }
}