package main.java.cn.xxq.tools.exportdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.jdbc.JdbcUtils;

import main.java.cn.xxq.tools.flyway.FlywayService;

/**
 * @author HEX145
 * @date 2019年9月25日
 */
public class ExportDbData {

    /**
     * @param flyway
     * @param dbType
     */
    private static void exportBaseData(Flyway flyway, String dbType) {
        Connection connect = JdbcUtils.openConnection(flyway.getConfiguration().getDataSource(),
            flyway.getConfiguration().getConnectRetries());
        String sql = "";
        try {
            Statement statement = connect.createStatement();
            ResultSet result =
                statement.executeQuery("select TABLE_NAME,FILE_NAME,SELECT_SQL,INSERT_SQL,NEED_DELETE,DELETE_SQL "
                    + "from BIZ_PUB_TABLE_BASE_DATA where db_type='" + dbType + "';");
            while (result.next()) {
                String tableName = result.getString("TABLE_NAME");
                String fileName = result.getString("FILE_NAME");
                String selectSql = result.getString("SELECT_SQL");
                String insertSql = result.getString("INSERT_SQL");
                String needDelete = result.getString("NEED_DELETE");
                String deleteSql = result.getString("DELETE_SQL");
                exportToFile(tableName, fileName, selectSql, insertSql, needDelete, deleteSql);
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeConnection(connect);
        }
    }

    public static void exportToFile(String tableName, String fileName, String selectSql, String insertSql,
        String needDelete, String deleteSql) {

    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://172.16.101.221:3306/userportal";
        String user = "root";
        String password = "root";
        FlywayService service = new FlywayService(url, user, password);
        exportBaseData(service.getFlyway(), "mysql");
    }
}
