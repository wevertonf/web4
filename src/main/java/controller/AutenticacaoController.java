package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UsuarioDao;
import model.UsuarioModel;

@WebServlet(urlPatterns = {"/AutenticacaoController", "/login", "/logout"}) 
public class AutenticacaoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    public AutenticacaoController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		
	  String acao = request.getServletPath();
		if (acao.equals("/logout")) {
			HttpSession session = request.getSession();
	        session.invalidate();
	        response.sendRedirect("login.jsp");
		}
		else {
			response.sendRedirect("login.jsp");
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String acao = request.getServletPath();
	
		if (acao.equals("/login")) {				
			String email = request.getParameter("email");
			String senha = request.getParameter("senha");	
			
			UsuarioModel usuario = UsuarioDao.login(email, senha);
			

			if(usuario!=null && usuario.getId()!=0) {
		        HttpSession session = request.getSession();
		        session.setAttribute("usuario", usuario);
		        response.sendRedirect("arealogada.jsp");

			}
			else {
				  response.sendRedirect("login.jsp");
			}
		}
		
	}

}