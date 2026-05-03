package view;

import controller.ClienteController;
import model.Cliente;
import javax.swing.JOptionPane;

public class ClienteView {

    private ClienteController clienteCtrl;

    public ClienteView() {
        this.clienteCtrl = new ClienteController();
    }

    public void exibirMenu() {
        String[] opcoes = {"1. Cadastrar", "2. Pesquisar", "3. Alterar", "4. Excluir", "Voltar"};
        boolean continuar = true;

        while (continuar) {
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "=== MÓDULO DE CLIENTES ===\nEscolha uma operação:",
                    "Menu - Cliente",
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
        JOptionPane.showMessageDialog(null, "=== CADASTRO DE CLIENTE ===\nSiga os passos a seguir.");

        String nome = JOptionPane.showInputDialog(null, "1. Digite o NOME do Cliente:");
        if (nome == null || nome.trim().isEmpty()) return;

        String cpf = JOptionPane.showInputDialog(null, "2. Digite o CPF do Cliente:");
        if (cpf == null || cpf.trim().isEmpty()) return;

        String rg = JOptionPane.showInputDialog(null, "3. Digite o RG do Cliente:");
        if (rg == null) return;

        String endereco = JOptionPane.showInputDialog(null, "4. Digite o ENDEREÇO do Cliente:");
        if (endereco == null) return;

        String telefone = JOptionPane.showInputDialog(null, "5. Digite o TELEFONE do Cliente:");
        if (telefone == null) return;

        boolean sucesso = clienteCtrl.salvar(nome, cpf, rg, endereco, telefone);
        if (sucesso) {
            JOptionPane.showMessageDialog(null, "Cliente '" + nome + "' cadastrado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaPesquisar() {
        String idStr = JOptionPane.showInputDialog(null, "=== PESQUISAR CLIENTE ===\nDigite o CPF do Cliente:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            Cliente c = clienteCtrl.pesquisar(idStr);

            if (c != null) {
                String dados = "Cliente Encontrado:\n\n" +
                        "ID: " + c.getId() + "\n" +
                        "Nome: " + c.getNome() + "\n" +
                        "CPF: " + c.getCpf() + "\n" +
                        "RG: " + c.getRg() + "\n" +
                        "Endereço: " + c.getEndereco() + "\n" +
                        "Telefone: " + c.getTelefone();
                JOptionPane.showMessageDialog(null, dados, "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Cliente não encontrado para o ID: " + idStr, "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaAlterar() {
        String idStr = JOptionPane.showInputDialog(null, "=== ALTERAR CLIENTE ===\nDigite o CPF do Cliente que deseja alterar:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            Cliente c = clienteCtrl.pesquisar(idStr);

            if (c != null) {
                JOptionPane.showMessageDialog(null, "Deixe o campo em branco se não quiser alterar o valor atual.");

                String nome = JOptionPane.showInputDialog(null, "Nome atual: " + c.getNome() + "\nNovo Nome:");
                if (nome == null) return;
                if (nome.trim().isEmpty()) nome = c.getNome(); // Mantém o antigo se deixar em branco

                String cpf = JOptionPane.showInputDialog(null, "CPF atual: " + c.getCpf() + "\nNovo CPF:");
                if (cpf == null) return;
                if (cpf.trim().isEmpty()) cpf = c.getCpf();

                String rg = JOptionPane.showInputDialog(null, "RG atual: " + c.getRg() + "\nNovo RG:");
                if (rg == null) return;
                if (rg.trim().isEmpty()) rg = c.getRg();

                String endereco = JOptionPane.showInputDialog(null, "Endereço atual: " + c.getEndereco() + "\nNovo Endereço:");
                if (endereco == null) return;
                if (endereco.trim().isEmpty()) endereco = c.getEndereco();

                String telefone = JOptionPane.showInputDialog(null, "Telefone atual: " + c.getTelefone() + "\nNovo Telefone:");
                if (telefone == null) return;
                if (telefone.trim().isEmpty()) telefone = c.getTelefone();

                boolean sucesso = clienteCtrl.alterar(c.getId(), nome, cpf, rg, endereco, telefone);
                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Cliente alterado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao alterar cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cliente não encontrado para o ID: " + idStr, "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void telaExcluir() {
        String idStr = JOptionPane.showInputDialog(null, "=== EXCLUIR CLIENTE ===\nDigite o ID do Cliente que deseja excluir:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);

            int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o cliente de ID " + id + "?", "Confirmação", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                boolean sucesso = clienteCtrl.excluir(id);
                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir. Verifique se o ID existe.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}