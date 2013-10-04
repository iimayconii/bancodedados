/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author maycon
 */
public class BancoController extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("appView.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

//        File file = new File("C:\\Users\\Maycon Costa\\Videos\\Biblia_ddl_JFARC.sql");
//        BufferedReader reader = new BufferedReader(new FileReader(file));
//        String line = "";
//        String newSql = "";
//
//        boolean comentary = false;        
//        while ((line = reader.readLine()) != null) {
//            StringTokenizer tokens = new StringTokenizer(line);
//            while (tokens.hasMoreTokens()) {
//                String token = tokens.nextToken();
//                if (token.contains("--")) {                    
//                    break;
//                }                
//                if (token.contains("/*") && !comentary) {
//                    comentary = true;                    
//                }
//                if (!comentary) {
//                    newSql = newSql + token + " ";
//                }
//                if (token.contains("*/") && comentary) {                    
//                    comentary = false;
//                }
//
//            }
//        }
//        System.out.println("Resultado");
//        System.out.println(newSql);
//        
//        String[] split = newSql.split(";");
//        
//        for(String comando : split){
//            System.out.println("-----------------");
//            System.out.println(comando);
//            System.out.println("-----------------");
//        }

//        List<String> comandos = Arrays.asList("DROP TABLE IF EXISTS Testamento, Apocalipse",
//                "drop teste", "drop table if exists teste", "drop table if exists");
////        for(String comando : comandos){
////            try{
////                dropTable(comando);
////            }catch(IllegalStateException e){
////                System.out.println("Comando errado");
////            }
////        }
//
//        String tester = "drop table arroz";
//        String tester2 = "drop table arroz, google";
//        String regular = "drop table if existis ([^\\s]+)";
//
//        Pattern r = Pattern.compile(regular);
//        Matcher m = r.matcher(tester);
//        if(m.matches()){
//            System.out.println("ok");
//        }

    }

    //DROP TABLE [ IF EXISTS ] name [, ...]
    //DROP TABLE IF EXISTS Testamento, Apocalipse = true
    //drop table teste = true
    //drop table if exists teste = true
    //drop testamento = false
    //drop table if exists = false
    public static void dropTable(String sql) throws IllegalStateException {
        sql = sql.toLowerCase();
        int position = 0;
        StringTokenizer tokens = new StringTokenizer(sql);
        boolean optionIf = false;
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if (position == 1 && !token.equalsIgnoreCase("table")) {
                throw new IllegalStateException("O comando drop está errado");
            }
            if (position == 2 && token.equalsIgnoreCase("if")) {
                optionIf = true;
            }
            if (optionIf && position == 3 && !token.equalsIgnoreCase("exists")) {
                throw new IllegalStateException("O comando drop está errado");
            }
            position++;
        }
        String tabelas = sql.toLowerCase()
                .replace("drop", "")
                .replace("table", "")
                .replace("if", "")
                .replace("exists", "");

        if (tabelas.isEmpty()) {
            throw new IllegalStateException("O comando drop está errado");
        }
        String[] tabelasList = tabelas.split(",");
        System.out.println("Tabelas a serem deletadas");
        for (String tabela : tabelasList) {
            System.out.println(tabela);
        }
    }

    /**
     * CREATE [ [ GLOBAL | LOCAL ] { TEMPORARY | TEMP } | UNLOGGED ] TABLE [ IF
     * NOT EXISTS ] table_name ( [ { column_name data_type [ COLLATE collation ]
     * [ column_constraint [ ... ] ] | table_constraint | LIKE parent_table [
     * like_option ... ] } [, ... ] ] )
     */
    public static void createTable(String sql) {
        sql = sql.toLowerCase();
        StringTokenizer tokens = new StringTokenizer(sql);
        String tokensRead = "";
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            int position = 0;

            tokensRead += " " + token;
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}