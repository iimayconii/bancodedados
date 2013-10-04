/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft;

import br.edu.uft.controller.ConnectionController;
import br.edu.uft.dao.BancoDAO;
import br.edu.uft.dao.DataDao;
import br.edu.uft.model.Banco;
import br.edu.uft.model.Column;
import br.edu.uft.model.Table;
import br.edu.uft.util.Util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author maycon
 */
public class appViewController implements Initializable {
    
    @FXML
    private TreeView dataBases;
    @FXML
    private TableView columnTable;
    ConnectionController connection;
    @FXML
    private TabPane tabPane;
    @FXML
    private TableView<Map> dataTable;

    @FXML
    void conectar(ActionEvent event) {
        Stage stage = new Stage();
        connection = new ConnectionController(stage);
        Scene scene = new Scene(connection);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        dataBases.setRoot(treeItemRoot(connection.getDatabase()));
    }

    @FXML
    void showSQLEditor(ActionEvent event) {
        if (connection.getDatabase() != null) {
            Stage stage = new Stage();
            SqlEditorController sqlEditor = new SqlEditorController(stage,
                    connection.getdBConnectionController());
            Scene scene = new Scene(sqlEditor);
            stage.setScene(scene);
            stage.show();
        }else{
            JOptionPane.showMessageDialog(null, "Você não conectou em um banco de dados!");
        }
    }

    public TreeItem<String> treeItemRoot(Banco banco) {
        ImageView rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/br/edu/uft/database.png")));
        rootIcon.setScaleX(0.5);
        rootIcon.setScaleY(0.5);
        TreeItem<String> rootNode = new TreeItem<>(banco.getName(), rootIcon);
        rootNode.setExpanded(true);
        for (Table tabela : banco.getTables().values()) {
            ImageView tabelaIcon = new ImageView(new Image(getClass().getResourceAsStream("/br/edu/uft/datatable.png")));
            tabelaIcon.setScaleX(0.5);
            tabelaIcon.setScaleY(0.5);
            TreeItem<String> treeItem = new TreeItem<>(tabela.getName(), tabelaIcon);
            rootNode.getChildren().add(treeItem);
        }
        return rootNode;
    }

    public final void updateTable(String tableName) {
        if (connection.getDatabase().getTables().containsKey(tableName)) {
            columnTable.getItems().clear();
            columnTable.getItems().addAll(connection.getDatabase().getTables().get(tableName)
                    .getColumns());
        }
    }

    public void createHeaderTable(Table table) {
        dataTable.getColumns().clear();
        for (Column column : table.getColumns()) {
            TableColumn tableColumn = new TableColumn(column.getNome());
            tableColumn.setCellValueFactory(new MapValueFactory(column.getNome()));
            tableColumn.setPrefWidth(45.0);
            dataTable.getColumns().add(tableColumn);
        }
    }

    @FXML
    void exportarCSV(ActionEvent event) {
        File file = Util.showSaveChooser();
        if (file != null) {
            BufferedWriter wr = null;
            try {
                Table get = connection.getDatabase().getTables().get((String) ((TreeItem) dataBases.getSelectionModel().getSelectedItem()).getValue());
                List<Map<String, String>> reload = reload(get);
                wr = new BufferedWriter(new FileWriter(file));
                for (Map<String, String> dado : reload) {
                    Iterator<Column> iterator = get.getColumns().iterator();
                    while (iterator.hasNext()) {
                        Column col = iterator.next();
                        if (dado.containsKey(col.getNome())) {
                            if (col.getTipo().equalsIgnoreCase("character varying")) {
                                wr.write("\"" + dado.get(col.getNome()) + "\"");
                            } else {
                                wr.write(dado.get(col.getNome()));
                            }
                        }
                        if (iterator.hasNext()) {
                            wr.write(",");
                        }
                    }
                    wr.write("\n");
                }
            } catch (IOException ex) {
                Logger.getLogger(appViewController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    wr.close();
                } catch (IOException ex) {
                    Logger.getLogger(appViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    void importarCSV(ActionEvent event) {
        File file = Util.showFileChooser();
        if (file != null) {
            try {
                Table get = connection.getDatabase().getTables().get((String) ((TreeItem) dataBases.getSelectionModel().getSelectedItem()).getValue());
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = "";
                while ((line = br.readLine()) != null) {
                    String[] split = line.split(",");
                    if (get.getColumns().size() == split.length) {
                        System.out.println(Arrays.deepToString(split));
                        BancoDAO dataDao = new BancoDAO(connection.getdBConnectionController().getConnectionInstance());
                        dataDao.insert(get, split);
                    } else {
                        System.out.println("erro, csv não tem a quantidade de colunas certas");
                        break;
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(appViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(appViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    void reloadData(ActionEvent event) {
        Table get = connection.getDatabase().getTables().get((String) ((TreeItem) dataBases.getSelectionModel().getSelectedItem()).getValue());
        List<Map<String, String>> reload = reload(get);
        System.out.println(Arrays.deepToString(reload.toArray()));
        dataTable.getItems().clear();
        dataTable.getItems().addAll(reload);
    }

    public List<Map<String, String>> reload(Table table) {
        DataDao dataDao = new DataDao(connection.getdBConnectionController().getConnectionInstance(),
                table);
        return dataDao.findAll();
    }

    public void clearTable() {
        columnTable.getItems().clear();
    }

    public void createColumns() {
        TableColumn columnName = new TableColumn("Nome");
        columnName.setCellValueFactory(new PropertyValueFactory("nome"));
        TableColumn columnTipo = new TableColumn("Tipo");
        columnTipo.setCellValueFactory(new PropertyValueFactory("tipo"));
        TableColumn columnTamanho = new TableColumn("Tamanho");
        columnTamanho.setCellValueFactory(new PropertyValueFactory("tamanho"));
        TableColumn columnNulo = new TableColumn("Nulo");
        columnNulo.setCellValueFactory(new PropertyValueFactory("nulo"));
        TableColumn columnPrimaryKey = new TableColumn("Primary Key");
        columnPrimaryKey.setCellValueFactory(new PropertyValueFactory("primaryKey"));
        TableColumn columnDescricao = new TableColumn("Descrição");
        columnDescricao.setCellValueFactory(new PropertyValueFactory("descricao"));
        columnTable.getColumns().addAll(columnName, columnTipo, columnTamanho,
                columnNulo, columnPrimaryKey, columnDescricao);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataBases.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object oldValue, Object newValue) {
                tabPane.getSelectionModel().select(0);
                updateTable((String) ((TreeItem) newValue).getValue());
            }
        });
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> tab, Tab oldTab, Tab newTab) {
                if (newTab.getText().equalsIgnoreCase("dados")) {
                    if (!dataBases.getSelectionModel().getSelectedItems().isEmpty()) {
                        Table get = connection.getDatabase().getTables().get((String) ((TreeItem) dataBases.getSelectionModel().getSelectedItem()).getValue());
                        List<Map<String, String>> reload = reload(get);
                        createHeaderTable(get);
                        System.out.println(Arrays.deepToString(reload.toArray()));
                        dataTable.getItems().clear();
                        dataTable.getItems().addAll(reload);
                    }
                }
            }
        });
        createColumns();
    }
}
