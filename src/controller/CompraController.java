package controller;

import dao.CompraDAO;
import model.Compra;
import model.Fornecedor;
import model.ItemCompra;

import java.util.List;

public class CompraController {

    private CompraDAO compraDAO;

    public CompraController() {
        this.compraDAO = new CompraDAO();
    }


    public boolean salvar(String dataCompra, int idFornecedor, List<ItemCompra> itens) {
        if (dataCompra == null || dataCompra.trim().isEmpty()) {
            System.err.println("Validação falhou: A data da compra é obrigatória.");
            return false;
        }
        if (idFornecedor <= 0) {
            System.err.println("Validação falhou: Fornecedor inválido.");
            return false;
        }
        if (itens == null || itens.isEmpty()) {
            System.err.println("Validação falhou: A compra precisa ter pelo menos um item.");
            return false;
        }

        double valorTotalCalculado = 0;
        for (ItemCompra item : itens) {
            if (item.getQuantidade() <= 0 || item.getValor_unitario() <= 0) {
                System.err.println("Validação falhou: Quantidade e valor unitário dos itens devem ser maiores que zero.");
                return false;
            }
            valorTotalCalculado += (item.getQuantidade() * item.getValor_unitario());
        }

        Compra compra = new Compra();
        compra.setData_compra(dataCompra);
        compra.setValor_total(valorTotalCalculado);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(idFornecedor);
        compra.setFornecedor(fornecedor);

        compra.setItens(itens);

        return compraDAO.salvar(compra);
    }

    public boolean alterar(int idCompra, String novaData, double novoValorTotal, int idFornecedor) {
        if (idCompra <= 0) {
            System.err.println("Validação falhou: ID da compra inválido.");
            return false;
        }

        Compra compra = new Compra();
        compra.setId(idCompra);
        compra.setData_compra(novaData);
        compra.setValor_total(novoValorTotal);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(idFornecedor);
        compra.setFornecedor(fornecedor);

        return compraDAO.alterar(compra);
    }

    public boolean excluir(int id) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID inválido.");
            return false;
        }
        return compraDAO.excluir(id);
    }

    public Compra pesquisar(int id) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID inválido para pesquisa.");
            return null;
        }
        return compraDAO.pesquisar(id);
    }
}