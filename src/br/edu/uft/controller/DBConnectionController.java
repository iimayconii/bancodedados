/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft.controller;

import br.edu.uft.banco.DAOFactory;
import br.edu.uft.banco.DAOUtil;

/**
 *
 * @author Maycon Costa
 */
public class DBConnectionController {
    DAOFactory connectionInstance;

    public DBConnectionController() {        
    }
    
    public void connect(String ip, String porta, String bancoDeDados,String username, String password){
        this.connectionInstance = DAOFactory.getInstance(ip, Integer.valueOf(porta), username, password, bancoDeDados);
    }
    
    public void disconnect(){
        //ainda a realizar        
    }    

    public DAOFactory getConnectionInstance() {
        return connectionInstance;
    }

    public void setConnectionInstance(DAOFactory connectionInstance) {
        this.connectionInstance = connectionInstance;
    }
    
}
