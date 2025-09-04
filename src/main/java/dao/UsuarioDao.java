package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import model.UsuarioModel;
import util.SenhaUtil;

public class UsuarioDao {

	public static boolean inserir(UsuarioModel usuario) {
		try {

			Connection conn = new Conexao().conectar();

			PreparedStatement statement = conn.prepareStatement("INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)");

			 statement.setString(1, usuario.getNome());
		     statement.setString(2, usuario.getEmail());
		     statement.setString(3, usuario.getSenha());
			
			statement.execute();

			conn.close();

			return true;
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}
	
	public static UsuarioModel login(String email, String senha) {
	    try {
	        Connection conn = new Conexao().conectar();
	        
	        PreparedStatement statement = conn.prepareStatement(
	            "SELECT id, nome, email, senha FROM usuarios WHERE email = ?"
	        );
	        statement.setString(1, email);
	        
	        ResultSet resultSet = statement.executeQuery();
	        
	        if (resultSet.next()) {
	            String senhaHash = resultSet.getString("senha");
	            if (BCrypt.checkpw(senha, senhaHash)) {
	                UsuarioModel usuario = new UsuarioModel();
	                usuario.setId(resultSet.getInt("id"));
	                usuario.setNome(resultSet.getString("nome"));
	                usuario.setEmail(resultSet.getString("email"));
	                conn.close();
	                return usuario;
	            }
	        }
	        conn.close();
	        return null; // Login inválido
	        
	    } catch (Exception e) {
	        System.out.println("Erro no login: " + e.toString());
	        e.printStackTrace(); // Para ver o erro completo
	        return null;
	    }
	}
	
	@SuppressWarnings("finally")
	public static List<UsuarioModel> buscar() {
		List<UsuarioModel> lista = new ArrayList<UsuarioModel>();

		try {
			Connection conn = new Conexao().conectar();

			PreparedStatement statement = conn.prepareStatement("select * from usuarios");

			ResultSet retorno = statement.executeQuery();

			while (retorno.next()) {
				UsuarioModel p = new UsuarioModel();

				p.setId(retorno.getInt("id"));
				p.setNome(retorno.getString("nome"));
				p.setEmail(retorno.getString("email"));

				lista.add(p);
			}
			conn.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			return lista;
		}
	}
	
	public static UsuarioModel buscarPorId(int id) {
		UsuarioModel uModel = new UsuarioModel();
		
		try {
			
			Connection conn = new Conexao().conectar();

			PreparedStatement statement = conn.prepareStatement("select * from usuarios where id=?");

			statement.setInt(1, id);
			
			//statement.executeQuery();// p/ uso do select
			
			ResultSet resultado = statement.executeQuery();
			
			if(resultado.next()) {
				uModel.setId(resultado.getInt("id"));
				uModel.setNome(resultado.getString("nome"));
				uModel.setEmail(resultado.getString("email"));
			}
			
			conn.close();
				
			return uModel;
		} catch (Exception e) {
			System.out.println("Erro " + e.toString());
			return uModel;
		}
		
	}

	public static boolean atualizar(UsuarioModel usuario) {
		// TODO Auto-generated method stub
		
		try {
			Connection conn = new Conexao().conectar();

			PreparedStatement statement = conn.prepareStatement("update usuarios set nome=? where id=?");
			
			statement.setString(1, usuario.getNome());
			statement.setInt(2, usuario.getId());
			
			int qtdLinhas = statement.executeUpdate();//verificar qnts limhas alteradas na table
			
			conn.close();
			return true;
			
			
		} catch (Exception e) {
			
			return false;
			
		}
	}
	
	public static boolean deletar(int id) {
	    try {
	        Connection conn = new Conexao().conectar();
	        PreparedStatement statement = conn.prepareStatement("DELETE FROM usuarios WHERE id = ?");
	        statement.setInt(1, id);
	        
	        int linhasAfetadas = statement.executeUpdate();
	        conn.close();
	        
	        return linhasAfetadas > 0; // se deletou pelo menos uma linha
	        
	    } catch (Exception e) {
	        System.out.println("Erro ao deletar usuário: " + e.toString());
	        e.printStackTrace();
	        return false;
	    }
	}
}