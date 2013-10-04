/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft;

import br.edu.uft.controller.DBConnectionController;
import br.edu.uft.dao.BancoDAO;
import br.edu.uft.util.Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Maycon Costa
 */
public class SqlEditorController extends Pane {

    @FXML
    private TextArea sqlArea;
    Stage stage;
    DBConnectionController dBConnectionController;

    public SqlEditorController(Stage stage, DBConnectionController dBConnectionController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/br/edu/uft/sqlEditor.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            this.stage = stage;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.dBConnectionController = dBConnectionController;
    }

    @FXML
    void executar(ActionEvent event) {
        try {
            BufferedReader reader = new BufferedReader(new StringReader(sqlArea.getText()));
            boolean comentary = false;
            String line = "";
            String newSql = "";
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokens = new StringTokenizer(line);
                while (tokens.hasMoreTokens()) {
                    String token = tokens.nextToken();
                    if (token.contains("--")) {
                        break;
                    }
                    if (token.contains("/*") && !comentary) {
                        comentary = true;
                    }
                    if (!comentary) {
                        newSql = newSql + token + " ";
                    }
                    if (token.contains("*/") && comentary) {
                        comentary = false;
                    }
                }
            }
            try {
                new BancoDAO(dBConnectionController.getConnectionInstance()).executeQuery(newSql);
            } catch (SQLException ex) {
                Logger.getLogger(SqlEditorController.class.getName()).log(Level.SEVERE, null, ex);              
                JOptionPane.showMessageDialog(null, "Error ao executar a sql");
            }
        } catch (IOException ex) {
            Logger.getLogger(SqlEditorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void limpar(ActionEvent event) {
        sqlArea.clear();
    }

    @FXML
    void selecionarSQL(ActionEvent event) {
        sqlArea.clear();
        File file = Util.showFileChooser();
        if (file != null) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                String line = "";
                while ((line = br.readLine()) != null) {
                    sqlArea.appendText(line + "\n");
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SqlEditorController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SqlEditorController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(SqlEditorController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
