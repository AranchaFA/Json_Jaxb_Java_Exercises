
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import jaxb.clientes.Clientes;
import jaxb.clientes.ObjectFactory;
import jaxb.clientes.TipoDireccion;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author aranx
 */
public class MetodosJSON_Clientes {

    public static JsonArray leerFicheroJSON(File ficheroJSON) throws FileNotFoundException {
        FileReader entrada = new FileReader(ficheroJSON); // José lo tiene con String ruta, no el File
        JsonReader jsonReader = Json.createReader(entrada);
        JsonArray arrayJSON = jsonReader.readArray();
        return arrayJSON;
    }

    public static void crearFicheroJSON_AMano(String rutaFicheroACrear) throws IOException {
        File ficheroJASONCreado = new File(rutaFicheroACrear);

        // Creamos cada JSONObject que luego serán grabados en el fichero
        JsonObject cliente1 = Json.createObjectBuilder()
                .add("nombre", "Sara")
                .add("apellido", "García")
                .add("edad", 21)
                .add("direccion", Json.createObjectBuilder()
                        .add("calle", "Uría 12")
                        .add("ciudad", "Avilés")
                        .add("provincia", "Asturias")
                        .add("codigo", "33401")
                )
                .add("telefono", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("tipo", "casa")
                                .add("numero", "985-213344"))
                        .add(Json.createObjectBuilder()
                                .add("tipo", "fax")
                                .add("numero", "985-213355")))
                .build();

        JsonObject cliente2 = Json.createObjectBuilder()
                .add("nombre", "María")
                .add("apellido", "Pérez")
                .add("edad", 21)
                .add("direccion", Json.createObjectBuilder()
                        .add("calle", "Covadonga 2")
                        .add("ciudad", "Oviedo")
                        .add("provincia", "Asturias")
                        .add("codigo", "33011")
                )
                .add("telefono", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("tipo", "casa")
                                .add("numero", "984-213344"))
                        .add(Json.createObjectBuilder()
                                .add("tipo", "fax")
                                .add("numero", "984-213355")))
                .build();

        // Funciona perfectamente declarando el Builder, añadiendo objetos el array y haciendo el build() todo por separado
        // Podría emplearse en un bucle para cargar datos desde una lista de objetos pasada por parámetro
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        arrayBuilder.add(cliente1);
        arrayBuilder.add(cliente2);
        JsonArray arrayJSONClientes = arrayBuilder.build();

        // Grabamos el array con los JSONObjects en el fichero
        FileWriter ficheroSalida = new FileWriter(rutaFicheroACrear);
        JsonWriter jsonWriter = Json.createWriter(ficheroSalida);
        jsonWriter.writeArray(arrayJSONClientes);
        ficheroSalida.flush();
        ficheroSalida.close();
    }

    // APARTADO 1 : Crear un Json con datos (CON JAXB)
    // 1) 
    public static JsonArray crearDirecciones(List<TipoDireccion> listaDirecciones) {
        // Objeto para construir un array de JsonObjects
        JsonArrayBuilder arrayBuilder =Json.createArrayBuilder();
        for (Iterator<TipoDireccion> iterator = listaDirecciones.iterator(); iterator.hasNext();) {
            // Creamos un JsonObject de cada dirección de la lista de direcciones
            TipoDireccion direccion = iterator.next();
            JsonObject direccionJSON = Json.createObjectBuilder()
                .add("calle", direccion.getCalle() + " " + direccion.getNumero())
                .add("ciudad", direccion.getCiudad())
                .add("provincia", "Asturias") // Los objetos del bind no traen atributo provincia
                .add("codigo", direccion.getCp())
                .build();
            arrayBuilder.add(direccionJSON);
        }
        // Construimos el array con los JsonObjects cargados en el builder
        JsonArray arrayJsonDirecciones = arrayBuilder.build();

        return arrayJsonDirecciones;
    }
    // 2) 
    public static JsonObject crearCliente(Clientes.Cliente cliente) {
        JsonObject clienteJSON = Json.createObjectBuilder()
                .add("nombre", "NombreAquí") // Etiqueta vacía, da NullPointerException
                .add("apellido", cliente.getApellido().get(0) + " " + cliente.getApellido().get(1))
                .add("edad", 21) // No tiene edad el bind
                // Las direcciones son una lista
                .add("direccion", crearDirecciones(cliente.getDireccion()))
                .add("telefono", Json.createObjectBuilder()
                        .add("tipo", "casa") // No lo trae el objeto del bind
                        .add("numero", cliente.getTelefono()))
                .build();
        return clienteJSON;
    }
    // 3) 
    public static JsonArray crearListaClientes(Clientes clientes) {
        List<Clientes.Cliente> listaClientes = clientes.getCliente();
        JsonArrayBuilder arrayClientesJSON = Json.createArrayBuilder();
        for (Iterator<Clientes.Cliente> iterator = listaClientes.iterator(); iterator.hasNext();) {
            Clientes.Cliente clienteLeido = iterator.next();
            arrayClientesJSON.add(crearCliente(clienteLeido));
        }
        JsonArray listaClientesJSON = arrayClientesJSON.build();
        return listaClientesJSON;
    }

    // APARTADO 2 : Crear un JSON desde el XML
    // 4)
    public static JsonArray crearJSONCompleto(File ficheroXML, String nombrePaqueteJAXB) {
        ObjectFactory factory = new ObjectFactory();
        Clientes clientes = factory.createClientes(ficheroXML, nombrePaqueteJAXB);
        return crearListaClientes(clientes);
    }  
    // 5)
    public static File crearFicheroJSON(File ficheroXML,String nombrePaqueteJAXB) throws IOException{
        File ficheroJSON=new File("ficheroJSONPrueba");
        
        JsonArray jsonArray = crearJSONCompleto(ficheroXML, nombrePaqueteJAXB);
        
        FileWriter ficheroSalida = new FileWriter(ficheroJSON);
        JsonWriter jsonWriter = Json.createWriter(ficheroSalida);
        jsonWriter.writeArray(jsonArray);
        ficheroSalida.flush();
        ficheroSalida.close();
        
        return ficheroJSON;
    }
    
    // APARTADO 3 : Crear un XML desde el JSON
    // 6)
    /*
        <cliente>
		<apellido>String</apellido>
		<apellido>String</apellido>
		<direccion>
			<calle>String</calle>
			<numero>String</numero>
			<piso>0</piso>
			<escalera>c</escalera>
			<cp>11</cp>
			<ciudad>String</ciudad>
		</direccion>
		<direccion>
			<calle>String</calle>
			<numero>String</numero>
			<piso>0</piso>
			<escalera>a</escalera>
			<cp>12</cp>
			<ciudad>String</ciudad>
		</direccion>
		<telefono>String</telefono>
		<nombre/>
	</cliente>
    */
    
    public static List<TipoDireccion> crearDireccionesJAXB(JsonArray arrayDireccionesJSON){
        List<TipoDireccion> listaDireccionesJAXB=new ArrayList<>();
        // De cada JsonObject creamos un JAXBElement TipoDireccion y la añadimos a la lista
        for (int i = 0; i < arrayDireccionesJSON.size(); i++) {
            JsonObject direccionJSON = arrayDireccionesJSON.getJsonObject(i);
            String calle=direccionJSON.getString("calle");
            String ciudad=direccionJSON.getString("ciudad");
            //String provincia=direccionJSON.getString("provincia"); // En JAXB no tiene provincia
            int cp=Integer.valueOf(direccionJSON.getInt("codigo"));
            TipoDireccion direccionJAXB=new ObjectFactory().createTipoDireccion(calle,ciudad,cp);
            listaDireccionesJAXB.add(direccionJAXB);
        }        
        return listaDireccionesJAXB;
    }
    
    // Para el teléfono no tendremos método porque en JAXB no es un objeto, sino un String, 
    // porque no tiene tipo sólo número y además hay uno sólo y no una lista de teléfonos
    
    public static Clientes.Cliente crearClienteJAXB(JsonObject clienteJSON){       
        String nombre=clienteJSON.getString("nombre"); // Da NullPointerException porque en XML nombre es etiqueta vacía, hay que dejarlo sin settear en el constructor
        // ¿CÓMO PONER UNA ETIQUETA SIN CONTENIDO? AUNQUE NO TIENE MUCHO SENTIDO...
        String apellido = clienteJSON.getString("apellido");
        List<String> listaApellido=new ArrayList<>();
        listaApellido.add(apellido.substring(0, apellido.indexOf(" ")));
        listaApellido.add(apellido.substring(apellido.indexOf(" ")+1));
        // En JAXB no hay edad, así que no la incluimos
        // Sólo hay una dirección en JSON pero lo habíamos construido igualmente como un JsonArray
        JsonArray arrayDireccionesJSON = clienteJSON.getJsonArray("direccion"); 
        List<TipoDireccion> listaDirecciones = crearDireccionesJAXB(arrayDireccionesJSON);
        // En JAXB no hay una lista de teléfonos, sólo un teléfono String, y en JSON lo habíamos creado con JsonObject no JsonArray
        String telefono = clienteJSON.getJsonObject("telefono").getString("numero");       
        
        Clientes.Cliente clienteJAXB=new ObjectFactory().createClientesCliente(nombre,listaApellido,listaDirecciones,telefono);
        return clienteJAXB;
    }
    
    public static File crearFicheroXML(File ficheroJSON) throws FileNotFoundException {
        JsonArray arrayJsonClientes = leerFicheroJSON(ficheroJSON);
        List<Clientes.Cliente> listaClienteJAXB=new ArrayList<>();
        // Guardamos en la lista cada JsonObject transformado a JAXB
        for (int i = 0; i < arrayJsonClientes.size(); i++) {
            JsonObject clienteJSON = arrayJsonClientes.getJsonObject(i);
            Clientes.Cliente clienteJAXB = crearClienteJAXB(clienteJSON);
            listaClienteJAXB.add(clienteJAXB);
        }
        
        // Objeto JAXB correspondiente al nodo raíz del fichero XML
        Clientes clientesJAXB = new ObjectFactory().createClientes(listaClienteJAXB);
        // Marshalizamos
        File ficheroXML = marshalizar(clientesJAXB, "jaxb.clientes");
        
        return ficheroXML;
    }

   
    
    
    public static File marshalizar(Clientes clientesJAXB, String nombrePaqueteJAXB) {
        File ficheroXML = new File("ficheroPruebaXML.xml");
        try {
            // Objeto para manipular el contexto de nuestro árbol JAVA de clases sacadas del XML
            JAXBContext jaxbContext = JAXBContext.newInstance(nombrePaqueteJAXB);
            // Objeto marshaller
            Marshaller marshaller = jaxbContext.createMarshaller();
            // Le damos la propiedad de que genere el XML formateado (indentado, con saltos de líneas,...)
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Generamos el XML desde el elemento raíz
            marshaller.marshal(clientesJAXB, ficheroXML);
        } catch (JAXBException ex) {
            Logger.getLogger(MetodosJSON_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ficheroXML;
    }
}
