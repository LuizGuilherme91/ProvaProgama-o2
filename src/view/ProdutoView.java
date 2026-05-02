package view;

import controller.ProdutoController;
import model.Produto;
import javax.swing.JOptionPane;

public class ProdutoView {

    private ProdutoController produtoCtrl;

    public ProdutoView() {
        this.produtoCtrl = new ProdutoController();
    }

    public void exibirMenu() {
        String[] opcoes = {"1. Cadastrar", "2. Pesquisar", "3. Alterar", "4. Excluir", "Voltar"};
        boolean continuar = true;

        while (continuar) {
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "=== MÓDULO DE PRODUTOS ===\nEscolha uma operação:",
                    "Menu - Produto",
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
        JOptionPane.showMessageDialog(null, "=== CADASTRO DE PRODUTO ===\nSiga os passos a seguir.");

        String nome = JOptionPane.showInputDialog(null, "1. Digite o NOME do Produto:");
        if (nome == null || nome.trim().isEmpty()) return;

        String idCatStr = JOptionPane.showInputDialog(null, "2. Digite o ID da Categoria deste produto:");
        if (idCatStr == null || idCatStr.trim().isEmpty()) return;

        try {
            int idCategoria = Integer.parseInt(idCatStr);

            // Estoque e preços sempre começam zerados no cadastro inicial
            boolean sucesso = produtoCtrl.salvar(nome, 0.0, 0.0, 0.0, 0.0, idCategoria);

            if (sucesso) {
                JOptionPane.showMessageDialog(null, "Produto '" + nome + "' cadastrado com sucesso!\nEstoque inicial: 0");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto. Verifique se a Categoria existe.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro de digitação: O ID da Categoria deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaPesquisar() {
        String idStr = JOptionPane.showInputDialog(null, "=== PESQUISAR PRODUTO ===\nDigite o ID do Produto:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);
            Produto p = produtoCtrl.pesquisar(id);

            if (p != null) {
                String dados = "Produto Encontrado:\n\n" +
                        "ID: " + p.getId() + "\n" +
                        "Nome: " + p.getNome() + "\n" +
                        "Estoque Atual: " + p.getQtdeEstoque() + "\n" +
                        "Preço Médio (Custo): R$ " + p.getPrecoMedio() + "\n" +
                        "Valor Última Venda: R$ " + p.getValorUltimaVenda();
                JOptionPane.showMessageDialog(null, dados, "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Produto não encontrado para o ID: " + id, "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaAlterar() {
        String idStr = JOptionPane.showInputDialog(null, "=== ALTERAR PRODUTO ===\nDigite o ID do Produto que deseja alterar:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);
            Produto p = produtoCtrl.pesquisar(id);

            if (p != null) {
                JOptionPane.showMessageDialog(null, "Atenção: Estoque e Preços são alterados automaticamente via Compras e Vendas.\nAqui você altera apenas os dados básicos.");

                String novoNome = JOptionPane.showInputDialog(null, "Nome atual: " + p.getNome() + "\nNovo Nome:");
                if (novoNome == null) return;
                if (novoNome.trim().isEmpty()) novoNome = p.getNome();

                String idCatStr = JOptionPane.showInputDialog(null, "Digite o NOVO ID da Categoria (ou o mesmo para manter):");
                if (idCatStr == null || idCatStr.trim().isEmpty()) return;

                int novaCategoria = Integer.parseInt(idCatStr);

                // Passamos o novo nome e nova categoria, mas mantemos os preços e estoque que já estavam no objeto pesquisado
                boolean sucesso = produtoCtrl.alterar(id, novoNome, p.getPrecoCusto(), p.getQtdeEstoque(), p.getPrecoMedio(), p.getValorUltimaVenda(), novaCategoria);

                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Produto alterado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao alterar produto.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Produto não encontrado para o ID: " + id, "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID/Categoria inválidos! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaExcluir() {
        String idStr = JOptionPane.showInputDialog(null, "=== EXCLUIR PRODUTO ===\nDigite o ID do Produto que deseja excluir:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);

            int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o produto de ID " + id + "?", "Confirmação", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                boolean sucesso = produtoCtrl.excluir(id);
                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Produto excluído com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir. Verifique se o ID existe ou se o produto já possui histórico de vendas/compras.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}