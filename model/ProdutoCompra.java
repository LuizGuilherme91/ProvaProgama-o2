public class ProdutoCompra extends ClasseGenerica {
    private int id;
    private int id_compra;
    private int id_produto;
    private int quantidade;
    private double valor_unitario;

    public ProdutoCompra() {
    }

    public ProdutoCompra(int id, int id_compra, int id_produto, int quantidade, double valor_unitario) {
        this.id = id;
        this.id_compra = id_compra;
        this.id_produto = id_produto;
        this.quantidade = quantidade;
        this.valor_unitario = valor_unitario;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getId_compra() {
        return id_compra;
    }
    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public int getId_produto() {
        return id_produto;
    }
    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }

    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValor_unitario() {
        return valor_unitario;
    }
    public void setValor_unitario(double valor_unitario) {
        this.valor_unitario = valor_unitario;
    }

}
