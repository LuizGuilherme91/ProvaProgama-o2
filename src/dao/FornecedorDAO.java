package dao;

import model.Fornecedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {

    public boolean salvar(Fornecedor fornecedor) {
        String sql = "INSERT INTO Fornecedor (nome_fantasia, razao_social, cnpj) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getNome_fantasia());
            stmt.setString(2, fornecedor.getRazao_social());
            stmt.setString(3, fornecedor.getCnpj());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0; 

        } catch (SQLException e) {
            System.err.println("Erro ao salvar fornecedor: " + e.getMessage());
            return false;
        }
    }

    public boolean alterar(Fornecedor fornecedor) {
        String sql = "UPDATE Fornecedor SET nome_fantasia = ?, razao_social = ? WHERE cnpj = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getNome_fantasia());
            stmt.setString(2, fornecedor.getRazao_social());

            stmt.setString(3, fornecedor.getCnpj());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao alterar fornecedor: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int idFornecedor) {
        String sql = "DELETE FROM Fornecedor WHERE id = ?";
        
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFornecedor);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir fornecedor. Pode haver compras atreladas a ele: " + e.getMessage());
            return false;
        }
    }

    public Fornecedor pesquisar(String cnpj) {
        String sql = "SELECT * FROM Fornecedor WHERE cnpj = ?";
        System.out.println("valor cnpj: " + cnpj);
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cnpj);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Fornecedor f = new Fornecedor();
                f.setId(rs.getInt("id"));
                f.setNome_fantasia(rs.getString("nome_fantasia"));
                f.setRazao_social(rs.getString("razao_social"));
                f.setCnpj(rs.getString("cnpj"));
                
                return f;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar fornecedor por CNPJ: " + e.getMessage());
        }
        
        return null;
    }

    public List<Fornecedor> listarTodos() {
        List<Fornecedor> listaFornecedores = new ArrayList<>();
        String sql = "SELECT * FROM Fornecedor";
        
        try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Fornecedor f = new Fornecedor();
                f.setId(rs.getInt("id"));
                f.setNome_fantasia(rs.getString("nome_fantasia"));
                f.setRazao_social(rs.getString("razao_social"));
                f.setCnpj(rs.getString("cnpj"));
                
                listaFornecedores.add(f);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os fornecedores: " + e.getMessage());
        }
        
        return listaFornecedores;
    }
}