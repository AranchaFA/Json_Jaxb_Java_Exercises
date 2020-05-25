
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import jaxb.diccionarioEspanol.DiccionarioEspanol;
import jaxb.diccionarioEspanol.ObjectFactory;
import jaxb.diccionarioEspanol.PalabraType;
import jaxb.diccionarioEspanol.SinonimoType;
import jaxb.diccionarioEspanol.TraduccionType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author aranx
 */
public class MetodosDiccionario implements InterfaceDiccionario {

    private String nombrePaqueteJAXB = "jaxb.diccionarioEspanol";

    @Override
    public File marshalizar(JAXBElement jaxbElement, String nombrePaqueteJAXB) {
        File ficheroXML = new File(jaxbElement.getName() + ".xml");
        try {
            // Objeto para manipular el contexto de nuestro árbol JAVA de clases sacadas del XML
            JAXBContext jaxbContext = JAXBContext.newInstance(nombrePaqueteJAXB);
            // Objeto marshaller
            Marshaller marshaller = jaxbContext.createMarshaller();
            // Le damos la propiedad de que genere el XML formateado (indentado, con saltos de líneas,...)
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Generamos el XML desde el elemento raíz
            marshaller.marshal(jaxbElement, ficheroXML);
        } catch (JAXBException ex) {
            Logger.getLogger(MetodosDiccionario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ficheroXML;
    }

    @Override
    public JAXBElement unmarshalizar(File ficheroXML, String nombrePaqueteJAXB) {
        JAXBElement jaxbElement = null;
        try {
            // Objeto para manipular el contexto de nuestro árbol JAVA de clases sacadas del XML
            JAXBContext jaxbContext = JAXBContext.newInstance(nombrePaqueteJAXB);
            // Objeto unmarshaller
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            // Objeto del elemento raíz
            jaxbElement = unmarshaller.unmarshal(new StreamSource(ficheroXML), DiccionarioEspanol.class);
        } catch (JAXBException ex) {
            Logger.getLogger(MetodosDiccionario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jaxbElement;
    }

    @Override
    public int cantidadDefinicionesDeUnaPalabra(DiccionarioEspanol diccionarioEspanol, PalabraType palabra) {
        for (PalabraType palabraLeida : diccionarioEspanol.getPalabra()) {
            if (palabraLeida.getGrafia().equalsIgnoreCase(palabra.getGrafia())) {
                return palabraLeida.getDefinicion().size();
            }
        }
        return 0;
    }

    @Override
    public File borrarTraducciones(DiccionarioEspanol diccionarioEspanol, String idiomaTraduccion) {
        File ficheroXMLModificado = new File(diccionarioEspanol.getClass().getSimpleName() + "Sin" + idiomaTraduccion + ".xml");
        // El objeto DiccionarioEspanol del parámetro se modifica, luego en el main al ejecutar los siguientes
        // métodos lo hace sobre el objeto modificado y no sobre un original
        ObjectFactory factoria = new ObjectFactory();

        for (Iterator iteratorPalabras = diccionarioEspanol.getPalabra().iterator(); iteratorPalabras.hasNext();) {
            PalabraType palabra = (PalabraType) iteratorPalabras.next();
            for (Iterator iteratorTraducciones = palabra.getTraducciones().getTraduccion().iterator(); iteratorTraducciones.hasNext();) {
                TraduccionType traduccion = (TraduccionType) iteratorTraducciones.next();
                if (traduccion.getIdiomaTraduccion().equalsIgnoreCase(idiomaTraduccion)) {
                    iteratorTraducciones.remove();
                }
            }
        }
        // El objeto diccionarioEspanol no lo admite en el método marshalizar porque es de nuestro tipo concreto de JAXBElement :(
        // Pondríamos el parámetro de los métodos como Object y no como JAXBElement?
        try {
            // Objeto para manipular el contexto de nuestro árbol JAVA de clases sacadas del XML
            JAXBContext jaxbContext = JAXBContext.newInstance(this.nombrePaqueteJAXB);
            // Objeto marshaller
            Marshaller marshaller = jaxbContext.createMarshaller();
            // Le damos la propiedad de que genere el XML formateado (indentado, con saltos de líneas,...)
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Generamos el XML desde el elemento raíz
            marshaller.marshal(diccionarioEspanol, ficheroXMLModificado);
        } catch (JAXBException ex) {
            Logger.getLogger(MetodosDiccionario.class.getName()).log(Level.SEVERE, null, ex);
        }

        // JAXBElement jaxbElement=new JAXBElement(DiccionarioEspanol.class.getName() DiccionarioEspanol.class, diccionarioEspanol);
        // jaxbElement.setValue(diccionarioEspanol);
        return ficheroXMLModificado;
    }

    @Override
    public Map<String, Integer> cantidadTraduccionesIdiomas(DiccionarioEspanol diccionarioEspanol) {
        Map<String, Integer> mapTraducciones = new HashMap<>();
        for (Iterator iteratorPalabras = diccionarioEspanol.getPalabra().iterator(); iteratorPalabras.hasNext();) {
            PalabraType palabra = (PalabraType) iteratorPalabras.next();
            for (Iterator iteratorTraducciones = palabra.getTraducciones().getTraduccion().iterator(); iteratorTraducciones.hasNext();) {
                TraduccionType traduccion = (TraduccionType) iteratorTraducciones.next();
                // Si ya existe ese idioma en el map, sumamos 1 al total de traducciones
                if (mapTraducciones.containsKey(traduccion.getIdiomaTraduccion())) {
                    // int totalTraducciones = mapTraducciones.get(traduccion.getIdiomaTraduccion())+1;
                    mapTraducciones.put(traduccion.getIdiomaTraduccion(), mapTraducciones.get(traduccion.getIdiomaTraduccion()) + 1);
                } else {
                    // Si no existe ese idioma en el map, inicializamos a 1 el total de traducciones
                    mapTraducciones.put(traduccion.getIdiomaTraduccion(), 1);
                }
            }
        }

        return mapTraducciones;
    }

    @Override
    public Map<SinonimoType, List<String>> mapearSinonimos(DiccionarioEspanol diccionarioEspanol, PalabraType palabra) {
        Map<SinonimoType, List<String>> mapSinonimos = new HashMap<>();

        for (PalabraType palabraLeida : diccionarioEspanol.getPalabra()) {
            if (palabraLeida.getGrafia().equalsIgnoreCase(palabra.getGrafia())) {
                for (SinonimoType sinonimo : palabraLeida.getSinonimos().getSinonimo()) {
                    PalabraType sinonimoPalabraType = new ObjectFactory().createPalabraType(sinonimo.getGrafia());
                    List<String> listaDefiniciones = listarDefinicionesPalabra(diccionarioEspanol, sinonimoPalabraType);
                    if (listaDefiniciones!=null) {
                        mapSinonimos.put(sinonimo,listaDefiniciones);
                    } else {
                        List<String> listaVacia=new ArrayList<>();
                        listaVacia.add("No se han encontrado definiciones");
                        mapSinonimos.put(sinonimo,listaVacia);
                    }
                    
                }
                // Así no sigue iterando el resto de la colección, para en cuanto encuentra la palabra
                return mapSinonimos;
            }
        }
        return mapSinonimos;
    }

    public List<String> listarDefinicionesPalabra(DiccionarioEspanol diccionarioEspanol, PalabraType palabra) {
        for (PalabraType palabraLeida : diccionarioEspanol.getPalabra()) {
            if (palabraLeida.getGrafia().equalsIgnoreCase(palabra.getGrafia())) {
                return palabraLeida.getDefinicion();
            }
        }
        return null;
    }

}
