package Utils;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author WXY
 * @version 创建时间：2018/11/1
 */
public class PostgresqlUtil {
    private final static Logger logger = Logger.getLogger(PostgresqlUtil.class);

    public static Connection getPostgresqlConnectionAndTest(String ip, String passwd) {
        String driver = "org.postgresql.Driver";  //获取mysql数据库的驱动类
        String url = "jdbc:postgresql://" + ip + ":5432/postgres";
        String name = "postgres";
        String pwd = passwd;

        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, name, pwd);//获取连接对象
            if (!conn.isClosed()) {
                logger.info("Postgresql connection success");
                return conn;
            } else {
                logger.info("Postgresql connection failed");
                return null;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

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

    public static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
