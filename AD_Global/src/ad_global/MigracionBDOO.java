
package ad_global;

import org.hibernate.SessionFactory;
import Modelo.*;
import java.io.File;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
/**
 *
 * @author Jairo
 */
public class MigracionBDOO {
    public static void main(String[] args) {
        borrarFichero();
        //Crease Hibernate session
        //creamos la sesión consulta Hibernate
        SessionFactory sesionGeneral = SessionFactorySingleton.getSessionFactory();
        Session sesionActual = sesionGeneral.openSession();
        
        MigracionBDOO programa = new MigracionBDOO();
        programa.iniciar(sesionActual);

    }
    /**
     * Delete the file in database for when we start the program.
     * Borra el fichero de la base de datos para cuando iniciemos el programa
     */
    public static void borrarFichero(){
        File f = new File("C://users//Yleve//Desktop//Employees.Neodatis");
        if(f.exists())
            f.delete();
    }
    /**
     * Execute the program.
     * Inicia la ejecucion del programa
     * @param sesionActual objeto Session para realizar las consultas
     */
    public static void iniciar(Session sesionActual){
        //Create Neodatis database file
        //Crear Neodatis
        ODB odb = ODBFactory.open("C://users//Yleve//Desktop//Employees.Neodatis");
        //Query in Hibernate
        //Consulta en Hibernate
        Query q = sesionActual.createQuery("from Employees");
        //Create employees list
        //Creamos la lista de empleados
         List<Employees>listaEmpleados = q.list();
        //Save all employees on NeoDatis
         //Almacenamos los elementos en NeoDatis
        for (Employees empleado:listaEmpleados){
             odb.store(empleado);
         }
        
        org.neodatis.odb.Objects<Employees>listaEmployees = odb.getObjects (Employees.class);
        Employees emp;
        //Show all the employees on the list
        //Recorremos la lista para mostrar los empleados
        while(listaEmployees.hasNext()){
        emp = listaEmployees.next();
            System.out.println("*****************************");
            System.out.println("ID: "+emp.getEmployeeId()+"\n"
                +"Nombre: "+emp.getFirstName()+"\n"
                    +"Apellido: "+emp.getLastName()+"\n"
                    +"Email: "+emp.getEmail()+"\n"
                    +"Telefono: "+emp.getPhoneNumber()+"\n"
                    +"Fecha Contratacion: "+emp.getHireDate()+"\n"
                    +"Salario: "+emp.getSalary()+"\n"
                    +"Comision "+emp.getCommissionPct()+"\n");
        }
        //Close the data flow
        //Cerramos flujos
        odb.close();
        sesionActual.close();
        }   
    }

