public class ItemVenda {
    private int id;
    private double quantidade;
    private double valor_unitario;
    private double subtotal;

    public ItemVenda() {
    }

    public ItemVenda(int id, double quantidade, double valor_unitario, double subtotal) {
        this.id = id;
        this.quantidade = quantidade;
        this.valor_unitario = valor_unitario;
        this.subtotal = subtotal;
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
    

}
