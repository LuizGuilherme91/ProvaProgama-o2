package controller;

import dao.FornecedorDAO;
import model.Fornecedor;

import java.util.List;

// Lembre-se de importar o Fornecedor e o FornecedorDAO com Ctrl + .
// Ex: import model.Fornecedor;
//     import dao.FornecedorDAO;

public class FornecedorController {

    private FornecedorDAO fornecedorDAO;

    public FornecedorController() {
        this.fornecedorDAO = new FornecedorDAO();
    }

    // ==========================================
    // 1. SALVAR
    // ==========================================
    public boolean salvar(String nomeFantasia, String razaoSocial, String cnpj) {
        // Validação: Nome fantasia e CNPJ não podem ser vazios
        if (nomeFantasia == null || nomeFantasia.trim().isEmpty()) {
            System.err.println("Validação falhou: O nome fantasia é obrigatório.");
            return false;
        }
        if (cnpj == null || cnpj.trim().isEmpty()) {
            System.err.println("Validação falhou: O CNPJ é obrigatório.");
            return false;
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome_fantasia(nomeFantasia);
        fornecedor.setRazao_social(razaoSocial);
        fornecedor.setCnpj(cnpj);

        return fornecedorDAO.salvar(fornecedor);
    }

    // ==========================================
    // 2. ALTERAR
    // ==========================================
    public boolean alterar(int id, String nomeFantasia, String razaoSocial, String cnpj) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID do fornecedor inválido.");
            return false;
        }
        if (nomeFantasia == null || nomeFantasia.trim().isEmpty() || cnpj == null || cnpj.trim().isEmpty()) {
            System.err.println("Validação falhou: Nome fantasia e CNPJ não podem ser vazios ao alterar.");
            return false;
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(id);
        fornecedor.setNome_fantasia(nomeFantasia);
        fornecedor.setRazao_social(razaoSocial);
        fornecedor.setCnpj(cnpj);

        return fornecedorDAO.alterar(fornecedor);
    }

    // ==========================================
    // 3. EXCLUIR
    // ==========================================
    public boolean excluir(int id) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID inválido.");
            return false;
        }
        return fornecedorDAO.excluir(id);
    }

    // ==========================================
    // 4. PESQUISAR (Pelo CNPJ, conforme o DAO)
    // ==========================================
    public Fornecedor pesquisar(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            System.err.println("Validação falhou: CNPJ inválido para pesquisa.");
            return null;
        }
        return fornecedorDAO.pesquisar(cnpj);
    }

    // ==========================================
    // 5. LISTAR TODOS
    // ==========================================
    public List<Fornecedor> listarTodos() {
        return fornecedorDAO.listarTodos();
    }
}