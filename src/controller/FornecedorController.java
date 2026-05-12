package controller;

import dao.FornecedorDAO;
import model.Fornecedor;

import java.util.List;

public class FornecedorController {

    private FornecedorDAO fornecedorDAO;

    public FornecedorController() {
        this.fornecedorDAO = new FornecedorDAO();
    }

    public boolean salvar(String nomeFantasia, String razaoSocial, String cnpj) {
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

    public boolean alterar(String cnpj, String nomeFantasia, String razaoSocial) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            System.err.println("Validação falhou: CNPJ do fornecedor inválido.");
            return false;
        }
        if (nomeFantasia == null || nomeFantasia.trim().isEmpty()) {
            System.err.println("Validação falhou: Nome fantasia não pode ser vazio ao alterar.");
            return false;
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCnpj(cnpj);
        fornecedor.setNome_fantasia(nomeFantasia);
        fornecedor.setRazao_social(razaoSocial);

        return fornecedorDAO.alterar(fornecedor);
    }

    public boolean excluir(int id) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID inválido.");
            return false;
        }
        return fornecedorDAO.excluir(id);
    }

    public Fornecedor pesquisar(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            System.err.println("Validação falhou: CNPJ inválido para pesquisa.");
            return null;
        }
        return fornecedorDAO.pesquisar(cnpj);
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorDAO.listarTodos();
    }
}