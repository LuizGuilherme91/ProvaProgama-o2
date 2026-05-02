package controller;

import dao.ClienteDAO;
import model.Cliente;

import java.util.List;

// Lembre-se de importar o Cliente e o ClienteDAO com Ctrl + .
// Ex: import model.Cliente;
//     import dao.ClienteDAO;

public class ClienteController {

    private ClienteDAO clienteDAO;

    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
    }

    // ==========================================
    // 1. SALVAR
    // ==========================================
    public boolean salvar(String nome, String cpf, String rg, String endereco, String telefone) {
        // Validação: Nome e CPF não podem ser vazios (são obrigatórios no nosso banco)
        if (nome == null || nome.trim().isEmpty()) {
            System.err.println("Validação falhou: O nome do cliente é obrigatório.");
            return false;
        }
        if (cpf == null || cpf.trim().isEmpty()) {
            System.err.println("Validação falhou: O CPF do cliente é obrigatório.");
            return false;
        }

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setRg(rg);
        cliente.setEndereco(endereco);
        cliente.setTelefone(telefone);

        return clienteDAO.salvar(cliente);
    }

    // ==========================================
    // 2. ALTERAR
    // ==========================================
    public boolean alterar(int id, String nome, String cpf, String rg, String endereco, String telefone) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID do cliente inválido.");
            return false;
        }
        if (nome == null || nome.trim().isEmpty() || cpf == null || cpf.trim().isEmpty()) {
            System.err.println("Validação falhou: Nome e CPF não podem ser vazios ao alterar.");
            return false;
        }

        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setRg(rg);
        cliente.setEndereco(endereco);
        cliente.setTelefone(telefone);

        return clienteDAO.alterar(cliente);
    }

    // ==========================================
    // 3. EXCLUIR
    // ==========================================
    public boolean excluir(int id) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID inválido.");
            return false;
        }
        return clienteDAO.excluir(id);
    }

    // ==========================================
    // 4. PESQUISAR (Pelo CPF, conforme o DAO)
    // ==========================================
    public Cliente pesquisar(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            System.err.println("Validação falhou: CPF inválido para pesquisa.");
            return null;
        }
        return clienteDAO.pesquisar(cpf);
    }

    // ==========================================
    // 5. LISTAR TODOS
    // ==========================================
    public List<Cliente> listarTodos() {
        return clienteDAO.listarTodos();
    }
}