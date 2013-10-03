/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.labsystem.dao;

import br.com.labsystem.dao.exception.DAOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Maycon
 */
public interface DAOInterface<T, ID> {

    public T find(ID id) throws DAOException;
    
    public T find(String query, Object... valores) throws DAOException;

    public List<T> findAll() throws DAOException;

    public T create(T entity) throws IllegalArgumentException, DAOException;

    public T update(T entity) throws IllegalArgumentException, DAOException;

    public T delete(T entity) throws DAOException;
    
    public T getObject(ResultSet result) throws SQLException;
}
