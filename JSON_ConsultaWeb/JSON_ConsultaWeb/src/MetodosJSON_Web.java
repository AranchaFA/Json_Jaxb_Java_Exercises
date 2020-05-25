
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
public class MetodosJSON_Web {

    // ESTE DEVUELVE JSONOBJECT PORQUE LA CONSULTA DEVOLVERÁ UN ÚNICO OBJETO, NO UN ARRAY DE ELLOS
    public static JsonObject leerFicheroJSON(File ficheroJSON) throws FileNotFoundException {
        FileReader entrada = new FileReader(ficheroJSON); // José lo tiene con String ruta, no el File
        JsonReader jsonReader = Json.createReader(entrada);
        JsonObject arrayJSON = jsonReader.readObject();
        return arrayJSON;
    }

    public static File crearFicheroJSON(String rutaFicheroACrear,boolean operacionExitosa) throws IOException {
        File ficheroJSON = new File(rutaFicheroACrear);
        JsonObject consultaWeb;
        
        // Creamos el JsonObject del resultado en función de si ha habido o no error en la consulta
        if (operacionExitosa) {
            consultaWeb = Json.createObjectBuilder()
                .add("results", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("elevation", 1608.6379)
                                .add("location", Json.createObjectBuilder()
                                        .add("lat", 39.73)
                                        .add("lng", -104.98)
                                )
                                .add("resolution", 4.77)
                        )
                )
                .add("status", "OK")
                .build();
        } else {
            consultaWeb=Json.createObjectBuilder()
                    .add("error_message", "Invalid request. Invalid locations parameters.")
                    .add("results", Json.createArrayBuilder()
                            .addNull()
                    )
                    .add("status", "REQUEST_DENIED")
                    .build();
        }
        
        // Grabamos el array con los JSONObjects en el fichero
        FileWriter ficheroSalida = new FileWriter(rutaFicheroACrear);
        JsonWriter jsonWriter = Json.createWriter(ficheroSalida);
        jsonWriter.writeObject(consultaWeb);
        ficheroSalida.flush();
        ficheroSalida.close();

        return ficheroJSON;
    }
    
    public static double mostrarElevacion(File ficheroJSON) throws FileNotFoundException{
        double elevacion=0;
        
        JsonObject jsonObject = leerFicheroJSON(ficheroJSON);
        if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
            elevacion=jsonObject.getJsonArray("results").getJsonObject(0).getJsonNumber("elevation").doubleValue();
            System.out.println("ELEVACIÓN :"+elevacion);
        }
        
        return elevacion;
    }
    
    public static double mostrarResolucion(File ficheroJSON) throws FileNotFoundException{
        double resolucion=0;
        
        JsonObject jsonObject = leerFicheroJSON(ficheroJSON);
        if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
            resolucion = jsonObject.getJsonArray("results").getJsonObject(0).getJsonNumber("resolution").doubleValue();
            System.out.println("RESOLUCIÓN :"+resolucion);
        }
        
        return resolucion;
    }
}
