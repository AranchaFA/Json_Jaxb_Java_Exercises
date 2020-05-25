
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.converter.BigDecimalStringConverter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import jaxb.agenda.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author aranx
 */
public class MetodosAgenda {

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
            Logger.getLogger(MetodosAgenda.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ficheroXML;
    }

    public JAXBElement unmarshalizar(File ficheroXML, String nombrePaqueteJAXB) {
        JAXBElement jaxbElement = null;
        try {
            // Objeto para manipular el contexto de nuestro árbol JAVA de clases sacadas del XML
            JAXBContext jaxbContext = JAXBContext.newInstance(nombrePaqueteJAXB);
            // Objeto unmarshaller
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            // Objeto del elemento raíz
            jaxbElement = unmarshaller.unmarshal(new StreamSource(ficheroXML), Agenda.class);
        } catch (JAXBException ex) {
            Logger.getLogger(MetodosAgenda.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jaxbElement;
    }

    public AlarmaType anhadirAlarma(Agenda agenda, AlarmaType alarma) {
        AlarmaType alarmaAnhadida = agenda.getAlarmas().getAlarma().add(alarma) ? alarma : null;
        return alarmaAnhadida;
    }

    public String anhadirTelefono(Agenda agenda, ContactoType contacto, String telefono) {
        String telefonoAnhadido = null;
        for (ContactoType contactoLeido : agenda.getContactos().getContacto()) {
            if (contactoLeido.getNombre().getValue().equalsIgnoreCase(contacto.getNombre().getValue())) {
                telefonoAnhadido = contactoLeido.getTelefono().add(telefono) ? telefono : null;
            }
        }
        return telefonoAnhadido;
    }

    public Map<String, String> mostrarYContarCorreos(Agenda agenda) {
        Map<String, String> mapAsuntoYRemitente = new HashMap<>();

        Agenda.Correos correos = agenda.getCorreos();
        CorreoType correo = correos.getCorreo();
        mapAsuntoYRemitente.put(correo.getAsunto(), correo.getRemitente());
        for (String asunto : mapAsuntoYRemitente.keySet()) {
            System.out.println("Asunto: " + asunto + "   Remitente: " + mapAsuntoYRemitente.get(asunto));
        }
        System.out.println("Total correos: " + mapAsuntoYRemitente.size());

        return mapAsuntoYRemitente;
    }

    public Map<ContactoType, List<String>> buscarTelefonos(Agenda agenda, ContactoType contacto) {
        Map<ContactoType, List<String>> mapTelefonos = new HashMap<>();
        for (ContactoType contactoLeido : agenda.getContactos().getContacto()) {
            if (contactoLeido.getNombre().getValue().equalsIgnoreCase(contacto.getNombre().getValue())) {
                mapTelefonos.put(contacto, contactoLeido.getTelefono());
            }
        }
        return mapTelefonos;
    }

    public ContactoType borrarContacto(Agenda agenda, ContactoType contacto) throws ContactoDuplicadoException {
        ContactoType contactoABorrar = null;
        int contadorEncontrados = 0;

        for (ContactoType contactoLeido : agenda.getContactos().getContacto()) {
            if (contactoLeido.getNombre().getValue().equalsIgnoreCase(contacto.getNombre().getValue())) {
                contactoABorrar = contactoLeido;
                contadorEncontrados++;
                if (contadorEncontrados > 1) {
                    throw new ContactoDuplicadoException("¡Se ha encontrado más de un contacto con ese nombre!");
                }
            }
        }

        return contactoABorrar;
    }

    public Map<String, Integer> contarTotalEntradas(Agenda agenda) {
        Map<String, Integer> mapTotalEntradas = new HashMap<>();

        mapTotalEntradas.put("alarmas", agenda.getAlarmas().getAlarma().size());
        mapTotalEntradas.put("correos", 1);
        mapTotalEntradas.put("contactos", agenda.getContactos().getContacto().size());

        return mapTotalEntradas;
    }

    public List<AlarmaType> alarmasPendientes(Agenda agenda, Date fecha) {
        List<AlarmaType> listaAlarmasPendientes = new ArrayList<>();

        for (AlarmaType alarmaLeida : agenda.getAlarmas().getAlarma()) {
            if (fechaAlarmaADate(alarmaLeida).after(fecha)) {
                listaAlarmasPendientes.add(alarmaLeida);
            }
        }
        return listaAlarmasPendientes;
    }

    public Date fechaAlarmaADate(AlarmaType alarma) {
        DiaType diaLeido = alarma.getDiaHora().getDia();
        HoraType horaLeida = alarma.getDiaHora().getHora();
        Date fechaDate = new Date();
        fechaDate.setYear(diaLeido.getAño() - 1900);
        fechaDate.setMonth(diaLeido.getMes() - 1);
        fechaDate.setDate(diaLeido.getDia());
        fechaDate.setHours(horaLeida.getHora());
        fechaDate.setMinutes(horaLeida.getMinuto());
        String segundoLeidoString = new BigDecimalStringConverter().toString(horaLeida.getSegundo());
        segundoLeidoString = segundoLeidoString.substring(0, segundoLeidoString.length() - 2);
        fechaDate.setSeconds(Integer.valueOf(segundoLeidoString));
        
        return fechaDate;
    }

    public class ContactoDuplicadoException extends Exception {

        public ContactoDuplicadoException() {
        }

        public ContactoDuplicadoException(String string) {
            super(string);
        }

    }
}
