package Utils;


import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author WXY
 * @version 创建时间：2018/10/31
 */
public class MysqlUtil {
    private final static Logger logger = Logger.getLogger(MysqlUtil.class);

    public static Connection getConnectionAndTest(String ip, String passwd) throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";  //获取mysql数据库的驱动类
        String url = String.format("jdbc:mysql://%s:3306/", ip); //连接数据库
        String name = "root";//连接mysql的用户名
        String pwd = passwd;//连接mysql的密码

        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, name, pwd);//获取连接对象
        if (!conn.isClosed()) {
            logger.info("Test connection success");
            return conn;
        } else {
            logger.info("Test connection failed");
            return null;
        }


    }

    public static int getConnectionAndQueryResult(String ip, String passwd, String sql) throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";  //获取mysql数据库的驱动类
        String url = String.format("jdbc:mysql://%s:3306/", ip); //连接数据库
        String name = "root";//连接mysql的用户名
        String pwd = passwd;//连接mysql的密码

        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, name, pwd);//获取连接对象
        Statement smt = conn.createStatement();
        int i = smt.executeUpdate(sql);

        if (!conn.isClosed()) {
            if (i == 1) {
                logger.info("Test connection success");
            }
            return i;
        } else {
            logger.info("Test connection failed");
            return 0;
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
