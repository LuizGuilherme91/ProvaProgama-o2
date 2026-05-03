package controller;

import dao.VendaDAO;
import model.Cliente;
import model.ItemVenda;
import model.Venda;

import java.sql.Date;
import java.util.List;

public class VendaController {

    private VendaDAO vendaDAO;

    public VendaController() {
        this.vendaDAO = new VendaDAO();
    }

    // ==========================================
    // 1. SALVAR (Com bloqueio RNF004)
    // ==========================================
    public boolean salvar(String dataVenda, int idCliente, List<ItemVenda> itens) {
        // Validações básicas
        if (dataVenda == null || dataVenda.trim().isEmpty()) {
            System.err.println("Validação falhou: A data da venda é obrigatória.");
            return false;
        }
        if (idCliente <= 0) {
            System.err.println("Validação falhou: Cliente inválido.");
            return false;
        }
        if (itens == null || itens.isEmpty()) {
            System.err.println("Validação falhou: A venda precisa ter pelo menos um item.");
            return false;
        }

        // ==========================================================
        // RNF004: Verifica quantas vendas o cliente tem no mês
        // ==========================================================
        int vendasNesteMes = vendaDAO.contarVendasPorClienteNoMes(idCliente, dataVenda);
        if (vendasNesteMes >= 3) {
            System.err.println("RNF004 Bloqueada: Cliente já possui 3 vendas neste mês.");
            return false; // Bloqueia a venda imediatamente e devolve falso pra View
        }

        // Calcula o valor total da venda somando (quantidade * valor unitário) de cada item
        double valorTotalCalculado = 0;
        for (ItemVenda item : itens) {
            if (item.getQuantidade() <= 0 || item.getValor_unitario() <= 0) {
                System.err.println("Validação falhou: Quantidade e valor unitário dos itens devem ser maiores que zero.");
                return false;
            }
            valorTotalCalculado += (item.getQuantidade() * item.getValor_unitario());
        }

        // Monta o objeto Venda
        Venda venda = new Venda();
        venda.setData_venda(Date.valueOf(dataVenda));
        venda.setValor_total(valorTotalCalculado);

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        venda.setCliente(cliente);

        venda.setItens(itens);

        // Manda pro DAO salvar (onde RNF001, RNF003 e RNF005 serão executados)
        return vendaDAO.salvar(venda);
    }

    // ==========================================
    // 2. ALTERAR (Apenas dados da capa da venda)
    // ==========================================
    public boolean alterar(int idVenda, String novaData, double novoValorTotal, int idCliente) {
        if (idVenda <= 0) {
            System.err.println("Validação falhou: ID da venda inválido.");
            return false;
        }

        Venda venda = new Venda();
        venda.setId(idVenda);
        venda.setData_venda(Date.valueOf(novaData));
        venda.setValor_total(novoValorTotal);

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        venda.setCliente(cliente);

        return vendaDAO.alterar(venda);
    }

    // ==========================================
    // 3. EXCLUIR
    // ==========================================
    public boolean excluir(int id) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID inválido.");
            return false;
        }
        return vendaDAO.excluir(id);
    }

    // ==========================================
    // 4. PESQUISAR
    // ==========================================
    public Venda pesquisar(int id) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID inválido para pesquisa.");
            return null;
        }
        return vendaDAO.pesquisar(id);
    }
}