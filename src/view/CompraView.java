package view;

import controller.CompraController;
import model.Compra;
import model.ItemCompra;
import model.Produto;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class CompraView {

    private CompraController compraCtrl;

    public CompraView() {
        this.compraCtrl = new CompraController();
    }

    public void exibirMenu() {
        String[] opcoes = {"1. Registrar Compra", "2. Pesquisar", "3. Alterar Capa", "4. Excluir", "Voltar"};
        boolean continuar = true;

        while (continuar) {
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "=== MÓDULO DE COMPRAS ===\nEscolha uma operação:",
                    "Menu - Compra",
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
        JOptionPane.showMessageDialog(null, "=== REGISTRAR COMPRA ===\nSiga os passos a seguir.");

        String data = JOptionPane.showInputDialog(null, "1. Data da Compra (YYYY-MM-DD):");
        if (data == null || data.trim().isEmpty()) return;

        String idFornStr = JOptionPane.showInputDialog(null, "2. ID do Fornecedor:");
        if (idFornStr == null || idFornStr.trim().isEmpty()) return;

        String idProdStr = JOptionPane.showInputDialog(null, "3. ID do Produto sendo comprado:");
        if (idProdStr == null || idProdStr.trim().isEmpty()) return;

        String qtdStr = JOptionPane.showInputDialog(null, "4. Quantidade comprada:");
        if (qtdStr == null || qtdStr.trim().isEmpty()) return;

        String valorStr = JOptionPane.showInputDialog(null, "5. Valor Unitário (Custo):");
        if (valorStr == null || valorStr.trim().isEmpty()) return;

        try {
            int idFornecedor = Integer.parseInt(idFornStr);
            int idProduto = Integer.parseInt(idProdStr);
            double quantidade = Double.parseDouble(qtdStr);
            double valorUnitario = Double.parseDouble(valorStr);

            List<ItemCompra> itens = new ArrayList<>();
            ItemCompra item = new ItemCompra();
            Produto p = new Produto();
            p.setId(idProduto);

            item.setProduto(p);
            item.setQuantidade(quantidade);
            item.setValor_unitario(valorUnitario);
            itens.add(item);

            boolean sucesso = compraCtrl.salvar(data, idFornecedor, itens);

            if (sucesso) {
                JOptionPane.showMessageDialog(null, "Compra registrada! Estoque e preço médio atualizados.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao registrar compra.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro: Digite apenas números nos IDs, Quantidade e Valor.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaPesquisar() {
        String idStr = JOptionPane.showInputDialog(null, "=== PESQUISAR COMPRA ===\nDigite o ID da Compra:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);
            Compra c = compraCtrl.pesquisar(id);

            if (c != null) {
                JOptionPane.showMessageDialog(null, "Compra Encontrada!\n\nID: " + c.getId() + "\nData: " + c.getData_compra(), "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Compra não encontrada para o ID: " + id, "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaAlterar() {
        String idStr = JOptionPane.showInputDialog(null, "=== ALTERAR COMPRA ===\nDigite o ID da Compra que deseja alterar:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);
            Compra c = compraCtrl.pesquisar(id);

            if (c != null) {
                String novaData = JOptionPane.showInputDialog(null, "Data atual: " + c.getData_compra() + "\nNova Data (YYYY-MM-DD):");
                if (novaData == null || novaData.trim().isEmpty()) return;

                // Passando a lista de itens vazia apenas para atualizar a capa, se o seu controller permitir
                boolean sucesso = compraCtrl.alterar(id, novaData, 1, 1);

                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Compra alterada com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao alterar compra.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Compra não encontrada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao processar a alteração.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaExcluir() {
        String idStr = JOptionPane.showInputDialog(null, "=== EXCLUIR COMPRA ===\nDigite o ID da Compra que deseja excluir:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);

            int confirmacao = JOptionPane.showConfirmDialog(null, "Atenção: Excluir uma compra pode causar inconsistência no estoque. Deseja continuar?", "Confirmação", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                boolean sucesso = compraCtrl.excluir(id);
                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Compra excluída com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir. Verifique se o ID existe.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}