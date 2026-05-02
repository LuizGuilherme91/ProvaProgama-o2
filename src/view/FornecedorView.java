package view;

import controller.FornecedorController;
import model.Fornecedor;
import javax.swing.JOptionPane;

public class FornecedorView {

    private FornecedorController fornecedorCtrl;

    public FornecedorView() {
        this.fornecedorCtrl = new FornecedorController();
    }

    public void exibirMenu() {
        String[] opcoes = {"1. Cadastrar", "2. Pesquisar", "3. Alterar", "4. Excluir", "Voltar"};
        boolean continuar = true;

        while (continuar) {
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "=== MÓDULO DE FORNECEDORES ===\nEscolha uma operação:",
                    "Menu - Fornecedor",
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
        JOptionPane.showMessageDialog(null, "=== CADASTRO DE FORNECEDOR ===\nSiga os passos a seguir.");

        String nomeFantasia = JOptionPane.showInputDialog(null, "1. Digite o NOME FANTASIA do Fornecedor:");
        if (nomeFantasia == null || nomeFantasia.trim().isEmpty()) return;

        String razaoSocial = JOptionPane.showInputDialog(null, "2. Digite a RAZÃO SOCIAL do Fornecedor:");
        if (razaoSocial == null) return;

        String cnpj = JOptionPane.showInputDialog(null, "3. Digite o CNPJ do Fornecedor:");
        if (cnpj == null || cnpj.trim().isEmpty()) return;

        boolean sucesso = fornecedorCtrl.salvar(nomeFantasia, razaoSocial, cnpj);
        if (sucesso) {
            JOptionPane.showMessageDialog(null, "Fornecedor '" + nomeFantasia + "' cadastrado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar fornecedor.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaPesquisar() {
        String idStr = JOptionPane.showInputDialog(null, "=== PESQUISAR FORNECEDOR ===\nDigite o ID do Fornecedor:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);
            Fornecedor f = fornecedorCtrl.pesquisar(idStr);

            if (f != null) {
                String dados = "Fornecedor Encontrado:\n\n" +
                        "ID: " + f.getId() + "\n" +
                        "Nome Fantasia: " + f.getNome_fantasia() + "\n" +
                        "Razão Social: " + f.getRazao_social() + "\n" +
                        "CNPJ: " + f.getCnpj();
                JOptionPane.showMessageDialog(null, dados, "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Fornecedor não encontrado para o ID: " + id, "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaAlterar() {
        String idStr = JOptionPane.showInputDialog(null, "=== ALTERAR FORNECEDOR ===\nDigite o ID do Fornecedor que deseja alterar:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);
            Fornecedor f = fornecedorCtrl.pesquisar(idStr);

            if (f != null) {
                JOptionPane.showMessageDialog(null, "Deixe o campo em branco se não quiser alterar o valor atual.");

                String nomeFantasia = JOptionPane.showInputDialog(null, "Nome Fantasia atual: " + f.getNome_fantasia() + "\nNovo Nome Fantasia:");
                if (nomeFantasia == null) return;
                if (nomeFantasia.trim().isEmpty()) nomeFantasia = f.getNome_fantasia();

                String razaoSocial = JOptionPane.showInputDialog(null, "Razão Social atual: " + f.getRazao_social() + "\nNova Razão Social:");
                if (razaoSocial == null) return;
                if (razaoSocial.trim().isEmpty()) razaoSocial = f.getRazao_social();

                String cnpj = JOptionPane.showInputDialog(null, "CNPJ atual: " + f.getCnpj() + "\nNovo CNPJ:");
                if (cnpj == null) return;
                if (cnpj.trim().isEmpty()) cnpj = f.getCnpj();

                boolean sucesso = fornecedorCtrl.alterar(id, nomeFantasia, razaoSocial, cnpj);
                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Fornecedor alterado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao alterar fornecedor.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Fornecedor não encontrado para o ID: " + id, "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaExcluir() {
        String idStr = JOptionPane.showInputDialog(null, "=== EXCLUIR FORNECEDOR ===\nDigite o ID do Fornecedor que deseja excluir:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);

            int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o fornecedor de ID " + id + "?", "Confirmação", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                boolean sucesso = fornecedorCtrl.excluir(id);
                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Fornecedor excluído com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir. Verifique se o ID existe.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}