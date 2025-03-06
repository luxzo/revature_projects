package controller;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionController {

    //Config variable to load application.properties file
    private static final Config config = ConfigFactory.load("application.properties");

    /*
    * This method retrieves data from application.properties file in order to set the attributes for Connection
    * So it is not necessary to hard code database info.
    */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                config.getString("db.url"),
                config.getString("db.username"),
                config.getString("db.password")
        );
    }
}
