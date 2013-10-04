/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;

/**
 *
 * @author Maycon Costa
 */
public class Util {

    public static File showFileChooser() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(null);
    }

    public static File showSaveChooser() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showSaveDialog(null);
    }

    public static List<Integer> readerInputArchive(FileInputStream csv, String delimiter) {
        return reader(csv, delimiter);
    }

    public static List<Integer> reader(InputStream stream, String delimiter) {
        BufferedReader bin;
        List<Integer> data = new ArrayList<Integer>();
        try {
            bin = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String line;
            while ((line = bin.readLine()) != null) {
                String[] split = line.split(delimiter);
                for (String value : split) {
                    if (!value.isEmpty()) {
                        data.add(Integer.parseInt(value));
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
}
