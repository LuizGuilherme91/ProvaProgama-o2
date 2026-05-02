package model;

public class ProdutoFornecedor {
    private int id;
    private Produto produto;
    private Fornecedor fornecedor;

    public ProdutoFornecedor() {
    }

    public ProdutoFornecedor(Produto produto, int id, Fornecedor fornecedor) {
        this.produto = produto;
        this.id = id;
        this.fornecedor = fornecedor;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public Produto getProduto() {return produto;}
    public void setProduto(Produto produto) {this.produto = produto;}

    public Fornecedor getFornecedor() {return fornecedor;}
    public void setFornecedor(Fornecedor fornecedor) {this.fornecedor = fornecedor;}
}