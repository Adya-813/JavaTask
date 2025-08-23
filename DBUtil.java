import java.sql.Connection; import java.sql.DriverManager; import java.sql.SQLException;

public class DBUtil { private static final String URL = "jdbc:mysql://localhost:3306/digital_lib?useSSL=false&serverTimezone=UTC"; private static final String USER = "root"; // <-- change private static final String PASSWORD = "your_password"; // <-- change

static {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        System.err.println("MySQL JDBC Driver not found. Add the driver to classpath.");
        e.printStackTrace();
    }
}

public static Connection getConnection() throws SQLException {
    String PASSWORD = null;
    return DriverManager.getConnection(URL, USER, PASSWORD);
}

}

