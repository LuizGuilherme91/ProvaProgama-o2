package model;

import java.util.ArrayList;
import java.util.List;

public class Compra extends ClasseGenerica {
    private int id;
    private String data_compra;
    private double valor_total;
    private Fornecedor fornecedor;
    private List<ItemCompra> itens = new ArrayList<>();

    public Compra() {
    }

    public Compra(int id, String data_compra, double valor_total,  Fornecedor fornecedor, ItemCompra itemCompra) {
        this.id = id;
        this.data_compra = data_compra;
        this.valor_total = valor_total;
        this.fornecedor = fornecedor;
        this.itens.add(itemCompra);
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

    public Fornecedor getFornecedor() {return fornecedor;}
    public void setFornecedor(Fornecedor fornecedor) {this.fornecedor = fornecedor;}

    public List<ItemCompra> getItens() {return itens;}
    public void setItens(List<ItemCompra> itens) {this.itens = itens;}

}
