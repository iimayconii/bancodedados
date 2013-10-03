/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft.dao;

import br.com.labsystem.dao.exception.DAOException;
import br.edu.uft.banco.DAOFactory;
import br.edu.uft.banco.DAOUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Maycon Costa
 */
public class BancoDAO {

    private DAOFactory daoFactory;
    
    public String SELECT_COLUMNS_OF_TABLE = "SELECT a.attnum " +
",a.attname AS name " +
",format_type(a.atttypid, a.atttypmod) AS typ " +
",a.attnotnull AS notnull " +
",coalesce(p.indisprimary, FALSE) AS primary_key " +
",f.adsrc AS default_val " +
",d.description AS col_comment " +
"FROM pg_attribute a " +
"LEFT JOIN pg_index p ON p.indrelid = a.attrelid AND a.attnum = ANY(p.indkey) " +
"LEFT JOIN pg_description d ON d.objoid  = a.attrelid AND d.objsubid = a.attnum " +
"LEFT JOIN pg_attrdef f ON f.adrelid = a.attrelid  AND f.adnum = a.attnum " +
"WHERE a.attnum > 0 " +
"AND NOT a.attisdropped " +
"AND a.attrelid = ?::regclass " +
"ORDER BY a.attnum;";

    public BancoDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public void listTables() {
        Connection conexao = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            conexao = daoFactory.getConnection();
            preparedStatement = conexao.prepareStatement("SELECT schemaname AS esquema, "
                    + "tablename AS tabela, tableowner AS dono"
                    + " FROM pg_catalog.pg_tables"
                    + " WHERE schemaname NOT IN ('pg_catalog', 'information_schema', 'pg_toast')"
                    + " ORDER BY schemaname, tablename");
            result = preparedStatement.executeQuery();
            while (result.next()) {
                System.out.println(result.getString("tabela"));
                System.out.println("------------------------");
                System.out.println("Tabelas");
                System.out.println("------------------------");
                listTableColumn(result.getString("tabela"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            DAOUtil.close(conexao, preparedStatement, result);
        }
    }
    
    public void listTableColumn(String tableName) {
        Connection conexao = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            conexao = daoFactory.getConnection();
            preparedStatement = conexao.prepareStatement(SELECT_COLUMNS_OF_TABLE);
            preparedStatement.setString(1, "public."+tableName+"");
            System.out.println(preparedStatement.toString());
            result = preparedStatement.executeQuery();
            while (result.next()) {
                System.out.println("\t\t" + result.getString("name"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            DAOUtil.close(conexao, preparedStatement, result);
        }
    }

}
