/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft.controller;

import br.edu.uft.banco.DAOFactory;
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

    @FXML
    void conectar(ActionEvent event) {
        DAOFactory instance = DAOFactory.getInstance(host.getText(), Integer.parseInt(porta.getText()), usuario.getText(),
                                      password.getText(), bancoDeDados.getText());
        instance.get
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {       
    }
}
