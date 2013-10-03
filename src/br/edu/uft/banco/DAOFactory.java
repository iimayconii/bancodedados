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
        instance = new DriverManagerDAOFactory(url, username, password);
        return instance;
    }

    abstract Connection getConnection() throws SQLException;
}

class DriverManagerDAOFactory extends DAOFactory {

    private String url;
    private String username;
    private String password;

    DriverManagerDAOFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
