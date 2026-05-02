package dao;

import model.Cliente;
import model.ItemVenda;
import model.Venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Lembre-se de importar suas classes da Model (model.Venda, model.ItemVenda, model.Cliente, model.Produto, etc.)
// usando o atalho Ctrl + .

public class VendaDAO {

    // ==========================================
    // 1. SALVAR (CREATE) - COM REGRAS DE NEGÓCIO
    // ==========================================
    public boolean salvar(Venda venda) {
        String sqlVenda = "INSERT INTO Venda (data_venda, valor_total, id_cliente) VALUES (?, ?, ?)";
        String sqlItem = "INSERT INTO ItemVenda (id_venda, id_produto, quantidade, valor_unitario) VALUES (?, ?, ?, ?)";
        String sqlVerificaEstoque = "SELECT qtde_estoque FROM Produto WHERE id = ?";
        String sqlAtualizaProduto = "UPDATE Produto SET qtde_estoque = qtde_estoque - ?, valor_ultima_venda = ? WHERE id = ?";

        Connection conn = null;

        try {
            conn = ConexaoBanco.getConnection();
            conn.setAutoCommit(false); // Inicia a transação (trava para fazer tudo junto)

            // ------------------------------------------------------------------
            // REGRA DE NEGÓCIO: RNF004 (Máximo de 3 vendas por CPF no mês)
            // ------------------------------------------------------------------
            if (!podeRealizarVenda(conn, venda.getCliente().getId(), venda.getData_venda().toString())) {
                System.err.println("Operação abortada: model.Cliente excedeu o limite de 3 vendas no mês atual. (RNF004)");
                return false; 
            }

            // ------------------------------------------------------------------
            // PASSO 1: Salvar a model.Venda e recuperar o ID gerado
            // ------------------------------------------------------------------
            int idVendaGerado = 0;
            try (PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
                // Considerando que dataVenda está como String no formato "YYYY-MM-DD"
                stmtVenda.setDate(1, venda.getData_venda()); 
                stmtVenda.setDouble(2, venda.getValor_total());
                stmtVenda.setInt(3, venda.getCliente().getId());
                stmtVenda.executeUpdate();

                ResultSet rsIds = stmtVenda.getGeneratedKeys();
                if (rsIds.next()) {
                    idVendaGerado = rsIds.getInt(1);
                }
            }

            // ------------------------------------------------------------------
            // PASSO 2: Salvar os Itens e Atualizar Produtos
            // ------------------------------------------------------------------
            try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
                PreparedStatement stmtVerificaEstoque = conn.prepareStatement(sqlVerificaEstoque);
                PreparedStatement stmtAtualizaProduto = conn.prepareStatement(sqlAtualizaProduto)) {

                for (ItemVenda item : venda.getItens()) {
                    
                    // Verifica estoque atual do produto
                    stmtVerificaEstoque.setInt(1, item.getProduto().getId());
                    ResultSet rsEstoque = stmtVerificaEstoque.executeQuery();
                    
                    if (rsEstoque.next()) {
                        double estoqueAtual = rsEstoque.getDouble("qtde_estoque");
                        
                        // REGRA DE NEGÓCIO: RNF003 (Abortar se estoque inferior a 1 ou insuficiente)
                        if (estoqueAtual < 1 || estoqueAtual < item.getQuantidade()) {
                            System.err.println("Operação abortada: Estoque insuficiente para o produto ID " + item.getProduto().getId() + ". (RNF003)");
                            conn.rollback(); // Cancela tudo
                            return false;
                        }
                    }

                    // Insere o model.ItemVenda
                    stmtItem.setInt(1, idVendaGerado);
                    stmtItem.setInt(2, item.getProduto().getId());
                    stmtItem.setDouble(3, item.getQuantidade());
                    stmtItem.setDouble(4, item.getValor_unitario());
                    stmtItem.executeUpdate();

                    // REGRA DE NEGÓCIO: RNF001 e RNF005 (Subtrai estoque e atualiza último valor)
                    stmtAtualizaProduto.setDouble(1, item.getQuantidade());
                    stmtAtualizaProduto.setDouble(2, item.getValor_unitario());
                    stmtAtualizaProduto.setInt(3, item.getProduto().getId());
                    stmtAtualizaProduto.executeUpdate();
                }
            }

            conn.commit(); // Confirma todas as operações no banco!
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Se deu erro de SQL, desfaz tudo
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

    // ==========================================
    // MÉTODO AUXILIAR PARA RNF004
    // ==========================================
    private boolean podeRealizarVenda(Connection conn, int idCliente, String dataVenda) throws SQLException {
        // Pega o Ano e Mês da venda (Ex: extrai "2026-05" de "2026-05-01")
        String anoMesVenda = dataVenda.substring(0, 7); 

        String sql = "SELECT COUNT(*) AS total_vendas FROM Venda WHERE id_cliente = ? AND strftime('%Y-%m', data_venda) = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.setString(2, anoMesVenda);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int totalVendas = rs.getInt("total_vendas");
                return totalVendas < 3; // Retorna true se fez menos de 3 vendas
            }
        }
        return true;
    }

    // ==========================================
    // 2. ALTERAR (UPDATE)
    // ==========================================
    public boolean alterar(Venda venda) {
        // Alterar vendas completas é bem complexo (envolve repor estoque antigo e retirar o novo).
        // Aqui fazemos o básico da capa da model.Venda, como o diagrama sugere de forma simples.
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

    // ==========================================
    // 3. EXCLUIR (DELETE)
    // ==========================================
    public boolean excluir(int idVenda) {
        String sqlItens = "DELETE FROM ItemVenda WHERE id_venda = ?";
        String sqlVenda = "DELETE FROM Venda WHERE id = ?";
        
        Connection conn = null;
        try {
            conn = ConexaoBanco.getConnection();
            conn.setAutoCommit(false); // Transação pois exclui de 2 tabelas

            // Primeiro deleta os itens (por causa da chave estrangeira)
            try (PreparedStatement stmtItens = conn.prepareStatement(sqlItens)) {
                stmtItens.setInt(1, idVenda);
                stmtItens.executeUpdate();
            }

            // Depois deleta a venda
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

    // ==========================================
    // 4. PESQUISAR (READ)
    // ==========================================
    public Venda pesquisar(int id) {
        String sql = "SELECT * FROM Venda WHERE id = ?";
        
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Venda v = new Venda();
                v.setId(rs.getInt("id"));
                v.setData_venda(rs.getDate("data_venda"));
                v.setValor_total(rs.getDouble("valor_total"));
                
                // Para simplificar, setamos apenas o ID do cliente. 
                // Idealmente, você chamaria dao.ClienteDAO.pesquisar() aqui.
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
}