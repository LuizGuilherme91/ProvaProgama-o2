import java.sql.Date;
import java.util.List;

public class Venda extends ClasseGenerica{
    private int id;
    private Date data_venda;
    private double valor_total;
    private Cliente cliente; // Objeto cliente
    private List<ItemVenda> itens; // Lista com os produtos que estão sendo vendidos
// + Getters e Setters

    public Venda() {
    }

    public Venda(int id, Date data_venda, double valor_total) {
        this.id = id;
        this.data_venda = data_venda;
        this.valor_total = valor_total;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Date getData_venda() {
        return data_venda;
    }
    public void setData_venda(Date data_venda) {
        this.data_venda = data_venda;
    }

    public double getValor_total() {
        return valor_total;
    }
    public void setValor_total(double valor_total) {
        this.valor_total = valor_total;
    }

    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }
    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }
}