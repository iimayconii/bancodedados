/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft.controller;

import br.edu.uft.banco.DAOFactory;
import br.edu.uft.dao.BancoDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author maycon
 */
public class ConnectionController implements Initializable {

    @FXML
    private TextField bancoDeDados;
    @FXML
    private TextField host;
    @FXML
    private Label label;
    @FXML
    private PasswordField password;
    @FXML
    private TextField porta;
    @FXML
    private TextField usuario;
    
    //Controladores
    DBConnectionController dBConnectionController;

    @FXML
    void conectar(ActionEvent event) {
        dBConnectionController.connect(host.getText(), porta.getText(), bancoDeDados.getText(),
                usuario.getText(), password.getText());
        new BancoDAO(dBConnectionController.connectionInstance).listTables();
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dBConnectionController = new DBConnectionController();
    }
}
