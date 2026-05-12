package controller;
import dao.CategoriaDAO;
import model.Categoria;

import java.util.List;

public class CategoriaController {

    private CategoriaDAO categoriaDAO;

    public CategoriaController() {
        this.categoriaDAO = new CategoriaDAO();
    }

    public boolean salvar(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            System.err.println("Validação falhou: O nome da categoria não pode estar vazio.");
            return false;
        }

        Categoria categoria = new Categoria();
        categoria.setNome(nome);

        return categoriaDAO.salvar(categoria);
    }

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

    public boolean excluir(int id) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID inválido.");
            return false;
        }
        return categoriaDAO.excluir(id);
    }

    public Categoria pesquisar(int id) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID inválido para pesquisa.");
            return null;
        }
        return categoriaDAO.pesquisar(id);
    }

    public List<Categoria> listarTodas() {
        return categoriaDAO.listarTodas();
    }
}