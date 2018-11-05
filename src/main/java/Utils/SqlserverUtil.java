package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author WXY
 * @version 创建时间：2018/11/1
 */
public class SqlserverUtil {
    public static Connection getSqlserverConnectionResult(String ip, String passwd) {
        String URL = "jdbc:sqlserver://" + ip + ":1433";
        String USER = "sa";
        Connection conn = null;

        try {
            //1.加载驱动程序
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //2.获得数据库的连接
            conn = DriverManager.getConnection(URL, USER, passwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
