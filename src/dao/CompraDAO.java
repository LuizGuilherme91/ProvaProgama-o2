package dao;

import model.Compra;
import model.Fornecedor;
import model.ItemCompra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Lembre-se de importar as suas entidades (model.Compra, model.ItemCompra, model.Fornecedor, model.Produto, etc)
// usando o atalho Ctrl + .

public class CompraDAO {

    // ==========================================
    // 1. SALVAR (CREATE) - COM REGRAS DE NEGÓCIO
    // ==========================================
    public boolean salvar(Compra compra) {
        String sqlCompra = "INSERT INTO Compra (data_compra, valor_total, id_fornecedor) VALUES (?, ?, ?)";
        String sqlItem = "INSERT INTO ItemCompra (id_compra, id_produto, quantidade, valor_unitario) VALUES (?, ?, ?, ?)";
        String sqlBuscaProduto = "SELECT qtde_estoque, preco_medio FROM Produto WHERE id = ?";
        String sqlAtualizaProduto = "UPDATE Produto SET qtde_estoque = ?, valor_ultima_compra = ?, preco_medio = ? WHERE id = ?";

        Connection conn = null;

        try {
            conn = ConexaoBanco.getConnection();
            conn.setAutoCommit(false); // Inicia a transação (tudo junto ou nada)

            // ------------------------------------------------------------------
            // PASSO 1: Salvar a model.Compra e recuperar o ID gerado
            // ------------------------------------------------------------------
            int idCompraGerado = 0;
            try (PreparedStatement stmtCompra = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS)) {
                stmtCompra.setString(1, compra.getData_compra());
                stmtCompra.setDouble(2, compra.getValor_total());
                stmtCompra.setInt(3, compra.getFornecedor().getId());
                stmtCompra.executeUpdate();

                ResultSet rsIds = stmtCompra.getGeneratedKeys();
                if (rsIds.next()) {
                    idCompraGerado = rsIds.getInt(1);
                }
            }

            // ------------------------------------------------------------------
            // PASSO 2: Salvar os Itens e Atualizar Estoque/Preços
            // ------------------------------------------------------------------
            try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
                 PreparedStatement stmtBuscaProduto = conn.prepareStatement(sqlBuscaProduto);
                 PreparedStatement stmtAtualizaProduto = conn.prepareStatement(sqlAtualizaProduto)) {

                for (ItemCompra item : compra.getItens()) {

                    // Insere o model.ItemCompra no banco
                    stmtItem.setInt(1, idCompraGerado);
                    stmtItem.setInt(2, item.getProduto().getId());
                    stmtItem.setDouble(3, item.getQuantidade());
                    stmtItem.setDouble(4, item.getValor_unitario());
                    stmtItem.executeUpdate();

                    // Busca as informações atuais do produto
                    stmtBuscaProduto.setInt(1, item.getProduto().getId());
                    ResultSet rsProduto = stmtBuscaProduto.executeQuery();

                    if (rsProduto.next()) {
                        double estoqueAtual = rsProduto.getDouble("qtde_estoque");
                        double precoMedioAtual = rsProduto.getDouble("preco_medio");

                        // REGRA DE NEGÓCIO: RNF002 (Somar estoque)
                        double novoEstoque = estoqueAtual + item.getQuantidade();

                        // REGRA DE NEGÓCIO: RNF007 (Calcular novo preço médio ponderado)
                        double novoPrecoMedio;
                        if (estoqueAtual <= 0) {
                            novoPrecoMedio = item.getValor_unitario(); // Se o estoque estava zerado, o médio é o novo
                        } else {
                            // Cálculo da Média Ponderada
                            double valorTotalAntigo = estoqueAtual * precoMedioAtual;
                            double valorTotalNovo = item.getQuantidade() * item.getValor_unitario();
                            novoPrecoMedio = (valorTotalAntigo + valorTotalNovo) / novoEstoque;
                        }

                        // REGRA DE NEGÓCIO: Atualiza RNF002 (Estoque), RNF006 (Valor Última model.Compra) e RNF007 (Preço Médio)
                        stmtAtualizaProduto.setDouble(1, novoEstoque);
                        stmtAtualizaProduto.setDouble(2, item.getValor_unitario());
                        stmtAtualizaProduto.setDouble(3, novoPrecoMedio);
                        stmtAtualizaProduto.setInt(4, item.getProduto().getId());
                        stmtAtualizaProduto.executeUpdate();
                    }
                }
            }

            conn.commit(); // Se tudo deu certo, salva no banco!
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Desfaz tudo se der erro
            } catch (SQLException ex) {
                System.err.println("Erro ao fazer rollback: " + ex.getMessage());
            }
            System.err.println("Erro ao processar compra: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    // ==========================================
    // 2. ALTERAR (UPDATE)
    // ==========================================
    public boolean alterar(Compra compra) {
        String sql = "UPDATE Compra SET data_compra = ?, valor_total = ?, id_fornecedor = ? WHERE id = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, compra.getData_compra());
            stmt.setDouble(2, compra.getValor_total());
            stmt.setInt(3, compra.getFornecedor().getId());
            stmt.setInt(4, compra.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao alterar compra: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // 3. EXCLUIR (DELETE)
    // ==========================================
    // ==========================================
    // 3. EXCLUIR (DELETE)
    // ==========================================
    public boolean excluir(int idCompra) {
        // Precisamos deletar os filhos (itens) primeiro, e depois o pai (compra)
        String sqlItens = "DELETE FROM ItemCompra WHERE id_compra = ?";
        String sqlCompra = "DELETE FROM Compra WHERE id = ?";

        java.sql.Connection conn = null;
        try {
            conn = dao.ConexaoBanco.getConnection();
            conn.setAutoCommit(false); // Inicia uma transação para garantir que as duas tabelas sejam afetadas juntas

            // 1. Deleta os itens da compra
            try (java.sql.PreparedStatement stmtItens = conn.prepareStatement(sqlItens)) {
                stmtItens.setInt(1, idCompra);
                stmtItens.executeUpdate();
            }

            // 2. Deleta a capa da compra
            try (java.sql.PreparedStatement stmtCompra = conn.prepareStatement(sqlCompra)) {
                stmtCompra.setInt(1, idCompra);
                stmtCompra.executeUpdate();
            }

            conn.commit(); // Confirma a exclusão nas duas tabelas
            return true;

        } catch (java.sql.SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Se algo der errado, cancela a exclusão
            } catch (java.sql.SQLException ex) {
                System.err.println("Erro no rollback: " + ex.getMessage());
            }
            System.err.println("Erro ao excluir compra: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (java.sql.SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    // ==========================================
    // 4. PESQUISAR (READ)
    // ==========================================
    public Compra pesquisar(int id) {
        String sql = "SELECT * FROM Compra WHERE id = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Compra c = new Compra();
                c.setId(rs.getInt("id"));
                c.setData_compra(rs.getString("data_compra"));
                c.setValor_total(rs.getDouble("valor_total"));

                Fornecedor f = new Fornecedor();
                f.setId(rs.getInt("id_fornecedor"));
                c.setFornecedor(f);

                return c;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar compra: " + e.getMessage());
        }
        return null;
    }
}