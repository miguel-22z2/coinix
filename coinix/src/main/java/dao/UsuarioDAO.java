package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.UsuarioBean;

public class UsuarioDAO {
	public UsuarioBean autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, senha);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new UsuarioBean(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha")
                );
            }
        }
        return null;
    }

    public boolean emailExiste(String email) throws SQLException {
        String sql = "SELECT id FROM usuarios WHERE email = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public int cadastrar(String nome, String email, String senha) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, senha);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int novoId = rs.getInt(1);
                criarTabelaDespesas(conn, novoId);
                return novoId;
            }
        }
        return -1;
    }

    private void criarTabelaDespesas(Connection conn, int usuarioId) throws SQLException {
        String tabela = "despesas_usuario_" + usuarioId;
        String sql = "CREATE TABLE IF NOT EXISTS " + tabela + " (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "descricao VARCHAR(200) NOT NULL, " +
                     "valor DECIMAL(10,2) NOT NULL, " +
                     "categoria VARCHAR(50) NOT NULL, " +
                     "data DATE NOT NULL, " +
                     "criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                     ")";
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }
}
