package controller;


import dao.ProdutoDAO;
import model.Categoria;
import model.Produto;

import java.util.List;

// Lembre-se de importar o Produto, a Categoria e o ProdutoDAO com Ctrl + .
// Ex: import model.Produto;
//     import model.Categoria;
//     import dao.ProdutoDAO;

public class ProdutoController {

    private ProdutoDAO produtoDAO;

    public ProdutoController() {
        this.produtoDAO = new ProdutoDAO();
    }

    // ==========================================
    // 1. SALVAR
    // ==========================================
    public boolean salvar(String nome, double precoMedio, double qtdeEstoque, double valorUltimaCompra, double valorUltimaVenda, int idCategoria) {
        // Validação: Nome é obrigatório e precisa ter uma categoria vinculada
        if (nome == null || nome.trim().isEmpty()) {
            System.err.println("Validação falhou: O nome do produto é obrigatório.");
            return false;
        }
        if (idCategoria <= 0) {
            System.err.println("Validação falhou: Categoria inválida.");
            return false;
        }

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPreco_medio(precoMedio);
        produto.setQtde_estoque(qtdeEstoque);
        produto.setValor_ultima_compra(valorUltimaCompra);
        produto.setValor_ultima_venda(valorUltimaVenda);

        // Monta o objeto Categoria apenas com o ID para o DAO conseguir puxar
        Categoria categoria = new Categoria();
        categoria.setId(idCategoria);
        produto.setCategoria(categoria);

        return produtoDAO.salvar(produto);
    }

    // ==========================================
    // 2. ALTERAR
    // ==========================================
    public boolean alterar(int id, String nome, double precoMedio, double qtdeEstoque, double valorUltimaCompra, double valorUltimaVenda, int idCategoria) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID do produto inválido.");
            return false;
        }
        if (nome == null || nome.trim().isEmpty()) {
            System.err.println("Validação falhou: O nome não pode estar vazio ao alterar.");
            return false;
        }

        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome(nome);
        produto.setPreco_medio(precoMedio);
        produto.setQtde_estoque(qtdeEstoque);
        produto.setValor_ultima_compra(valorUltimaCompra);
        produto.setValor_ultima_venda(valorUltimaVenda);

        Categoria categoria = new Categoria();
        categoria.setId(idCategoria);
        produto.setCategoria(categoria);

        return produtoDAO.alterar(produto);
    }

    // ==========================================
    // 3. EXCLUIR
    // ==========================================
    public boolean excluir(int id) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID inválido.");
            return false;
        }
        return produtoDAO.excluir(id);
    }

    // ==========================================
    // 4. PESQUISAR (Pelo ID)
    // ==========================================
    public Produto pesquisar(int id) {
        if (id <= 0) {
            System.err.println("Validação falhou: ID inválido para pesquisa.");
            return null;
        }
        return produtoDAO.pesquisar(id);
    }

    // ==========================================
    // 5. LISTAR TODOS
    // ==========================================
    public List<Produto> listarTodos() {
        return produtoDAO.listarTodos();
    }
}