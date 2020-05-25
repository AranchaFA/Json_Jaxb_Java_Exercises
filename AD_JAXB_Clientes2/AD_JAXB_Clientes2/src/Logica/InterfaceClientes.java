/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import jaxb.clientes.Clientes;
import jaxb.clientes.TipoDireccion;

/**
 *
 * @author aranx
 */
public interface InterfaceClientes {
    
    public JAXBElement unmarshalizar(File ficheroXML) throws JAXBException;
    
    public File marshalizar(JAXBElement jaxbElement) throws JAXBException;
    
    public int totalClientes(Clientes clientes);
    
    public int totalClientesProvincia(Clientes clientes,String codigoPostalProvincia);
    
    public List<Clientes.Cliente> borrarCliente(Clientes clientes,List<String> apellido);
    
    public Clientes.Cliente anhadirCliente(Clientes clientes,Clientes.Cliente cliente);
    
    public TipoDireccion anhadirDireccion(Clientes clientes,Clientes.Cliente cliente,TipoDireccion direccionNueva);
    
    public List<TipoDireccion> modificarDirecciones(Clientes clientes,Clientes.Cliente cliente,List<TipoDireccion> direccionesModificadas);
    
    public Map<Clientes.Cliente,List<TipoDireccion>> borrarDireccionesSinCP(Clientes clientes);
    
    public File generarFicheroHTML5(Clientes clientes)  throws IOException;
    
}
