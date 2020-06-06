/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leonrunner;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import static leonrunner.Controlador.QUOTE;
import static leonrunner.Controlador.SEPARATOR;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *
 * @author Matias
 */
public class hilos extends Thread{
    private String Navegador = "";
    private RemoteWebDriver driver = null;
    private CSVReader reader = null;
    
    @Override
    public void run(){
        try {
            //Clase cla = new Clase();
            reader = new CSVReader(new FileReader("C:\\Ambiente\\entradas\\SuitePruebas.csv"),SEPARATOR,QUOTE);
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setPlatform(Platform.WINDOWS);
            capabilities.setBrowserName("chrome");
            capabilities = DesiredCapabilities.chrome();
            URL url = new URL("http://localhost:5556/wd/hub");
            driver = new RemoteWebDriver(url, capabilities);
            //cla.ejecucionPruebas(reader, driver, "chrome");
        } catch (IOException ex) {
            Logger.getLogger(hilos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalThreadStateException ex) {
            Logger.getLogger(hilos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
