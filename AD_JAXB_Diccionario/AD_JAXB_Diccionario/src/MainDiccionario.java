
import java.io.File;
import java.util.List;
import java.util.Map;
import jaxb.diccionarioEspanol.DiccionarioEspanol;
import jaxb.diccionarioEspanol.ObjectFactory;
import jaxb.diccionarioEspanol.PalabraType;
import jaxb.diccionarioEspanol.SinonimoType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author aranx
 */
public class MainDiccionario {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        MetodosDiccionario metodos = new MetodosDiccionario();
        ObjectFactory factory = new ObjectFactory();
        DiccionarioEspanol diccionarioEspanol = factory.createDiccionarioEspanol(new File("diccionario.xml"), "jaxb.diccionarioEspanol");
        PalabraType palabra = factory.createPalabraType("HoLa");

        int cantidadDefiniciones = metodos.cantidadDefinicionesDeUnaPalabra(diccionarioEspanol, palabra);
        System.out.println("La palabra \"" + palabra.getGrafia() + "\" tiene " + cantidadDefiniciones + " definiciones.");

        // Borramos las traducciones de portugués PT
        metodos.borrarTraducciones(diccionarioEspanol, "PT");

        // Creamos de nuevo el objeto diccionario porque se habrán borrado las traducciones del portugués
        diccionarioEspanol = factory.createDiccionarioEspanol(new File("diccionario.xml"), "jaxb.diccionarioEspanol");
        Map<String, Integer> cantidadTraduccionesIdiomas = metodos.cantidadTraduccionesIdiomas(diccionarioEspanol);
        for (String idioma : cantidadTraduccionesIdiomas.keySet()) {
            System.out.println("IDIOMA: " + idioma + "  Traducciones: " + cantidadTraduccionesIdiomas.get(idioma));
        }

        Map<SinonimoType, List<String>> mapaSinonimos = metodos.mapearSinonimos(diccionarioEspanol, palabra);
        for (SinonimoType sinonimo : mapaSinonimos.keySet()) {
            System.out.println("SINÓNIMO: " + sinonimo.getGrafia() + "  Definiciones: ");
            for (String definicion : mapaSinonimos.get(sinonimo)) {
                System.out.println(definicion);
            }
        }
    }

}
