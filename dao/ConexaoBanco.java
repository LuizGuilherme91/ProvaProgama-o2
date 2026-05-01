import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoBanco {
    
    // ATENÇÃO: Coloque o nome exato do arquivo .db que você criou
    private static final String URL = "jdbc:sqlite:BancoProg2.db";

    // O método precisa ser PUBLIC STATIC para o DAO conseguir acessá-lo diretamente
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            
            // Ativa as chaves estrangeiras do SQLite sempre que conectar
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            
            return conn;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados: " + e.getMessage(), e);
        }
    }
}