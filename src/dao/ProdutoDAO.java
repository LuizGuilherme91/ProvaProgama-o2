package dao;

import model.Categoria;
import model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Lembre-se de importar o model.Produto e a model.Categoria do seu pacote model!
// Ex: import model.model.Produto;
//     import model.model.Categoria;

public class ProdutoDAO {

    // ==========================================
    // 1. SALVAR (CREATE)
    // ==========================================
    public boolean salvar(Produto produto) {
        String sql = "INSERT INTO Produto (nome, preco_medio, qtde_estoque, valor_ultima_compra, valor_ultima_venda, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco_medio());
            stmt.setDouble(3, produto.getQtde_estoque());
            stmt.setDouble(4, produto.getValor_ultima_compra());
            stmt.setDouble(5, produto.getValor_ultima_venda());
            stmt.setInt(6, produto.getCategoria().getId()); // Pega o ID do objeto model.Categoria

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar produto: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // 2. ALTERAR (UPDATE)
    // ==========================================
    public boolean alterar(Produto produto) {
        String sql = "UPDATE Produto SET nome = ?, preco_medio = ?, qtde_estoque = ?, valor_ultima_compra = ?, valor_ultima_venda = ?, id_categoria = ? WHERE id = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco_medio());
            stmt.setDouble(3, produto.getQtde_estoque());
            stmt.setDouble(4, produto.getValor_ultima_compra());
            stmt.setDouble(5, produto.getValor_ultima_venda());
            stmt.setInt(6, produto.getCategoria().getId());
            stmt.setInt(7, produto.getId());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao alterar produto: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // 3. EXCLUIR (DELETE)
    // ==========================================
    // ==========================================
    // 3. EXCLUIR (DELETE)
    // ==========================================
    public boolean excluir(int id) {
        String sql = "DELETE FROM Produto WHERE id = ?";

        try (java.sql.Connection conn = dao.ConexaoBanco.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();

            // Retorna true se realmente apagou alguma linha
            return linhasAfetadas > 0;

        } catch (java.sql.SQLException e) {
            // Se cair aqui, provavelmente é o banco bloqueando por causa de histórico em Vendas/Compras
            System.err.println("Erro ao excluir produto. Ele pode estar vinculado a alguma transação: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // 4. PESQUISAR (READ)
    // ==========================================
    public Produto pesquisar(int id) {
        String sql = "SELECT * FROM Produto WHERE id = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setPreco_medio(rs.getDouble("preco_medio"));
                p.setQtde_estoque(rs.getDouble("qtde_estoque"));
                p.setValor_ultima_compra(rs.getDouble("valor_ultima_compra"));
                p.setValor_ultima_venda(rs.getDouble("valor_ultima_venda"));

                // Preenche a categoria (basicamente, setamos apenas o ID aqui para ser prático)
                Categoria c = new Categoria();
                c.setId(rs.getInt("id_categoria"));
                p.setCategoria(c);

                return p;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar produto: " + e.getMessage());
        }

        return null;
    }

    // ==========================================
    // MÉTODO EXTRA: LISTAR TODOS
    // ==========================================
    public List<Produto> listarTodos() {
        List<Produto> listaProdutos = new ArrayList<>();
        String sql = "SELECT * FROM Produto";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setPreco_medio(rs.getDouble("preco_medio"));
                p.setQtde_estoque(rs.getDouble("qtde_estoque"));
                p.setValor_ultima_compra(rs.getDouble("valor_ultima_compra"));
                p.setValor_ultima_venda(rs.getDouble("valor_ultima_venda"));

                Categoria c = new Categoria();
                c.setId(rs.getInt("id_categoria"));
                p.setCategoria(c);

                listaProdutos.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os produtos: " + e.getMessage());
        }

        return listaProdutos;
    }
}