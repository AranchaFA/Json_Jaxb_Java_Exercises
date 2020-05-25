/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import jaxb.clientes.Clientes;
import jaxb.clientes.TipoDireccion;

/**
 *
 * @author aranx
 */
public class LogicaClientes implements InterfaceClientes {

    @Override
    public JAXBElement unmarshalizar(File ficheroXML) throws JAXBException {
        JAXBElement jaxbElement = null;

        // Objeto para manipular el contexto de nuestro árbol JAVA de clases sacadas del XML
        JAXBContext jaxbContext = JAXBContext.newInstance("jaxb.clientes");

        // Objeto unmarshaller
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        // Objeto del elemento raíz
        jaxbElement = unmarshaller.unmarshal(new StreamSource(ficheroXML), Clientes.class);

        return jaxbElement;
    }

    @Override
    public File marshalizar(JAXBElement jaxbElement) throws JAXBException {
        File ficheroXML = null;

        // Objeto para manipular el contexto de nuestro árbol JAVA de clases sacadas del XML
        JAXBContext jaxbContext = JAXBContext.newInstance("jaxb.clientes");

        // Objeto marshaller
        Marshaller marshaller = jaxbContext.createMarshaller();

        // Le damos la propiedad de que genere el XML formateado (indentado, con saltos de líneas,...)
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Fichero XML a crear
        ficheroXML = new File(jaxbElement.getName() + ".xml");

        // Generamos el XML desde el elemento raíz
        marshaller.marshal(jaxbElement, ficheroXML);

        return ficheroXML;
    }

    // Esto iría en el ObjectFactory
    public Clientes generarObjetoClientes(File ficheroXML) throws JAXBException{
        return (Clientes) unmarshalizar(ficheroXML).getValue();
    }
    
    @Override
    public int totalClientes(Clientes clientes) {
        // Sacamos la lista de cliente y retornamos su size
        return clientes.getCliente().size();
    }

    @Override
    public int totalClientesProvincia(Clientes clientes, String codigoPostalProvincia) {
        // Sacamos la lista de clientes y contabilizamos los que su CP empieza por los 2 dígitos pasados
        int contador = 0;
        List<Clientes.Cliente> listaCliente = clientes.getCliente();
        for (Clientes.Cliente cliente : listaCliente) {
            List<TipoDireccion> listaDireccionCliente = cliente.getDireccion();
            for (TipoDireccion direccion : listaDireccionCliente) {
                if (String.valueOf(direccion.getCp()).startsWith(codigoPostalProvincia)) {
                    // Contaría varias veces un cliente con varias direcciones en la misma provincia
                    contador++;
                }
            }
        }
        return contador;
    }

    @Override
    public List<Clientes.Cliente> borrarCliente(Clientes clientes, List<String> apellido) {
        List<Clientes.Cliente> borrados = new ArrayList<Clientes.Cliente>();

        List<Clientes.Cliente> listaCliente = clientes.getCliente();
        for (Clientes.Cliente cliente : listaCliente) {
            List<String> listaApellido = cliente.getApellido();
            if (listaApellido.containsAll(apellido)) {
                borrados.add(cliente);
            }
        }
        listaCliente.removeAll(borrados);

        return borrados;
    }

    @Override
    public Clientes.Cliente anhadirCliente(Clientes clientes, Clientes.Cliente cliente) {
        return clientes.getCliente().add(cliente) ? cliente : null;
    }

    @Override
    public TipoDireccion anhadirDireccion(Clientes clientes, Clientes.Cliente cliente, TipoDireccion direccionNueva) {
        boolean anhadida = false;
        List<Clientes.Cliente> listaCliente = clientes.getCliente();
        for (Clientes.Cliente clienteLeido : listaCliente) {
            if (clienteLeido.getApellido().containsAll(cliente.getApellido())) { // No entiendo muy bien lo de lenguaje, y nombre es etiqueta vacía 0.o
                anhadida = clienteLeido.getDireccion().add(direccionNueva);
            }
        }
        return anhadida ? direccionNueva : null;
    }

    @Override
    public List<TipoDireccion> modificarDirecciones(Clientes clientes, Clientes.Cliente cliente, List<TipoDireccion> direccionesModificadas) {
        boolean anhadida = false;
        List<TipoDireccion> direccionesAntiguas = null;
        List<Clientes.Cliente> listaCliente = clientes.getCliente();
        for (Clientes.Cliente clienteLeido : listaCliente) {
            if (clienteLeido.getApellido().containsAll(cliente.getApellido())) { // No entiendo muy bien lo de lenguaje, y nombre es etiqueta vacía 0.o
                direccionesAntiguas = clienteLeido.getDireccion();
                clienteLeido.getDireccion().removeAll(direccionesAntiguas);
                anhadida = clienteLeido.getDireccion().addAll(direccionesModificadas);
            }
        }
        return anhadida ? direccionesModificadas : null;
    }

    @Override
    public Map<Clientes.Cliente, List<TipoDireccion>> borrarDireccionesSinCP(Clientes clientes) {
        Map<Clientes.Cliente, List<TipoDireccion>> mapDireccionesBorradas = new HashMap<>();
        List<Clientes.Cliente> listaCliente = clientes.getCliente();

        for (Clientes.Cliente cliente : listaCliente) {
            List<TipoDireccion> direccionesCliente = cliente.getDireccion();
            List<TipoDireccion> direccionesABorrar = new ArrayList<>();

            for (TipoDireccion direccion : direccionesCliente) {
                if (direccion.getCp() == 0) {
                    direccionesABorrar.add(direccion);
                }
            }

            mapDireccionesBorradas.put(cliente, direccionesABorrar);
            direccionesCliente.removeAll(direccionesABorrar);
        }

        return mapDireccionesBorradas;
    }

    @Override
    public File generarFicheroHTML5(Clientes clientes) throws IOException {
        File ficheroHTML5 = new File(clientes.getClass().getSimpleName() + "HTML5.html");

        FileWriter fw = new FileWriter(ficheroHTML5);
        BufferedWriter bw = new BufferedWriter(fw);
        FileReader fr = new FileReader(new File("baseHTML5.txt"));
        BufferedReader br = new BufferedReader(fr);

        String lineaLeida = br.readLine();
        while (lineaLeida != null) {
            if (lineaLeida.contains("<title>")) {
                lineaLeida = "\n<title>" + ficheroHTML5.getName() + "</title>\n";
            }
            if (lineaLeida.contains("<body>")) {
                lineaLeida += "\n<clientes>";
                for (Clientes.Cliente cliente : clientes.getCliente()) {
                    lineaLeida += generarNodoCliente(cliente);
                }
                lineaLeida += "</clientes>\n";
            }
            bw.write(lineaLeida);
            bw.flush();
            bw.newLine();
            lineaLeida = br.readLine();
        }

        return ficheroHTML5;
    }

    public String generarNodoCliente(Clientes.Cliente cliente) {
        String nodoCliente = "<cliente>\n";
        for (String apellido : cliente.getApellido()) {
            nodoCliente += "<apellido>" + apellido + "</apellido>\n";
        }
        for (TipoDireccion direccion : cliente.getDireccion()) {
            nodoCliente += "<direccion>\n<calle>" + direccion.getCalle() + "</calle>\n"
                    + "<numero>" + direccion.getNumero() + "</numero>\n"
                    + "<piso>" + direccion.getPiso() + "</piso>\n"
                    + "<escalera>" + direccion.getEscalera() + "</escalera>\n"
                    + "<cp>" + direccion.getCp() + "</cp>\n"
                    + "<ciudad>" + direccion.getCiudad() + "</ciudad>\n</direccion>\n";
        }
        nodoCliente += "</cliente>\n";

        return nodoCliente;
    }

}
