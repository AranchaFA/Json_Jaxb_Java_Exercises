
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author aranx
 */
public class MainPruebas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            File ficheroJSON = MetodosJSON_Web.crearFicheroJSON("ficheroPruebaJSON", true);
            MetodosJSON_Web.mostrarElevacion(ficheroJSON);
            MetodosJSON_Web.mostrarResolucion(ficheroJSON);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainPruebas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainPruebas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
