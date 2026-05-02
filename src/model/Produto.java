package model;

import java.util.ArrayList;
import java.util.List;

public class Produto extends ClasseGenerica{
    private int id;
    private String nome;
    private double preco_medio;
    private double qtde_estoque;
    private double valor_ultima_compra;
    private double valor_ultima_venda;
    private Categoria categoria;
    private List<Fornecedor> fornecedores = new ArrayList<>();

    public Produto() {
    }

    public Produto(int id, String nome, double preco_medio, double qtde_estoque, double valor_ultima_compra, double valor_ultima_venda,  Categoria categoria, List<Fornecedor> fornecedores) {
        this.id = id;
        this.nome = nome;
        this.preco_medio = preco_medio;
        this.qtde_estoque = qtde_estoque;
        this.valor_ultima_compra = valor_ultima_compra;
        this.valor_ultima_venda = valor_ultima_venda;
        this.categoria = categoria;
        this.fornecedores = fornecedores;
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

    public double getPreco_medio() {
        return preco_medio;
    }
    public void setPreco_medio(double preco_medio) {
        this.preco_medio = preco_medio;
    }

    public double getQtde_estoque() {
        return qtde_estoque;
    }
    public void setQtde_estoque(double qtde_estoque) {
        this.qtde_estoque = qtde_estoque;
    }

    public double getValor_ultima_compra() {
        return valor_ultima_compra;
    }
    public void setValor_ultima_compra(double valor_ultima_compra) {
        this.valor_ultima_compra = valor_ultima_compra;
    }

    public double getValor_ultima_venda() {
        return valor_ultima_venda;
    }
    public void setValor_ultima_venda(double valor_ultima_venda) {
        this.valor_ultima_venda = valor_ultima_venda;
    }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public  List<Fornecedor> getFornecedores() { return fornecedores; }
    public void setFornecedores(List<Fornecedor> fornecedores) {}

}
