package myutil;

import java.sql.*;

public class MySQLUtil {
    public static boolean checkServerConnection() {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/pads?useSSL=false&characterEncoding=utf8&useServerPrepStmts=true",
                "root", "");
            conn.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
