/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clickautomationtest;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import clickautomationtest.Acciones;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *
 * @author Matias
 */
public class Controlador extends Acciones {
    public static final char SEPARATOR=',';
    public static final char QUOTE='"';
    public Properties reporte = null; 
    
    public hilos hilo = new hilos();
    public String navegador="";
    public CSVReader reader = null;  
    public ArrayList listaCPs=null;
    
    public void LeerArchivo() throws IOException{
        
        String Resultado="";
        System.out.print("Antes de confirg");
        reporte = new clickautomationtest.Generico().getPropetiesFile("C:\\Ambiente\\entradas\\config.properties");
        System.out.print("despues de confirg");
        try {
            new clickautomationtest.Generico().leventarNodosGrid();
            if(reporte.getProperty("chrome").equals("si")){
                navegador = "chrome";
                RemoteWebDriver driver = this.openGridBrowser(navegador);
                reader = new CSVReader(new FileReader(reporte.getProperty("testCases")),SEPARATOR,QUOTE);
                if(reporte.getProperty("dataDriven").equals("si")){
                    this.ejecucionPruebasConDataDriven(reader, driver, navegador);
                }else{
                    this.ejecucionPruebasSinDataDriven(reader, driver, navegador);
                }
                driver.close();
            }
            
            if(reporte.getProperty("ie").equals("si")){
                navegador = "ie";
                RemoteWebDriver driver = this.openGridBrowser(navegador);
                reader = new CSVReader(new FileReader(reporte.getProperty("testCases")),SEPARATOR,QUOTE);
                if(reporte.getProperty("dataDriven").equals("si")){
                    this.ejecucionPruebasConDataDriven(reader, driver, navegador);
                }else{
                    this.ejecucionPruebasSinDataDriven(reader, driver, navegador);
                }
                driver.close();
            }
            
            if(reporte.getProperty("firefox").equals("si")){
                navegador = "firefox";
                RemoteWebDriver driver = this.openGridBrowser(navegador);
                reader = new CSVReader(new FileReader(reporte.getProperty("testCases")),SEPARATOR,QUOTE);
                if(reporte.getProperty("dataDriven").equals("si")){
                    this.ejecucionPruebasConDataDriven(reader, driver, navegador);
                }else{
                    this.ejecucionPruebasSinDataDriven(reader, driver, navegador);
                }
                driver.close();
            }
        }catch (Exception e) {
            System.out.println("Error: "+e);
            System.out.print("Error nodos");
        }finally {
            if (null != reader) {
               reader.close();
            } 
            new clickautomationtest.Generico().cierraNodosGrid();
        }
    }
    
    
    public void ejecucionPruebasSinDataDriven(CSVReader reader, RemoteWebDriver driver, String navegador) throws IOException, InterruptedException{
        String[] nextLine=null;
        while ((nextLine = reader.readNext()) != null) {
            if(!"TipoFila".equals(nextLine[0])){
                if("TestCase".equals(nextLine[0])){                   
                    List<String> Pasos= new ArrayList<String>();
                    String CP = nextLine[3]+"_"+nextLine[4]+"_"+nextLine[5];
                    int steps= Integer.parseInt(nextLine[1]);
                    Pasos.add(nextLine[7]);
                    this.ElegirAcciones(driver,nextLine[8],nextLine[9],nextLine[10],nextLine[11],nextLine[6],nextLine[7], 1, navegador, CP);
                    for(int a=1;a<steps;a++){
                       nextLine = reader.readNext(); 
                       Pasos.add(nextLine[7]);
                       String resultado = this.ElegirAcciones(driver,nextLine[8],nextLine[9],nextLine[10],nextLine[11],nextLine[6],nextLine[7], a+1, navegador, CP); 
                       if((a+1) == steps){
                           new clickautomationtest.Evidence().crearHTML(CP, resultado, steps, Pasos, "C:\\Ambiente\\evidencia\\", "", "", navegador);//.finalizarTestCase(driverCH, CP, Resultado, steps, Pasos, "C:\\Ambiente\\evidencia\\", "", "", "Chrome");
                          
                       }
                    }
                }
            }
        }
        
    }
    
    public void ejecucionPruebasConDataDriven(CSVReader reader, RemoteWebDriver driver, String navegador) throws IOException, InterruptedException{
        String[] nextLine=null;
        this.obtenerCP(reader);
        int contador = 0;
        while (listaCPs.size() > contador) {
            nextLine = (String[]) listaCPs.get(contador);
            if("TestCase".equals(nextLine[0])){
                
                //Leemos el caso de prueba
                List<String> Pasos= new ArrayList<String>();
                String CP = nextLine[4]+"_"+nextLine[5]+"_"+nextLine[6];
                int steps= Integer.parseInt(nextLine[2]);
                
                //Traemos los datos de prueba
                ArrayList DataDriven = new ArrayList();
                DataDriven = this.obtenerDataDriven(nextLine[1]);
                
                //iniciamos un ciclo while para el caso por data driven
                
                int contaDriven = 0;
                while(DataDriven.size() >= contaDriven){
                    //String[] driveNextLine = (String[]) DataDriven.get(contaDriven);
                    for(int a=0;a<steps;a++){
                       if(a != 0){
                            nextLine = (String[]) listaCPs.get(contador+a); 
                       }
                       Pasos.add(nextLine[7]);
                       String resultado = this.ElegirAcciones(driver,nextLine[9],nextLine[10],nextLine[11],this.obtenerDato(DataDriven, a, nextLine[12]),nextLine[7],nextLine[8], a+1, navegador, CP); 
                       if((a+1) == steps){
                           new clickautomationtest.Evidence().crearHTML(CP, resultado, steps, Pasos, "C:\\Ambiente\\evidencia\\", "", "", navegador);//.finalizarTestCase(driverCH, CP, Resultado, steps, Pasos, "C:\\Ambiente\\evidencia\\", "", "", "Chrome");
                           contador = contador + a;
                       }
                    }
                    contaDriven++;
                }
            }

        contador++;    
        }
    }
    
    public String obtenerDato(ArrayList DataDriven, int contadorFila, String dato){
        String retorno = "";
        String[] columnas = (String[]) DataDriven.get(0);
        String[] driveLine = (String[]) DataDriven.get(contadorFila+1);
        for(int a=0; a<columnas.length; a++){
            if(dato.equals(columnas[a])){
                retorno = (String) driveLine[a];
            }
        }
        
        return retorno;
    }
    
    public ArrayList obtenerCP(CSVReader diseño) throws IOException{
        String[] nextLine=null;
        listaCPs = new ArrayList();
        while ((nextLine = reader.readNext()) != null) {
            if(!"TipoFila".equals(nextLine[0])){
                //if("TestCase".equals(nextLine[0])){
                    listaCPs.add(nextLine);
                //}
            }
        }
        return listaCPs;
    }
    
    public ArrayList obtenerDataDriven(String Archivo) throws IOException{
        String[] nextLine=null;
        CSVReader Datos = new CSVReader(new FileReader("C:\\Ambiente\\entradas\\"+Archivo),SEPARATOR,QUOTE);
        ArrayList datosCP = new ArrayList();
        while ((nextLine = Datos.readNext()) != null) {
            datosCP.add(nextLine);
        }
        return datosCP;
    }
}
