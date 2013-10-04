/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft.controller;

import br.edu.uft.dao.BancoDAO;
import br.edu.uft.model.Banco;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author maycon
 */
public final class ConnectionController extends Pane {

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
    private Banco database;
    
    DBConnectionController dBConnectionController;
    Stage stage;

    public ConnectionController(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/br/edu/uft/appConnect.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            this.stage = stage;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        dBConnectionController = new DBConnectionController();
    }

    @FXML
    void conectar(ActionEvent event) {
        dBConnectionController.connect(host.getText(), porta.getText(), bancoDeDados.getText(),
                usuario.getText(), password.getText());
        this.database = new BancoDAO(dBConnectionController.connectionInstance).getTable();
        this.stage.close();
    }

    public TextField getBancoDeDados() {
        return bancoDeDados;
    }

    public void setBancoDeDados(TextField bancoDeDados) {
        this.bancoDeDados = bancoDeDados;
    }

    public TextField getHost() {
        return host;
    }

    public void setHost(TextField host) {
        this.host = host;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public PasswordField getPassword() {
        return password;
    }

    public void setPassword(PasswordField password) {
        this.password = password;
    }

    public TextField getPorta() {
        return porta;
    }

    public void setPorta(TextField porta) {
        this.porta = porta;
    }

    public TextField getUsuario() {
        return usuario;
    }

    public void setUsuario(TextField usuario) {
        this.usuario = usuario;
    }

    public DBConnectionController getdBConnectionController() {
        return dBConnectionController;
    }

    public void setdBConnectionController(DBConnectionController dBConnectionController) {
        this.dBConnectionController = dBConnectionController;
    }

    public Banco getDatabase() {
        return database;
    }

    public void setDatabase(Banco database) {
        this.database = database;
    }
 
}
