import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CategoriaDAO {

    // ==========================================
    // 1. SALVAR (CREATE)
    // ==========================================
    public boolean salvar(Categoria categoria) {
        String sql = "INSERT INTO Categoria (nome) VALUES (?)";
        
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Categoria só precisa do nome para ser inserida, o ID é automático
            stmt.setString(1, categoria.getNome());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0; 

        } catch (SQLException e) {
            System.err.println("Erro ao salvar categoria: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // 2. ALTERAR (UPDATE)
    // ==========================================
    public boolean alterar(Categoria categoria) {
        String sql = "UPDATE Categoria SET nome = ? WHERE id = ?";
        
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getId());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao alterar categoria: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // 3. EXCLUIR (DELETE)
    // ==========================================
    public boolean excluir(int idCategoria) {
        String sql = "DELETE FROM Categoria WHERE id = ?";
        
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCategoria);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir. Verifique se existem produtos atrelados a esta categoria: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // 4. PESQUISAR (READ)
    // ==========================================
    public Categoria pesquisar(int id) {
        // Como a categoria não tem CPF ou CNPJ, a pesquisa mais lógica é pelo ID
        String sql = "SELECT * FROM Categoria WHERE id = ?";
        
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                
                return c;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar categoria por ID: " + e.getMessage());
        }
        
        return null;
    }

    // ==========================================
    // MÉTODO EXTRA: LISTAR TODAS
    // ==========================================
    public List<Categoria> listarTodas() {
        List<Categoria> listaCategorias = new ArrayList<>();
        String sql = "SELECT * FROM Categoria";
        
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                
                listaCategorias.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar todas as categorias: " + e.getMessage());
        }
        
        return listaCategorias;
    }
}