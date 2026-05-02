package view;

import controller.CategoriaController;
import model.Categoria;
import javax.swing.JOptionPane;

public class CategoriaView {

    private CategoriaController categoriaCtrl;

    public CategoriaView() {
        this.categoriaCtrl = new CategoriaController();
    }

    // Método principal chamado pela TelaPrincipal
    public void exibirMenu() {
        String[] opcoes = {"1. Cadastrar", "2. Pesquisar", "3. Alterar", "4. Excluir", "Voltar"};
        boolean continuar = true;

        while (continuar) {
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "=== MÓDULO DE CATEGORIAS ===\nEscolha uma operação:",
                    "Menu - Categoria",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            // 'escolha' retorna o índice do botão clicado (0 a 4)
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
                    // Se clicar em "Voltar" ou fechar a janela no X
                    continuar = false;
                    break;
            }
        }
    }

    // ==========================================
    // MÉTODOS DO CRUD
    // ==========================================

    private void telaCadastrar() {
        String nome = JOptionPane.showInputDialog(null, "=== CADASTRAR CATEGORIA ===\nDigite o nome da Categoria:");
        if (nome == null || nome.trim().isEmpty()) return;

        boolean sucesso = categoriaCtrl.salvar(nome);
        if (sucesso) {
            JOptionPane.showMessageDialog(null, "Categoria '" + nome + "' cadastrada com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar categoria.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaPesquisar() {
        String idStr = JOptionPane.showInputDialog(null, "=== PESQUISAR CATEGORIA ===\nDigite o ID da Categoria:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);
            Categoria cat = categoriaCtrl.pesquisar(id);

            if (cat != null) {
                JOptionPane.showMessageDialog(null, "Categoria Encontrada:\n\nID: " + cat.getId() + "\nNome: " + cat.getNome(), "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Categoria não encontrada para o ID: " + id, "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaAlterar() {
        String idStr = JOptionPane.showInputDialog(null, "=== ALTERAR CATEGORIA ===\nDigite o ID da Categoria que deseja alterar:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);
            Categoria cat = categoriaCtrl.pesquisar(id);

            if (cat != null) {
                // Mostra o nome atual e pede o novo
                String novoNome = JOptionPane.showInputDialog(null, "Nome atual: " + cat.getNome() + "\n\nDigite o NOVO nome para a categoria:");
                if (novoNome == null || novoNome.trim().isEmpty()) return;

                boolean sucesso = categoriaCtrl.alterar(id, novoNome);
                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Categoria alterada com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao alterar categoria.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Categoria não encontrada para o ID: " + id, "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaExcluir() {
        String idStr = JOptionPane.showInputDialog(null, "=== EXCLUIR CATEGORIA ===\nDigite o ID da Categoria que deseja excluir:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);

            // Pede uma confirmação antes de apagar do banco
            int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir a categoria de ID " + id + "?", "Confirmação", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                boolean sucesso = categoriaCtrl.excluir(id);
                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Categoria excluída com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir. Verifique se o ID existe.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}