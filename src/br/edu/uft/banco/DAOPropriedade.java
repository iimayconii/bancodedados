/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.uft.banco;

import br.com.labsystem.dao.exception.DAOConfiguracaoException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Maycon
 */
public class DAOPropriedade {
    private static final String ARQUIVO_DE_PROPRIEDADE = "/br/edu/uft/banco/bd.properties";
    private static final Properties CONFIGURACAO = new Properties();    
    
    public String getPropriedade(String prop){
        String propriedade = CONFIGURACAO.getProperty(prop);
        if(propriedade == null || propriedade.trim().isEmpty()){
            throw new DAOConfiguracaoException("Propriedade '"+ prop +"'obrigatoria "
                    + "está faltando no arquivo de configuração");
        }
        return propriedade;
    }
}
