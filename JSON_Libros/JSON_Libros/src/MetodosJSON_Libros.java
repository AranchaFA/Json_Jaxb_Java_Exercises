
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author aranx
 */
public class MetodosJSON_Libros {

    // Crear un fichero con la forma del fichero libros.json
    public static void crearFicheroJSON(String rutaFicheroACrear) throws IOException {
        File ficheroJASONCreado = new File(rutaFicheroACrear);

        // Creamos cada JSONObject que luego serán grabados en el fichero
        JsonObject libro1 = Json.createObjectBuilder()
                .add("titulo", "Sueños IA")
                .add("totalPaginas", 210)
                .add("precio", 10)
                .add("autores",
                        Json.createArrayBuilder()
                                .add(Json.createObjectBuilder()
                                        .add("nombre", "Javier")
                                        .add("apellido", "Pérez"))
                                .add(Json.createObjectBuilder()
                                        .add("nombre", "María")
                                        .add("apellido", "Rodríguez"))
                )
                .add("generos",
                        Json.createArrayBuilder()
                                .add(Json.createObjectBuilder()
                                        .add("genero", "novela"))
                                .add(Json.createObjectBuilder()
                                        .add("genero","ficción")))
                .build();

        JsonObject libro2 = Json.createObjectBuilder()
                .add("titulo", "JSON para todos")
                .add("totalPaginas", 310)
                .add("precio", 20)
                .add("autores",
                        Json.createArrayBuilder()
                                .add(Json.createObjectBuilder()
                                        .add("nombre", "Ana")
                                        .add("apellido", "Cota"))
                                .add(Json.createObjectBuilder()
                                        .add("nombre", "Mar")
                                        .add("apellido", "Fernández"))
                )
                .add("generos",
                        Json.createArrayBuilder()
                                .add(Json.createObjectBuilder()
                                        .add("genero", "informática"))
                                .add(Json.createObjectBuilder()
                                        .add("genero","ficción")))
                .build();

        // Componemos un array con los objetos a grabar
        JsonArray arrayJSONLibros = Json.createArrayBuilder()
                .add(libro1)
                .add(libro2)
                .build();

        // Grabamos el array con los JSONObjects en el fichero
        FileWriter ficheroSalida = new FileWriter(rutaFicheroACrear);
        JsonWriter jsonWriter = Json.createWriter(ficheroSalida);
        jsonWriter.writeArray(arrayJSONLibros);
        ficheroSalida.flush();
        ficheroSalida.close();

    }
    
    public static JsonArray leerFicheroJSON(File ficheroJSON) throws FileNotFoundException{
        FileReader entrada = new FileReader(ficheroJSON); // José lo tiene con String ruta, no el File
        JsonReader jsonReader = Json.createReader(entrada);
        JsonArray arrayJSON = jsonReader.readArray();
        return arrayJSON; 
    }

    public static int totalLibros(File ficheroJSON) throws FileNotFoundException{
        return leerFicheroJSON(ficheroJSON).size();
    }
    
    public static void mostrarTitulos(File ficheroJSON) throws FileNotFoundException{
        List<String> titulos=new ArrayList<>();
        JsonArray jsonArray = leerFicheroJSON(ficheroJSON);
        for (int i = 0; i < jsonArray.size(); i++) {
            String tituloLibroLeido = jsonArray.getJsonObject(i).getString("titulo");
            titulos.add(tituloLibroLeido);
        }
        for (String titulo : titulos) {
            System.out.println(titulo);
        }
    }
    
    public static String autorDeUnLibro(File ficheroJSON,int posicionLibro,int posicionAutor) throws FileNotFoundException{
        JsonArray jasonArray = leerFicheroJSON(ficheroJSON);
        String nombreAutor = jasonArray.getJsonObject(posicionLibro).getJsonArray("autores").getJsonObject(posicionAutor).getString("nombre");
        return nombreAutor;
    }
    
    public static double sumaPrecios(File ficheroXML) throws FileNotFoundException{
        double precios=0;
        
        JsonArray jsonArray = leerFicheroJSON(ficheroXML);
        for (int i = 0; i < jsonArray.size(); i++) {
            precios+=jsonArray.getJsonObject(i).getJsonNumber("precio").doubleValue();
        }
        
        return precios;
    }
}
