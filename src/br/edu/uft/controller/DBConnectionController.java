/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft.controller;

import br.edu.uft.banco.DAOFactory;
import br.edu.uft.dao.BancoDAO;

/**
 *
 * @author Maycon Costa
 */
public class DBConnectionController {
    DAOFactory connectionInstance;
    BancoDAO bancoDAO;
    public DBConnectionController() {
        this.bancoDAO = new BancoDAO(connectionInstance);
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

    public BancoDAO getBancoDAO() {
        return bancoDAO;
    }

    public void setBancoDAO(BancoDAO bancoDAO) {
        this.bancoDAO = bancoDAO;
    }        
    
}
