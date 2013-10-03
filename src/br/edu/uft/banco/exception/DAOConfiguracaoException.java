/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.labsystem.dao.exception;

/**
 *
 * @author Maycon
 */
public class DAOConfiguracaoException extends RuntimeException{
   
    public DAOConfiguracaoException(String message) {
        super(message);
    }

    public DAOConfiguracaoException(Throwable cause) {
        super(cause);
    }

    public DAOConfiguracaoException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
