package lk.ijse.dep10.erpSystem.db;

import javafx.scene.control.Alert;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection dbConnection;
    private final Connection connection;

    private DBConnection() {
        try {

            StringBuilder sb = new StringBuilder("jdbc:mysql://");
            Properties properties = new Properties();
            FileReader fr=new FileReader("application.properties");
            properties.load(fr);
            fr.close();
            String host=properties.getProperty("team_shark_erp.host","127.0.0.1");
            String port=properties.getProperty("team_shark_erp.port","3306");
            String database=properties.getProperty("team_shark_erp.database","erp_system_db");
            String user=properties.getProperty("team_shark_erp.user","root");
            String password=properties.getProperty("team_shark_erp.password","");
            sb.append(host).append(":").append(port).append("/").append(database).append("?createDatabaseIfNotExist=true&allowMultiQueries=true");
            connection =DriverManager.getConnection(sb.toString(),user,password);



        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Failed to connect with the database please try again...").showAndWait();
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Fail to locate application.properties file...").showAndWait();
            throw new RuntimeException(e);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"Fail to locate application.properties file...").showAndWait();
            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }

    public static DBConnection getInstance() {
        return dbConnection==null?dbConnection= new DBConnection():dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
