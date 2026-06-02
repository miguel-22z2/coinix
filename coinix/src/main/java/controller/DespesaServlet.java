package controller;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import dao.DespesaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.DespesaBean;
import model.UsuarioBean;

@WebServlet("/despesas")
public class DespesaServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.html");
            return;
        }

        UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
        String acao = req.getParameter("acao");
        DespesaDAO dao = new DespesaDAO();

        try {
            if ("inserir".equals(acao)) {
                DespesaBean d = new DespesaBean();
                d.setDescricao(req.getParameter("descricao"));
                d.setValor(new BigDecimal(req.getParameter("valor")));
                d.setCategoria(req.getParameter("categoria"));
                d.setData(LocalDate.parse(req.getParameter("data")));
                dao.inserir(usuario.getId(), d);

            } else if ("editar".equals(acao)) {
                DespesaBean d = new DespesaBean();
                d.setId(Integer.parseInt(req.getParameter("id")));
                d.setDescricao(req.getParameter("descricao"));
                d.setValor(new BigDecimal(req.getParameter("valor")));
                d.setCategoria(req.getParameter("categoria"));
                d.setData(LocalDate.parse(req.getParameter("data")));
                dao.atualizar(usuario.getId(), d);

            } else if ("excluir".equals(acao)) {
                int id = Integer.parseInt(req.getParameter("id"));
                dao.excluir(usuario.getId(), id);
            }

            resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}