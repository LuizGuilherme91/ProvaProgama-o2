package dao;

import model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // ==========================================
    // 1. SALVAR (CREATE)
    // ==========================================
    public boolean salvar(Cliente cliente) {
        String sql = "INSERT INTO Cliente (nome, cpf, rg, endereco, telefone) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getRg());
            stmt.setString(4, cliente.getEndereco());
            stmt.setString(5, cliente.getTelefone());

            int linhasAfetadas = stmt.executeUpdate();
            // Retorna true se inseriu pelo menos uma linha
            return linhasAfetadas > 0; 

        } catch (SQLException e) {
            System.err.println("Erro ao salvar cliente: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // 2. ALTERAR (UPDATE)
    // ==========================================
    public boolean alterar(Cliente cliente) {
        // O UPDATE usa o 'id' para saber exatamente qual cliente modificar
        String sql = "UPDATE Cliente SET nome = ?, cpf = ?, rg = ?, endereco = ?, telefone = ? WHERE id = ?";
        
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getRg());
            stmt.setString(4, cliente.getEndereco());
            stmt.setString(5, cliente.getTelefone());
            stmt.setInt(6, cliente.getId());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao alterar cliente: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // 3. EXCLUIR (DELETE)
    // ==========================================
    public boolean excluir(int idCliente) {
        String sql = "DELETE FROM Cliente WHERE id = ?";
        
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir cliente. Pode haver dependências atreladas a ele: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // 4. PESQUISAR (READ)
    // ==========================================
    public Cliente pesquisar(String cpf) {
        String sql = "SELECT * FROM Cliente WHERE cpf = ?";
        
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            // Se encontrou no banco, monta o objeto e devolve
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setRg(rs.getString("rg"));
                cliente.setEndereco(rs.getString("endereco"));
                cliente.setTelefone(rs.getString("telefone"));
                
                return cliente;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar cliente por CPF: " + e.getMessage());
        }
        
        return null; // Retorna nulo se o CPF não for encontrado
    }

    // ==========================================
    // MÉTODO EXTRA: LISTAR TODOS (Ajuda muito nos testes)
    // ==========================================
    public List<Cliente> listarTodos() {
        List<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                c.setRg(rs.getString("rg"));
                c.setEndereco(rs.getString("endereco"));
                c.setTelefone(rs.getString("telefone"));
                
                listaClientes.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os clientes: " + e.getMessage());
        }
        
        return listaClientes;
    }
}