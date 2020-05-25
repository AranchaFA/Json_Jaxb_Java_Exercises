
import Logica.LogicaClientes;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import jaxb.clientes.Clientes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author aranx
 */
public class MainClientes2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Logica.LogicaClientes logica = new LogicaClientes();

        try {
            // El objeto JAXBElement contiene el 'equivalente' al nodo raíz del XML, pero para
            // trabajar con él tenemos que CASTEARLO a nuestra clase Clientes
            Clientes clientes=logica.generarObjetoClientes(new File("clientes.xml"));
            
            // Para 

        } catch (JAXBException ex) {
            Logger.getLogger(MainClientes2.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }

}
