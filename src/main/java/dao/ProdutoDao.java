package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ProdutoModel;
import model.UsuarioModel;

public class ProdutoDao {
    
    public static boolean inserir(ProdutoModel produto) {
        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO produtos (usuario_id, nome, preco) VALUES (?, ?, ?)");
            
            statement.setInt(1, produto.getUsuarioId());
            statement.setString(2, produto.getNome());
            statement.setDouble(3, produto.getPreco());
            
            int linhasAfetadas = statement.executeUpdate();
            conn.close();
            
            return linhasAfetadas > 0;
            
        } catch (Exception e) {
            System.out.println("Erro ao inserir produto: " + e.toString());
            e.printStackTrace();//p/ mais detalhes de erros
            return false;
        }
    }
    

    public static List<ProdutoModel> buscarTodos() {
        List<ProdutoModel> lista = new ArrayList<>();
        
        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement statement = conn.prepareStatement(
                "SELECT p.*, u.nome as usuario_nome, u.email as usuario_email FROM produtos p LEFT JOIN usuarios u ON p.usuario_id = u.id ORDER BY p.id DESC"
            );
            
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                ProdutoModel produto = new ProdutoModel();
                produto.setId(rs.getInt("id"));
                produto.setUsuarioId(rs.getInt("usuario_id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco"));
                
                if (rs.getInt("usuario_id") > 0) {
                    UsuarioModel usuario = new UsuarioModel();
                    usuario.setId(rs.getInt("usuario_id"));
                    usuario.setNome(rs.getString("usuario_nome"));
                    usuario.setEmail(rs.getString("usuario_email"));
                    produto.setUsuario(usuario);
                }
                
                lista.add(produto);
            }
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Erro ao buscar produtos: " + e.toString());
            e.printStackTrace();
        }
        
        return lista;
    }
    
    
    public static List<ProdutoModel> buscarPorUsuarioId(int usuarioId) {
        List<ProdutoModel> lista = new ArrayList<>();
        
        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement statement = conn.prepareStatement(
                "SELECT p.*, u.nome as usuario_nome, u.email as usuario_email FROM produtos p LEFT JOIN usuarios u ON p.usuario_id = u.id WHERE p.usuario_id = ? ORDER BY p.id DESC"                
            );
            
            statement.setInt(1, usuarioId);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                ProdutoModel produto = new ProdutoModel();
                produto.setId(rs.getInt("id"));
                produto.setUsuarioId(rs.getInt("usuario_id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco"));
                
                if (rs.getInt("usuario_id") > 0) {
                    UsuarioModel usuario = new UsuarioModel();
                    usuario.setId(rs.getInt("usuario_id"));
                    usuario.setNome(rs.getString("usuario_nome"));
                    usuario.setEmail(rs.getString("usuario_email"));
                    produto.setUsuario(usuario);
                }
                
                lista.add(produto);
            }
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Erro ao buscar produtos por usuário: " + e.toString());
            e.printStackTrace();
        }
        
        return lista;
    }
    
    
    public static ProdutoModel buscarPorId(int id) {
        ProdutoModel produto = null;
        
        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement statement = conn.prepareStatement(
                "SELECT p.*, u.nome as usuario_nome, u.email as usuario_email FROM produtos p LEFT JOIN usuarios u ON p.usuario_id = u.id WHERE p.id = ?"
            );
            
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                produto = new ProdutoModel();
                produto.setId(rs.getInt("id"));
                produto.setUsuarioId(rs.getInt("usuario_id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco"));
                
                if (rs.getInt("usuario_id") > 0) {
                    UsuarioModel usuario = new UsuarioModel();
                    usuario.setId(rs.getInt("usuario_id"));
                    usuario.setNome(rs.getString("usuario_nome"));
                    usuario.setEmail(rs.getString("usuario_email"));
                    produto.setUsuario(usuario);
                }
            }
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Erro ao buscar produto por ID: " + e.toString());
            e.printStackTrace();
        }
        
        return produto;
    }
    
    public static boolean atualizar(ProdutoModel produto) {
        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement statement = conn.prepareStatement(
                "UPDATE produtos SET nome = ?, preco = ? WHERE id = ?"
            );
            
            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.setInt(3, produto.getId());
            
            int linhasAfetadas = statement.executeUpdate();
            conn.close();
            
            return linhasAfetadas > 0;
            
        } catch (Exception e) {
            System.out.println("Erro ao atualizar produto: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean deletar(int id) {
        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement statement = conn.prepareStatement(
                "DELETE FROM produtos WHERE id = ?"
            );
            
            statement.setInt(1, id);
            int linhasAfetadas = statement.executeUpdate();
            conn.close();
            
            return linhasAfetadas > 0;
            
        } catch (Exception e) {
            System.out.println("Erro ao deletar produto: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean verificarPropriedade(int produtoId, int usuarioId) {
        try {
            Connection conn = new Conexao().conectar();
            PreparedStatement statement = conn.prepareStatement(
                "SELECT id FROM produtos WHERE id = ? AND usuario_id = ?"
            );
            
            statement.setInt(1, produtoId);
            statement.setInt(2, usuarioId);
            ResultSet rs = statement.executeQuery();
            
            boolean pertence = rs.next();
            conn.close();
            
            return pertence;
            
        } catch (Exception e) {
            System.out.println("Erro ao verificar propriedade: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }
}