package view;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Sistema de Estoque e Vendas - Menu Principal");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 1, 10, 10));

        // Botões agora representam MÓDULOS
        JButton btnCategoria = new JButton("1. Módulo de Categorias");
        JButton btnCliente = new JButton("2. Módulo de Clientes");
        JButton btnFornecedor = new JButton("3. Módulo de Fornecedores");
        JButton btnProduto = new JButton("4. Módulo de Produtos");
        JButton btnCompra = new JButton("5. Módulo de Compras");
        JButton btnVenda = new JButton("6. Módulo de Vendas");
        JButton btnSair = new JButton("7. Sair do Sistema");

        // Agora eles vão chamar um método "exibirMenu()" que vamos criar dentro de cada View
        btnCategoria.addActionListener(e -> new CategoriaView().exibirMenu());
        btnCliente.addActionListener(e -> new ClienteView().exibirMenu());
        btnFornecedor.addActionListener(e -> new FornecedorView().exibirMenu());
        btnProduto.addActionListener(e -> new ProdutoView().exibirMenu());
        btnCompra.addActionListener(e -> new CompraView().exibirMenu());
        btnVenda.addActionListener(e -> new VendaView().exibirMenu());

        btnSair.addActionListener(e -> System.exit(0));

        add(btnCategoria);
        add(btnCliente);
        add(btnFornecedor);
        add(btnProduto);
        add(btnCompra);
        add(btnVenda);
        add(btnSair);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            TelaPrincipal menu = new TelaPrincipal();
            menu.setVisible(true);
        });
    }
}