package controller;

import java.io.IOException;
import dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nome  = req.getParameter("nome");
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        try {
            UsuarioDAO dao = new UsuarioDAO();

            if (dao.emailExiste(email)) {
                resp.sendRedirect(req.getContextPath() + "/cadastro.html?erro=email");
                return;
            }

            int id = dao.cadastrar(nome, email, senha);
            if (id > 0) {
                resp.sendRedirect(req.getContextPath() + "/index.html?cadastro=ok");
            } else {
                resp.sendRedirect(req.getContextPath() + "/cadastro.html?erro=geral");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
