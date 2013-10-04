/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft.banco;

import br.com.labsystem.dao.exception.DAOConfiguracaoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Maycon
 */
public abstract class DAOFactory {
   
    public static DAOFactory getInstance(String ip, Integer port, String username, String password, String banco) {
        String url = "jdbc:postgresql://"+ip+":"+port+"/"+banco;
        String driverClassName = "org.postgresql.Driver";        
        DAOFactory instance;
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new DAOConfiguracaoException(
                    "O driver '" + driverClassName + "' está faltando.", e);
        }
        instance = new DriverManagerDAOFactory(url, username, password, banco);
        return instance;
    }

    public abstract Connection getConnection() throws SQLException;
    public abstract String getDatabase();
}

class DriverManagerDAOFactory extends DAOFactory {

    private String url;
    private String username;
    private String password;
    private String database;

    DriverManagerDAOFactory(String url, String username, String password, String database) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    @Override
    public Connection getConnection() throws SQLException {        
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }                
}
