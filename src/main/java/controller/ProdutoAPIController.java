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

import dao.ProdutoDao;
import dao.UsuarioDao;
import model.ProdutoModel;
import model.UsuarioModel;

@WebServlet("/api/produtos/*")
public class ProdutoAPIController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ProdutoAPIController() {
        super();
    }

    // GET /api/produtos                    → Listar todos produtos
    // GET /api/produtos/1234                → Buscar produto por ID
    // GET /api/produtos/usuario/1234        → Listar produtos de usuário
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String pathInfo = request.getPathInfo();
            
            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/produtos → Listar todos
                listarTodosProdutos(response, out);
            } else if (pathInfo.startsWith("/usuario/")) {
                // GET /api/produtos/usuario/123 → Listar por usuário
                listarProdutosPorUsuario(pathInfo, response, out);
            } else {
                // GET /api/produtos/123 → Buscar por ID
                buscarProdutoPorId(pathInfo, response, out);
            }
        } catch (Exception e) {
            enviarErro(response, out, "Erro interno do servidor: " + e.getMessage(), 500);
        }
    }

    // POST /api/produtos → Criar novo produto
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Ler dados do corpo
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            
            JSONObject json = new JSONObject(sb.toString());
            
            int usuarioId = json.optInt("usuario_id");
            String nome = json.optString("nome");
            double preco = json.optDouble("preco");
            
            if (usuarioId <= 0 || nome == null || nome.isEmpty() || preco <= 0) {
                enviarErro(response, out, "usuario_id, nome e preco são obrigatórios", 400);
                return;
            }
            
            // Verificar se usuário existe
            UsuarioModel usuario = UsuarioDao.buscarPorId(usuarioId);
            if (usuario == null || usuario.getId() == 0) {
                enviarErro(response, out, "Usuário não encontrado", 404);
                return;
            }
            
            // Criar produto
            ProdutoModel produto = new ProdutoModel();
            produto.setUsuarioId(usuarioId);
            produto.setNome(nome);
            produto.setPreco(preco);
            
            boolean resultado = ProdutoDao.inserir(produto);
            
            if (resultado) {
                JSONObject resposta = new JSONObject();
                resposta.put("sucesso", true);
                resposta.put("mensagem", "Produto criado com sucesso");
                resposta.put("id", produto.getId());
                
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print(resposta.toString());
            } else {
                enviarErro(response, out, "Erro ao criar produto", 500);
            }
            
        } catch (Exception e) {
            enviarErro(response, out, "Erro ao processar requisição: " + e.getMessage(), 400);
        }
    }

    // PUT /api/produtos/123 → Atualizar produto
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                enviarErro(response, out, "ID do produto é obrigatório", 400);
                return;
            }
            
            int id = extrairIdDoPath(pathInfo);
            if (id <= 0) {
                enviarErro(response, out, "ID inválido", 400);
                return;
            }
            
            // Verificar se produto existe
            ProdutoModel produtoExistente = ProdutoDao.buscarPorId(id);
            if (produtoExistente == null) {
                enviarErro(response, out, "Produto não encontrado", 404);
                return;
            }
            
            // Ler dados do corpo
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            
            JSONObject json = new JSONObject(sb.toString());
            
            String nome = json.optString("nome", produtoExistente.getNome());
            double preco = json.optDouble("preco", produtoExistente.getPreco());
            
            // Atualizar produto
            ProdutoModel produto = new ProdutoModel();
            produto.setId(id);
            produto.setUsuarioId(produtoExistente.getUsuarioId());
            produto.setNome(nome);
            produto.setPreco(preco);
            
            boolean resultado = ProdutoDao.atualizar(produto);
            
            if (resultado) {
                JSONObject resposta = new JSONObject();
                resposta.put("sucesso", true);
                resposta.put("mensagem", "Produto atualizado com sucesso");
                
                out.print(resposta.toString());
            } else {
                enviarErro(response, out, "Erro ao atualizar produto", 500);
            }
            
        } catch (NumberFormatException e) {
            enviarErro(response, out, "ID inválido", 400);
        } catch (Exception e) {
            enviarErro(response, out, "Erro ao processar requisição: " + e.getMessage(), 400);
        }
    }

    // DELETE /api/produtos/123 → Excluir produto
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                enviarErro(response, out, "ID do produto é obrigatório", 400);
                return;
            }
            
            int id = extrairIdDoPath(pathInfo);
            if (id <= 0) {
                enviarErro(response, out, "ID inválido", 400);
                return;
            }
            
            // Verificar se produto existe
            ProdutoModel produtoExistente = ProdutoDao.buscarPorId(id);
            if (produtoExistente == null) {
                enviarErro(response, out, "Produto não encontrado", 404);
                return;
            }
            
            boolean resultado = ProdutoDao.deletar(id);
            
            if (resultado) {
                JSONObject resposta = new JSONObject();
                resposta.put("sucesso", true);
                resposta.put("mensagem", "Produto excluído com sucesso");
                
                out.print(resposta.toString());
            } else {
                enviarErro(response, out, "Erro ao excluir produto", 500);
            }
            
        } catch (NumberFormatException e) {
            enviarErro(response, out, "ID inválido", 400);
        } catch (Exception e) {
            enviarErro(response, out, "Erro ao processar requisição: " + e.getMessage(), 500);
        }
    }

    // Métodos auxiliares
    private void listarTodosProdutos(HttpServletResponse response, PrintWriter out) {
        try {
            List<ProdutoModel> produtos = ProdutoDao.buscarTodos();
            JSONArray jsonArray = new JSONArray();
            
            for (ProdutoModel produto : produtos) {
                JSONObject json = criarJsonProduto(produto);
                jsonArray.put(json);
            }
            
            JSONObject resposta = new JSONObject();
            resposta.put("produtos", jsonArray);
            resposta.put("total", produtos.size());
            
            out.print(resposta.toString());
        } catch (Exception e) {
            enviarErro(response, out, "Erro ao listar produtos: " + e.getMessage(), 500);
        }
    }

    private void listarProdutosPorUsuario(String pathInfo, HttpServletResponse response, PrintWriter out) {
        try {
            String[] partes = pathInfo.split("/");
            if (partes.length < 3) {
                enviarErro(response, out, "ID do usuário é obrigatório", 400);
                return;
            }
            
            int usuarioId = Integer.parseInt(partes[2]);
            
            // Verificar se usuário existe
            UsuarioModel usuario = UsuarioDao.buscarPorId(usuarioId);
            if (usuario == null || usuario.getId() == 0) {
                enviarErro(response, out, "Usuário não encontrado", 404);
                return;
            }
            
            List<ProdutoModel> produtos = ProdutoDao.buscarPorUsuarioId(usuarioId);
            JSONArray jsonArray = new JSONArray();
            
            for (ProdutoModel produto : produtos) {
                JSONObject json = criarJsonProduto(produto);
                jsonArray.put(json);
            }
            
            JSONObject resposta = new JSONObject();
            resposta.put("produtos", jsonArray);
            resposta.put("usuario_id", usuarioId);
            resposta.put("usuario_nome", usuario.getNome());
            resposta.put("total", produtos.size());
            
            out.print(resposta.toString());
            
        } catch (NumberFormatException e) {
            enviarErro(response, out, "ID de usuário inválido", 400);
        } catch (Exception e) {
            enviarErro(response, out, "Erro ao listar produtos do usuário: " + e.getMessage(), 500);
        }
    }

    private void buscarProdutoPorId(String pathInfo, HttpServletResponse response, PrintWriter out) {
        try {
            int id = extrairIdDoPath(pathInfo);
            if (id <= 0) {
                enviarErro(response, out, "ID inválido", 400);
                return;
            }
            
            ProdutoModel produto = ProdutoDao.buscarPorId(id);
            if (produto == null) {
                enviarErro(response, out, "Produto não encontrado", 404);
                return;
            }
            
            JSONObject json = criarJsonProduto(produto);
            out.print(json.toString());
            
        } catch (NumberFormatException e) {
            enviarErro(response, out, "ID inválido", 400);
        } catch (Exception e) {
            enviarErro(response, out, "Erro ao buscar produto: " + e.getMessage(), 500);
        }
    }

    private JSONObject criarJsonProduto(ProdutoModel produto) {
        JSONObject json = new JSONObject();
        json.put("id", produto.getId());
        json.put("nome", produto.getNome());
        json.put("preco", produto.getPreco());
        json.put("usuario_id", produto.getUsuarioId());
        
        if (produto.getUsuario() != null) {
            JSONObject usuarioJson = new JSONObject();
            usuarioJson.put("id", produto.getUsuario().getId());
            usuarioJson.put("nome", produto.getUsuario().getNome());
            usuarioJson.put("email", produto.getUsuario().getEmail());
            json.put("usuario", usuarioJson);
        }
        
        return json;
    }

    private int extrairIdDoPath(String pathInfo) {
        try {
            String idStr = pathInfo.substring(1); // Remove a barra inicial
            // Se tiver mais barras, pega só o primeiro número
            if (idStr.contains("/")) {
                idStr = idStr.split("/")[0];
            }
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