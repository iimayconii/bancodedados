/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft.dao;

import br.com.labsystem.dao.exception.DAOException;
import br.edu.uft.banco.DAOFactory;
import br.edu.uft.banco.DAOUtil;
import br.edu.uft.model.Banco;
import br.edu.uft.model.Column;
import br.edu.uft.model.Table;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maycon Costa
 */
public class BancoDAO {

    private DAOFactory daoFactory;
    public String SELECT_COLUMNS_OF_TABLE = "SELECT \n"
            + "  columns.data_type AS tipo, \n"
            + "  columns.column_name AS nome, \n"
            + "  columns.ordinal_position AS posicao, \n"
            + "  columns.is_nullable AS nulo, \n"
            + "  columns.character_maximum_length AS tamanho\n"
            + "FROM \n"
            + "  information_schema.columns\n"
            + "WHERE \n"
            + "  columns.table_name LIKE ?;";
    public String SELECT_DESCRIPTION_OF_TABLE = "select pc.relname as tablename, "
            + "pa.attname as column, pd.description as comentario \n"
            + "from pg_description pd, pg_class pc, pg_attribute pa  \n"
            + "where pc.relname = ?  \n"
            + "and pa.attname = ? \n"
            + "and pa.attrelid = pc.oid  \n"
            + "and pd.objoid = pc.oid  \n"
            + "and pd.objsubid = pa.attnum order by 1,2; ";
    public String SELECT_PRIMARY_KEY = "SELECT  t.table_name as tabela,\n"
            + "        kcu.constraint_name as nome,\n"
            + "        kcu.column_name as coluna,\n"
            + "        tc.constraint_type as tipo\n"
            + "FROM    INFORMATION_SCHEMA.TABLES t\n"
            + "         LEFT JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc\n"
            + "                 ON tc.table_catalog = t.table_catalog\n"
            + "                 AND tc.table_schema = t.table_schema\n"
            + "                 AND tc.table_name = t.table_name                 \n"
            + "                 AND tc.constraint_type = ? \n"
            + "         LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu\n"
            + "                 ON kcu.table_catalog = tc.table_catalog\n"
            + "                 AND kcu.table_schema = tc.table_schema\n"
            + "                 AND kcu.table_name = tc.table_name\n"
            + "                 AND kcu.constraint_name = tc.constraint_name\n"
            + "WHERE   t.table_schema NOT IN ('pg_catalog', 'information_schema')"
            + " and t.table_name = ? and kcu.column_name = ?;";
    Connection c;

    public BancoDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public void insert(Table table, String[] data) {
        String sqlInsert = "INSERT INTO ?(?) VALUES (?);";
        String columns = "";
        String values = "";
        Iterator<Column> iterator = table.getColumns().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Column col = iterator.next();
            columns += col.getNome();
            values += data[index++];
            if (iterator.hasNext()) {
                columns += ",";
                values += ", ";
            }
        }
        sqlInsert = sqlInsert.replaceFirst("\\?", table.getName()).replaceFirst("\\?", columns)
                .replaceFirst("\\?", values).replaceAll("\"", "'");
        System.out.println(sqlInsert);
        executeSql(sqlInsert);
    }

    public void executeQuery(String sql) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultado = null;
        connection = daoFactory.getConnection();        
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        DAOUtil.close(connection, preparedStatement, resultado);

    }

    public void executeSql(String sql) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultado = null;
        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            int linhasAfetadas = preparedStatement.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new DAOException("Falha ao inserir no banco, nenhuma linha foi agetada.");
            }
            resultado = preparedStatement.getGeneratedKeys();
        } catch (SQLException ex) {
            Logger.getLogger(BancoDAO.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            DAOUtil.close(connection, preparedStatement, resultado);
        }
    }

    public Banco getTable() {
        Connection conexao = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        Banco banco = new Banco();
        HashMap<String, Table> tabelas = new HashMap<>();
        try {
            conexao = getConnection();
            preparedStatement = conexao.prepareStatement("SELECT schemaname AS esquema, "
                    + "tablename AS tabela, tableowner AS dono"
                    + " FROM pg_catalog.pg_tables"
                    + " WHERE schemaname NOT IN ('pg_catalog', 'information_schema', 'pg_toast')"
                    + " ORDER BY schemaname, tablename");
            result = preparedStatement.executeQuery();
            while (result.next()) {
                Table tabela = new Table();
                tabela.setName(result.getString("tabela"));
                tabela.setColumns(listTableColumn(result.getString("tabela")));
                tabela.setBanco(banco);
                tabelas.put(tabela.getName(), tabela);
            }
            banco.setTables(tabelas);
            banco.setName(daoFactory.getDatabase());
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            DAOUtil.close(conexao, preparedStatement, result);
        }
        return banco;
    }

    public List<Column> listTableColumn(String tableName) {
        Connection conexao = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        List<Column> colunas = new ArrayList<>();
        try {
            conexao = daoFactory.getConnection();
            preparedStatement = conexao.prepareStatement(SELECT_COLUMNS_OF_TABLE);
            preparedStatement.setString(1, tableName);
            result = preparedStatement.executeQuery();
            while (result.next()) {
                Column column = new Column();
                column.setNome(result.getString("nome"));
                column.setNulo(result.getBoolean("nulo"));
                column.setPosicao(result.getInt("posicao"));
                column.setPrimaryKey(false);
                column.setPrimaryKeyName(getConstraint("PRIMARY KEY", tableName, result.getString("nome")));
                column.setTamanho(result.getInt("tamanho"));
                column.setTipo(result.getString("tipo"));
                column.setDescricao(getComments(tableName, result.getString("nome")));
                colunas.add(column);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            DAOUtil.close(preparedStatement, result);
        }
        return colunas;
    }

    public String getComments(String tableName, String columnName) {
        Connection conexao = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String comentario = "";
        try {
            conexao = daoFactory.getConnection();
            preparedStatement = conexao.prepareStatement(SELECT_DESCRIPTION_OF_TABLE);
            preparedStatement.setString(1, tableName);
            preparedStatement.setString(2, columnName);
            result = preparedStatement.executeQuery();
            while (result.next()) {
                comentario = result.getString("comentario");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            DAOUtil.close(preparedStatement, result);
        }
        return comentario;
    }

    public String getConstraint(String tipo, String tableName, String columnName) {
        Connection conexao = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String nome = "";
        try {
            conexao = daoFactory.getConnection();
            preparedStatement = conexao.prepareStatement(SELECT_PRIMARY_KEY);
            preparedStatement.setString(1, tipo);
            preparedStatement.setString(2, tableName);
            preparedStatement.setString(3, columnName);
            result = preparedStatement.executeQuery();
            while (result.next()) {
                nome = result.getString("nome");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            DAOUtil.close(preparedStatement, result);
        }
        return nome;
    }

    private Connection getConnection() throws SQLException {
        if (c == null) {
            c = daoFactory.getConnection();
        }
        if (c.isClosed()) {
            c = daoFactory.getConnection();
        }
        return c;
    }
}
