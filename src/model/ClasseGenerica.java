package model;

public class ClasseGenerica {
    private int id;
    private String nome;
    private double valor_unitario;
    private double subtotal;

    public ClasseGenerica() {
    }

    public ClasseGenerica(int id, String nome, double valor_unitario, double subtotal) {
        this.id = id;
        this.nome = nome;
        this.valor_unitario = valor_unitario;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
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
