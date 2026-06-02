package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import model.DespesaBean;

public class DespesaDAO {
	private String tabela(int usuarioId) {
        return "despesas_usuario_" + usuarioId;
    }

    public List<DespesaBean> listar(int usuarioId) throws SQLException {
        List<DespesaBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM " + tabela(usuarioId) + " ORDER BY data DESC";
        try (Connection conn = ConexaoDB.getConexao();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new DespesaBean(
                    rs.getInt("id"),
                    rs.getString("descricao"),
                    rs.getBigDecimal("valor"),
                    rs.getString("categoria"),
                    rs.getDate("data").toLocalDate()
                ));
            }
        }
        return lista;
    }

    public void inserir(int usuarioId, DespesaBean d) throws SQLException {
        String sql = "INSERT INTO " + tabela(usuarioId) + " (descricao, valor, categoria, data) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getDescricao());
            ps.setBigDecimal(2, d.getValor());
            ps.setString(3, d.getCategoria());
            ps.setDate(4, Date.valueOf(d.getData()));
            ps.executeUpdate();
        }
    }

    public void atualizar(int usuarioId, DespesaBean d) throws SQLException {
        String sql = "UPDATE " + tabela(usuarioId) + " SET descricao=?, valor=?, categoria=?, data=? WHERE id=?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getDescricao());
            ps.setBigDecimal(2, d.getValor());
            ps.setString(3, d.getCategoria());
            ps.setDate(4, Date.valueOf(d.getData()));
            ps.setInt(5, d.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int usuarioId, int despesaId) throws SQLException {
        String sql = "DELETE FROM " + tabela(usuarioId) + " WHERE id=?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, despesaId);
            ps.executeUpdate();
        }
    }

    public DespesaBean buscarPorId(int usuarioId, int despesaId) throws SQLException {
        String sql = "SELECT * FROM " + tabela(usuarioId) + " WHERE id=?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, despesaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new DespesaBean(
                    rs.getInt("id"),
                    rs.getString("descricao"),
                    rs.getBigDecimal("valor"),
                    rs.getString("categoria"),
                    rs.getDate("data").toLocalDate()
                );
            }
        }
        return null;
    }
}
