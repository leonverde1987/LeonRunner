/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leonrunner;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import leonrunner.Acciones;

/**
 *
 * @author Matias
 */
public class Controlador extends Acciones {
    public static final char SEPARATOR=';';
    public static final char QUOTE='"';
    public void LeerArchivo() throws IOException{
       String CP="";
       String Navegador="";
       String Modulo="";
       String SubModulo="";
       String Paso="";
       String Resultado="";
       CSVReader reader = null;
      try {
         reader = new CSVReader(new FileReader("C:\\Ambiente\\entradas\\SuitePruebas.csv"),SEPARATOR,QUOTE);
         String[] nextLine=null;
         
         while ((nextLine = reader.readNext()) != null) {
            System.out.println(Arrays.toString(nextLine));
            if(!"TipoFila".equals(nextLine[0])){
                if(!"TestCase".equals(nextLine[0])){
                    this.ElegirAcciones(nextLine[8],nextLine[9],nextLine[10],nextLine[11],nextLine[8],nextLine[9], 1);
                    int steps= Integer.parseInt(nextLine[1]);
                    for(int a=1;a<steps;a++){
                       nextLine = reader.readNext(); 
                       this.ElegirAcciones(nextLine[8],nextLine[9],nextLine[10],nextLine[11],nextLine[8],nextLine[9], a+1); 
                    }
                    
                }
                
            }
         }
         
      }catch (Exception e) {
        System.out.println("Error: "+e);
      }finally {
        if (null != reader) {
           reader.close();
        } 
      }
    }
    
}
