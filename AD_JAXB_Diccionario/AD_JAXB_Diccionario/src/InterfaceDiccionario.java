
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import jaxb.diccionarioEspanol.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aranx
 */
public interface InterfaceDiccionario {   
    public File marshalizar(JAXBElement jaxbElement,String nombrePaqueteJAXB);
    public JAXBElement unmarshalizar(File ficheroXML, String nombrePaqueteJAXB);
    public int cantidadDefinicionesDeUnaPalabra(DiccionarioEspanol diccionarioespanol,PalabraType palabra);
    public File borrarTraducciones(DiccionarioEspanol diccionarioEspanol,String idiomatraduccion);
    public Map<String,Integer> cantidadTraduccionesIdiomas(DiccionarioEspanol diccionarioEspanol); 
    public Map<SinonimoType, List<String>> mapearSinonimos(DiccionarioEspanol diccionarioEspanol,PalabraType palabra);
}
