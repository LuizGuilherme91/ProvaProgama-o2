package view;

import controller.VendaController;
import model.ItemVenda;
import model.Produto;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class VendaView {

    private VendaController vendaCtrl;

    public VendaView() {
        this.vendaCtrl = new VendaController();
    }

    public void exibirTelaCadastro() {
        JOptionPane.showMessageDialog(null, "=== REGISTRAR VENDA (SAÍDA DE ESTOQUE) ===\nSiga os passos a seguir.");

        String data = JOptionPane.showInputDialog(null, "1. Data da Venda (YYYY-MM-DD):");
        if (data == null || data.trim().isEmpty()) return;

        String idCliStr = JOptionPane.showInputDialog(null, "2. ID do Cliente:");
        if (idCliStr == null || idCliStr.trim().isEmpty()) return;

        String idProdStr = JOptionPane.showInputDialog(null, "3. ID do Produto sendo vendido:");
        if (idProdStr == null || idProdStr.trim().isEmpty()) return;

        String qtdStr = JOptionPane.showInputDialog(null, "4. Quantidade vendida:");
        if (qtdStr == null || qtdStr.trim().isEmpty()) return;

        String valorStr = JOptionPane.showInputDialog(null, "5. Valor Unitário (Venda):");
        if (valorStr == null || valorStr.trim().isEmpty()) return;

        try {
            // Convertendo os textos para números
            int idCliente = Integer.parseInt(idCliStr);
            int idProduto = Integer.parseInt(idProdStr);
            double quantidade = Double.parseDouble(qtdStr);
            double valorUnitario = Double.parseDouble(valorStr);

            // Montando a lista de itens da venda (com 1 item para simplificar)
            List<ItemVenda> itens = new ArrayList<>();
            ItemVenda item = new ItemVenda();

            Produto p = new Produto();
            p.setId(idProduto);

            item.setProduto(p);
            item.setQuantidade(quantidade);
            item.setValor_unitario(valorUnitario);
            itens.add(item);

            // Manda o Controller salvar (Onde o limite de 3 vendas e o estoque são validados)
            boolean sucesso = vendaCtrl.salvar(data, idCliente, itens);

            if (sucesso) {
                JOptionPane.showMessageDialog(null, "Sucesso! Venda registrada.\nEstoque reduzido e valor de última venda atualizado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Venda Recusada!\nPossíveis motivos:\n- Falta de estoque.\n- Limite de 3 vendas no mês excedido para este CPF.\n- IDs inválidos.", "Aviso de Regra de Negócio", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro de digitação: Certifique-se de digitar apenas números nos campos de ID, Quantidade e Valor.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }
}