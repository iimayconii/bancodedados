/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft.controller;

import br.edu.uft.model.Banco;

/**
 *
 * @author Maycon Costa
 */
public interface BancoInterface {
    public void dropTable(String table, boolean ifExists);
    public void deleteDB(String db);
    public void createTable(Banco banco);
    //public void insert
}
