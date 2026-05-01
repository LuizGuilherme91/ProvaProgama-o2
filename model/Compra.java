public class Compra extends ClasseGenerica {
    private int id;
    private String data_compra;
    private double valor_total;

    public Compra() {
    }

    public Compra(int id, String data_compra, double valor_total) {
        this.id = id;
        this.data_compra = data_compra;
        this.valor_total = valor_total;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getData_compra() {
        return data_compra;
    }
    public void setData_compra(String data_compra) {
        this.data_compra = data_compra;
    }

    public double getValor_total() {
        return valor_total;
    }
    public void setValor_total(double valor_total) {
        this.valor_total = valor_total;
    }

}
