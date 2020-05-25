
import java.io.File;
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

        // Crear fichero JSON
        try {
            MetodosJSON_Libros.crearFicheroJSON("ficheroPruebaJSON");
            File ficheroJSON=new File("ficheroPruebaJSON");
            System.out.println("TOTAL LIBROS: "+MetodosJSON_Libros.totalLibros(ficheroJSON));
            MetodosJSON_Libros.mostrarTitulos(ficheroJSON);
            System.out.println(MetodosJSON_Libros.autorDeUnLibro(ficheroJSON, 0, 1));
            System.out.println("SUMA PRECIOS :"+MetodosJSON_Libros.sumaPrecios(ficheroJSON));
        } catch (IOException ex) {
            Logger.getLogger(MainPruebas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
