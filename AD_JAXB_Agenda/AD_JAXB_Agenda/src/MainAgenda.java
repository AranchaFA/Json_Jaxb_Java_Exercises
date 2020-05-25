
import java.io.File;
import java.util.Date;
import java.util.List;
import jaxb.agenda.Agenda;
import jaxb.agenda.AlarmaType;
import jaxb.agenda.ObjectFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aranx
 */
public class MainAgenda {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        MetodosAgenda metodos=new MetodosAgenda();
        ObjectFactory factory=new ObjectFactory();
        
        Agenda agenda = factory.createAgenda(new File("agenda.xml"), "jaxb.agenda");
        
        Date fechaSistema=new Date();
        List<AlarmaType> alarmasPendientes = metodos.alarmasPendientes(agenda, new Date());
        for (AlarmaType alarmaPendiente : alarmasPendientes) {
            System.out.println("Año:"+alarmaPendiente.getDiaHora().getDia().getAño());
        }
        
    }
    
}
