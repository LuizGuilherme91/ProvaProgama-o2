package model;

public class ItemCompra extends ClasseGenerica {
    private int id;
    private double quantidade;
    private double valor_unitario;
    private double subtotal;
    private Compra compra;
    private Produto produto;

    public ItemCompra() {
    }

    public ItemCompra(int id, double quantidade, double valor_unitario, double subtotal, Compra compra, Produto produto) {
        this.id = id;
        this.quantidade = quantidade;
        this.valor_unitario = valor_unitario;
        this.subtotal = subtotal;
        this.compra = compra;
        this.produto = produto;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public double getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getValor_unitario() {
        return valor_unitario;
    }
    public void setValor_unitario(double valor_unitario) {
        this.valor_unitario = valor_unitario;
    }

    public double getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Compra getCompra() {return compra;}
    public void setCompra(Compra compra) {this.compra = compra;}

    public Produto getProduto() {return produto;}
    public void setProduto(Produto produto) {this.produto = produto;}
    

}
