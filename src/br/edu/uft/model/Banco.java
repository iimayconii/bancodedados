/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft.model;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Maycon Costa
 */
public class Banco {
    public String name;
    public HashMap<String, Table> tables;

    public Banco() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Table> getTables() {
        return tables;
    }

    public void setTables(HashMap<String, Table> tables) {
        this.tables = tables;
    }            

    @Override
    public String toString() {
        return name;
    }   
    
}
