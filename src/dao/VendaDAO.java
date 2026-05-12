package dao;

import model.Cliente;
import model.ItemVenda;
import model.Venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VendaDAO {

    public boolean salvar(Venda venda) {
        String sqlVenda = "INSERT INTO Venda (data_venda, valor_total, id_cliente) VALUES (?, ?, ?)";
        String sqlItem = "INSERT INTO ItemVenda (id_venda, id_produto, quantidade, valor_unitario) VALUES (?, ?, ?, ?)";
        String sqlVerificaEstoque = "SELECT qtde_estoque FROM Produto WHERE id = ?";
        String sqlAtualizaProduto = "UPDATE Produto SET qtde_estoque = qtde_estoque - ?, valor_ultima_venda = ? WHERE id = ?";

        Connection conn = null;

        try {
            conn = ConexaoBanco.getConnection();
            conn.setAutoCommit(false);

            int idVendaGerado = 0;
            try (PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
                stmtVenda.setDate(1, venda.getData_venda());
                stmtVenda.setDouble(2, venda.getValor_total());
                stmtVenda.setInt(3, venda.getCliente().getId());
                stmtVenda.executeUpdate();

                ResultSet rsIds = stmtVenda.getGeneratedKeys();
                if (rsIds.next()) {
                    idVendaGerado = rsIds.getInt(1);
                }
            }

            try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
                 PreparedStatement stmtVerificaEstoque = conn.prepareStatement(sqlVerificaEstoque);
                 PreparedStatement stmtAtualizaProduto = conn.prepareStatement(sqlAtualizaProduto)) {

                for (ItemVenda item : venda.getItens()) {

                    stmtVerificaEstoque.setInt(1, item.getProduto().getId());
                    ResultSet rsEstoque = stmtVerificaEstoque.executeQuery();

                    if (rsEstoque.next()) {
                        double estoqueAtual = rsEstoque.getDouble("qtde_estoque");

                        if (estoqueAtual < 1 || estoqueAtual < item.getQuantidade()) {
                            System.err.println("Operação abortada: Estoque insuficiente para o produto ID " + item.getProduto().getId() + ". (RNF003)");
                            conn.rollback();
                            return false;
                        }
                    }

                    stmtItem.setInt(1, idVendaGerado);
                    stmtItem.setInt(2, item.getProduto().getId());
                    stmtItem.setDouble(3, item.getQuantidade());
                    stmtItem.setDouble(4, item.getValor_unitario());
                    stmtItem.executeUpdate();

                    stmtAtualizaProduto.setDouble(1, item.getQuantidade());
                    stmtAtualizaProduto.setDouble(2, item.getValor_unitario());
                    stmtAtualizaProduto.setInt(3, item.getProduto().getId());
                    stmtAtualizaProduto.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Erro ao fazer rollback: " + ex.getMessage());
            }
            System.err.println("Erro ao processar venda: " + e.getMessage());
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

    public boolean alterar(Venda venda) {
        String sql = "UPDATE Venda SET data_venda = ?, valor_total = ?, id_cliente = ? WHERE id = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, venda.getData_venda());
            stmt.setDouble(2, venda.getValor_total());
            stmt.setInt(3, venda.getCliente().getId());
            stmt.setInt(4, venda.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao alterar venda: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int idVenda) {
        String sqlItens = "DELETE FROM ItemVenda WHERE id_venda = ?";
        String sqlVenda = "DELETE FROM Venda WHERE id = ?";

        Connection conn = null;
        try {
            conn = ConexaoBanco.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtItens = conn.prepareStatement(sqlItens)) {
                stmtItens.setInt(1, idVenda);
                stmtItens.executeUpdate();
            }

            try (PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda)) {
                stmtVenda.setInt(1, idVenda);
                stmtVenda.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) { }
            System.err.println("Erro ao excluir venda: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) { }
        }
    }

    public Venda pesquisar(int id) {
        String sql = "SELECT * FROM Venda WHERE id = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Venda v = new Venda();
                v.setId(rs.getInt("id"));

                String dataStr = rs.getString("data_venda");
                if (dataStr != null) {
                    try {
                        long millis = Long.parseLong(dataStr);
                        v.setData_venda(new java.sql.Date(millis));
                    } catch (NumberFormatException ex) {
                        v.setData_venda(java.sql.Date.valueOf(dataStr));
                    }
                }

                v.setValor_total(rs.getDouble("valor_total"));

                Cliente c = new Cliente();
                c.setId(rs.getInt("id_cliente"));
                v.setCliente(c);

                return v;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar venda: " + e.getMessage());
        }
        return null;
    }

    public int contarVendasPorClienteNoMes(int idCliente, String dataVendaStr) {
        try {
            String[] partes = dataVendaStr.split("-");
            int ano = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);

            java.time.YearMonth yearMonth = java.time.YearMonth.of(ano, mes);
            java.sql.Date dataInicio = java.sql.Date.valueOf(yearMonth.atDay(1));
            java.sql.Date dataFim = java.sql.Date.valueOf(yearMonth.atEndOfMonth());

            String sql = "SELECT COUNT(*) AS total FROM Venda WHERE id_cliente = ? AND data_venda >= ? AND data_venda <= ?";

            try (java.sql.Connection conn = ConexaoBanco.getConnection();
                 java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idCliente);

                stmt.setDate(2, dataInicio);
                stmt.setDate(3, dataFim);

                java.sql.ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int contagem = rs.getInt("total");
                    System.out.println("=> [Verificação RNF004] O cliente " + idCliente + " possui " + contagem + " venda(s) registrada(s) neste mês.");
                    return contagem;
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao contar vendas do mês: " + e.getMessage());
        }
        return 0;
    }
}