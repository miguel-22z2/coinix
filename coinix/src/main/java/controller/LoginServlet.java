package controller;


import java.io.IOException;

import dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UsuarioBean;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        try {
            UsuarioDAO dao = new UsuarioDAO();
            UsuarioBean usuario = dao.autenticar(email, senha);

            if (usuario != null) {
                HttpSession session = req.getSession();
                session.setAttribute("usuario", usuario);
                resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
            } else {
                resp.sendRedirect(req.getContextPath() + "/index.html?erro=1");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
