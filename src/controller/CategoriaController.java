package controller;
import dao.CategoriaDAO;
import model.Categoria;

import java.util.List;

// Lembre-se de importar a sua Categoria e o CategoriaDAO com Ctrl + .
// Ex: import model.Categoria;
//     import dao.CategoriaDAO;

public class CategoriaController {

    private CategoriaDAO categoriaDAO;

    // Construtor: Toda vez que o Controller for chamado, ele prepara o DAO
    public CategoriaController() {
        this.categoriaDAO = new CategoriaDAO();
    }

    // ==========================================
    // 1. SALVAR
    // ==========================================
    // Veja que o Controller recebe apenas a String, monta o objeto e passa pro DAO!
    public boolean salvar(String nome) {
        // Validação básica de negócio
        if (nome == null || nome.trim().isEmpty()) {
            System.err.println("Validação falhou: O nome da categoria não pode estar vazio.");
            return false;
        }

        Categoria categoria = new Categoria();
        categoria.setNome(nome);

        return categoriaDAO.salvar(categoria);
    }

    // ==========================================
    // 2. ALTERAR
    // ==========================================
    public boolean alterar(int id, String novoNome) {
        if (id <= 0 || novoNome == null || novoNome.trim().isEmpty()) {
            System.err.println("Validação falhou: ID ou nome inválidos para alteração.");
            return false;
        }

        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNome(novoNome);

        return categoriaDAO.alterar(categoria);
    }

    // ==========================================
    // 3. EXCLUIR
    // ==========================================
    public boolean excluir(int id) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID inválido.");
            return false;
        }
        return categoriaDAO.excluir(id);
    }

    // ==========================================
    // 4. PESQUISAR
    // ==========================================
    public Categoria pesquisar(int id) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID inválido para pesquisa.");
            return null;
        }
        return categoriaDAO.pesquisar(id);
    }

    // ==========================================
    // 5. LISTAR TODAS
    // ==========================================
    public List<Categoria> listarTodas() {
        return categoriaDAO.listarTodas();
    }
}