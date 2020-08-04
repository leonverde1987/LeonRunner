/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leonrunner;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import leonrunner.Acciones;
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
    
    public void LeerArchivo() throws IOException{
        
        String Resultado="";
        
        reporte = new leonrunner.Generico().getPropetiesFile("C:\\Ambiente\\entradas\\config.properties");
        
        try {
            new leonrunner.Generico().leventarNodosGrid();
            if(reporte.getProperty("chrome").equals("si")){
                navegador = "chrome";
                RemoteWebDriver driver = this.openGridBrowser(navegador);
                reader = new CSVReader(new FileReader("C:\\Ambiente\\entradas\\SuitePruebas.csv"),SEPARATOR,QUOTE);
                this.ejecucionPruebas(reader, driver, navegador);
                driver.close();
            }
            
            if(reporte.getProperty("ie").equals("si")){
                navegador = "ie";
                RemoteWebDriver driver = this.openGridBrowser(navegador);
                reader = new CSVReader(new FileReader("C:\\Ambiente\\entradas\\SuitePruebas.csv"),SEPARATOR,QUOTE);
                this.ejecucionPruebas(reader, driver, navegador);
                driver.close();
            }
            
            if(reporte.getProperty("firefox").equals("si")){
                navegador = "firefox";
                RemoteWebDriver driver = this.openGridBrowser(navegador);
                reader = new CSVReader(new FileReader("C:\\Ambiente\\entradas\\SuitePruebas.csv"),SEPARATOR,QUOTE);
                this.ejecucionPruebas(reader, driver, navegador);
                driver.close();
            }
        }catch (Exception e) {
            System.out.println("Error: "+e);
        }finally {
            if (null != reader) {
               reader.close();
            } 
            new leonrunner.Generico().cierraNodosGrid();
        }
    }
    
    
    public void ejecucionPruebas(CSVReader reader, RemoteWebDriver driver, String navegador) throws IOException, InterruptedException{
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
                           new leonrunner.Evidence().crearHTML(CP, resultado, steps, Pasos, "C:\\Ambiente\\evidencia\\", "", "", navegador);//.finalizarTestCase(driverCH, CP, Resultado, steps, Pasos, "C:\\Ambiente\\evidencia\\", "", "", "Chrome");
                          
                       }
                    }
                }

            }
        }
    }
}
