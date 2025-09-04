package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {

	private String driver = "com.mysql.cj.jdbc.Driver";

	private String url = "jdbc:mysql://localhost/java?useTimezone=true&serverTimezone=UTC";

	private String usuario = "root";

	private String senha = "root";

	public Connection conectar() {
		try {
			Connection conexao = null;
			Class.forName(driver);
			conexao = DriverManager.getConnection(url, usuario, senha);
			return conexao;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
}