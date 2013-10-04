/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft.controller;

/**
 *
 * @author Maycon Costa
 */
public class BancoController {
    DBConnectionController dBConnectionController;

    public BancoController() {
    }        

    public DBConnectionController getdBConnectionController() {
        return dBConnectionController;
    }

    public void setdBConnectionController(DBConnectionController dBConnectionController) {
        this.dBConnectionController = dBConnectionController;
    }
    
    
}
