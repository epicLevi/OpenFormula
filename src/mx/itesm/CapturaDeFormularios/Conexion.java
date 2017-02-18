package mx.itesm.CapturaDeFormularios;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Conexion implements Serializable {

    private static String ipAddress;
    private static String dbName;
    private static String user;
    private static String password;
    private static String service;
    private static ResourceBundle dbProperties;
    
    public static Connection getconexion() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Drive");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        
        if (dbProperties == null) {
            dbProperties    = ResourceBundle.getBundle("config");
            ipAddress       = dbProperties.getString("ip_address");
            dbName          = dbProperties.getString("db_name");
            user            = dbProperties.getString("user");
            password        = dbProperties.getString("password");
            service         = dbProperties.getString("service");
        }
        
        // This is the "connection" line.
        return DriverManager.getConnection("jdbc:mysql://" + ipAddress + ":" + 
                service + "/" + dbName, 
                user, password);
    }
    
    public static void main(String[] args) throws SQLException {
        if( Conexion.getconexion() != null )
            System.out.println("Se conectó exitosamente ala base de datos.");
        else
            System.out.println("No se conectó a la base de datos.");
    }
}