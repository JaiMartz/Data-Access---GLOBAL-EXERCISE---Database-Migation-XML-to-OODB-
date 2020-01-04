/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad_global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

/**
 *
 * @author Jairo
 */
public class MigracionXML{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException, XMLDBException, XMLDBException, InstantiationException, IllegalAccessException, IOException, IOException{
        
        MigracionXML programa = new MigracionXML();
        programa.iniciar();
    }
    /**
     * Inicia la ejecución del programa
     * @throws ClassNotFoundException Clase no encontrada
     * @throws SQLException ExcepcionSQL
     * @throws XMLDBException Excepcion XML
     * @throws InstantiationException Excepcion Instancia
     * @throws IllegalAccessException Excepcion Acceso Ilegal
     * @throws FileNotFoundException Excepcion Fichero no encontrado
     * @throws IOException Excepcion IO
     */
    public static void iniciar() throws ClassNotFoundException, SQLException, XMLDBException, InstantiationException, IllegalAccessException, FileNotFoundException, IOException{
        //Connect with ORACLE database by JDBC
        //Conexion JDBC con ORACLE
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","HR","oracle");
        //Connection XML:DB
        //Conexion XML:DB
        Class driverClass = Class.forName("org.exist.xmldb.DatabaseImpl");
        Database db =(Database) driverClass.newInstance();
        DatabaseManager.registerDatabase(db);
        //Load collection
        //Cargar coleccion
        String url= "xmldb:exist://localhost:8080/exist/xmlrpc/db/empleados";
        String user = "admin";
        String password = "123456";
        org.xmldb.api.base.Collection colec = DatabaseManager.getCollection(url, user, password);
        
        
        if(colec != null){
            //Create service
            //Creacion del servicio
            XPathQueryService servicio = (XPathQueryService) colec.getService("XPathQueryService","1.0");
            //Ask for all the employees
            //Consulta todos los empleados
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery("SELECT * FROM Employees");
            //Show by screen all the employees
            //Mostrar por pantalla todos los empleados
            while(resultado.next()){
                //ID, FirstName, LastName, Email, PhoneNumber, HireDate, JobID, Salary, COMMISION_PCT, Manager_ID, Department_ID
                String sentenciaInsercion ="update insert <EMPLEADO>" +
                        "\n\t<EMP_ID>"+resultado.getInt(1)+"</EMP_ID>\n" +
                        "\t<FIRST_NAME>"+resultado.getString(2)+"</FIRST_NAME>\n" +
                        "\t<LAST_NAME>"+resultado.getString(3)+"</LAST_NAME>\n" +
                        "\t<EMAIL>"+resultado.getString(4)+"</EMAIL>\n" +
                        "\t<PHONE_NUM>"+resultado.getString(5)+"</PHONE_NUM>\n" +
                        "\t<HIRE_DATE>"+resultado.getDate(6)+"</HIRE_DATE>\n" +
                        "\t<JOB_ID>"+resultado.getString(7)+"</JOB_ID>\n" +
                        "\t<SALARY>"+resultado.getInt(8)+"</SALARY>\n" +
                        "\t<COMMISSION_PCT>"+resultado.getInt(9)+"</COMMISSION_PCT>\n" +
                        "\t<MANAGER_ID>"+resultado.getInt(10)+"</MANAGER_ID>\n" +
                        "\t<DEPT_ID>"+resultado.getInt(11)+"</DEPT_ID>\n" +
                    "</EMPLEADO> into /Empleados";
                
                servicio.query(sentenciaInsercion);
                }
            //READ XML FILE
            //LECTURA DEL FICHERO XML
            File f = new File("C://users//Yleve//Desktop//XML//Empleados.xml");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            System.out.println("LECTURA DE FICHERO XML EMPLEADOS.XML");
            //fILE READER
            //Lector del fichero
            while(line != null){
                System.out.println(line);
                line = br.readLine();
            }
            //Close buffered reader and file reader
            br.close();
            fr.close();
            
            //Close all data flows
            //Cerramos flujos
            colec.close();
            resultado.close();
            sentencia.close();
            conexion.close();
        }
    }
}


