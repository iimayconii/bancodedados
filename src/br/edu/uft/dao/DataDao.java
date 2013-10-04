/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft.dao;

import br.com.labsystem.dao.exception.DAOException;
import br.edu.uft.banco.DAOFactory;
import br.edu.uft.banco.DAOUtil;
import br.edu.uft.model.Column;
import br.edu.uft.model.Table;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Maycon Costa
 */
public class DataDao {
    
    private static final String BUSCAR_TODOS =
            "SELECT * FROM ";       
    
    private DAOFactory daoFactory;
    
    private Table tabela;

    public DataDao(DAOFactory daoFactory, Table tabela) {
        this.daoFactory = daoFactory;
        this.tabela = tabela;
    }            
   
    public List<Map<String, String>> findAll() throws DAOException {
        Connection conexao = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        List<Map<String, String>> dados = new ArrayList<>();
        try {
            conexao = daoFactory.getConnection();
            preparedStatement = conexao.prepareStatement(BUSCAR_TODOS + tabela.getName());
            System.out.println(preparedStatement);           
            result = preparedStatement.executeQuery();
            while (result.next()) {                
                dados.add(getObject(result));
            }            
        } catch (SQLException e) {            
            throw new DAOException(e);
        } finally {            
            DAOUtil.close(conexao, preparedStatement, result);
            return dados;
        }       
    }
            
    public Map<String, String> getObject(ResultSet result) throws SQLException {
        Map<String, String> dado = new HashMap<>();
        for(Column column : tabela.getColumns()){
            dado.put(column.getNome(), String.valueOf(result.getObject(column.getNome())));
        }                
        return dado;
    }
}
