package view;

import controller.VendaController;
import model.ItemVenda;
import model.Produto;
import model.Venda;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class VendaView {

    private VendaController vendaCtrl;

    public VendaView() {
        this.vendaCtrl = new VendaController();
    }

    public void exibirMenu() {
        String[] opcoes = {"1. Registrar Venda", "2. Pesquisar", "3. Alterar Capa", "4. Excluir", "Voltar"};
        boolean continuar = true;

        while (continuar) {
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "=== MÓDULO DE VENDAS ===\nEscolha uma operação:",
                    "Menu - Venda",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            switch (escolha) {
                case 0:
                    telaCadastrar();
                    break;
                case 1:
                    telaPesquisar();
                    break;
                case 2:
                    telaAlterar();
                    break;
                case 3:
                    telaExcluir();
                    break;
                default:
                    continuar = false;
                    break;
            }
        }
    }

    // ==========================================
    // MÉTODOS DO CRUD
    // ==========================================

    private void telaCadastrar() {
        JOptionPane.showMessageDialog(null, "=== REGISTRAR VENDA ===\nSiga os passos a seguir.");

        String data = JOptionPane.showInputDialog(null, "1. Data da Venda (YYYY-MM-DD):");
        if (data == null || data.trim().isEmpty()) return;

        String idCliStr = JOptionPane.showInputDialog(null, "2. ID do Cliente:");
        if (idCliStr == null || idCliStr.trim().isEmpty()) return;

        String idProdStr = JOptionPane.showInputDialog(null, "3. ID do Produto sendo vendido:");
        if (idProdStr == null || idProdStr.trim().isEmpty()) return;

        String qtdStr = JOptionPane.showInputDialog(null, "4. Quantidade vendida:");
        if (qtdStr == null || qtdStr.trim().isEmpty()) return;

        String valorStr = JOptionPane.showInputDialog(null, "5. Valor Unitário (Preço de Venda):");
        if (valorStr == null || valorStr.trim().isEmpty()) return;

        try {
            int idCliente = Integer.parseInt(idCliStr);
            int idProduto = Integer.parseInt(idProdStr);
            double quantidade = Double.parseDouble(qtdStr);
            double valorUnitario = Double.parseDouble(valorStr);

            List<ItemVenda> itens = new ArrayList<>();
            ItemVenda item = new ItemVenda();
            Produto p = new Produto();
            p.setId(idProduto);

            item.setProduto(p);
            item.setQuantidade(quantidade);
            item.setValor_unitario(valorUnitario);
            itens.add(item);

            boolean sucesso = vendaCtrl.salvar(data, idCliente, itens);

            if (sucesso) {
                JOptionPane.showMessageDialog(null, "Venda registrada com sucesso!\nEstoque reduzido e valor de última venda atualizado.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Venda Recusada!\nMotivos comuns:\n- Falta de estoque.\n- Limite de 3 vendas mensais excedido (RNF004).\n- IDs inválidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro: Digite apenas números nos IDs, Quantidade e Valor.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaPesquisar() {
        String idStr = JOptionPane.showInputDialog(null, "=== PESQUISAR VENDA ===\nDigite o ID da Venda:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);
            Venda v = vendaCtrl.pesquisar(id);

            if (v != null) {
                JOptionPane.showMessageDialog(null, "Venda Encontrada!\n\nID: " + v.getId() + "\nData: " + v.getData_venda(), "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Venda não encontrada para o ID: " + id, "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaAlterar() {
        String idStr = JOptionPane.showInputDialog(null, "=== ALTERAR VENDA ===\nDigite o ID da Venda que deseja alterar:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);
            Venda v = vendaCtrl.pesquisar(id);

            if (v != null) {
                String novaData = JOptionPane.showInputDialog(null, "Data atual: " + v.getData_venda() + "\nNova Data (YYYY-MM-DD):");
                if (novaData == null || novaData.trim().isEmpty()) return;

                String idCliStr = JOptionPane.showInputDialog(null, "Digite o ID do Cliente válido para esta venda:");
                if (idCliStr == null || idCliStr.trim().isEmpty()) return;
                int idCliente = Integer.parseInt(idCliStr);

                // AQUI ESTÁ A CORREÇÃO: Passando id, data, o valor total que já estava salvo e o id do cliente!
                boolean sucesso = vendaCtrl.alterar(id, novaData, v.getValor_total(), idCliente);

                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Venda alterada com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao alterar venda. Verifique se o ID do Cliente realmente existe.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Venda não encontrada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao processar a alteração. Digite apenas números no ID.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaExcluir() {
        String idStr = JOptionPane.showInputDialog(null, "=== EXCLUIR VENDA ===\nDigite o ID da Venda que deseja excluir:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);

            int confirmacao = JOptionPane.showConfirmDialog(null, "Atenção: Excluir uma venda pode causar inconsistência no estoque. Deseja continuar?", "Confirmação", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                boolean sucesso = vendaCtrl.excluir(id);
                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Venda excluída com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir. Verifique se o ID existe.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}