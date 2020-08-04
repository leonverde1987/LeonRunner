/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leonrunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author Matias
 */
public class Generico extends Evidence {
    /*
    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    CONFIGURACIONES
    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    
    /***
     * En este método abrimos el properties de configuración del proyecto debe estar en la ruta c:/ambiente/configuracion.
     * @return El archivo de propiedades con la configuración.
     * @throws FileNotFoundException si no encuentra el archivo en la ruta c:/ambiente/configuracion.
     */
    public Properties getPropetiesFile(String Archivo) throws FileNotFoundException{
        Properties prop = new Properties();
        try{
            prop.load(new FileInputStream(Archivo));
        }catch(IOException e){
            System.out.println("Mensaje Properties: "+ e);
        }
        return prop;
        
    }
    
    public RemoteWebDriver openGridBrowser(String navegador) throws MalformedURLException, InterruptedException{
        RemoteWebDriver driver = null;
        URL url=null;
        try{
        
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setPlatform(Platform.WINDOWS);
            
            if("chrome".equals(navegador)){
                capabilities.setBrowserName(navegador);
                capabilities = DesiredCapabilities.chrome();
                url = new URL("http://localhost:5556/wd/hub");
                driver = new RemoteWebDriver(url, capabilities);
            }
            if("ie".equals(navegador)){
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                //capabilities.setBrowserName(navegador);
                //capabilities = DesiredCapabilities.internetExplorer();
//                capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
//                capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION,false);
                url = new URL("http://localhost:5559/wd/hub");
                
                driver = new RemoteWebDriver(url, ieOptions);
            }
            if("edge".equals(navegador)){
                EdgeOptions optionEdge = new EdgeOptions();
                //capabilities.setBrowserName(navegador);
                //capabilities = DesiredCapabilities.edge();
                url = new URL("http://localhost:5558/wd/hub");
                driver = new RemoteWebDriver(url, optionEdge);
            }
            if("firefox".equals(navegador)){
                driver = null;
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                //capabilities.setBrowserName(navegador);
                //capabilities = DesiredCapabilities.firefox();
                url = new URL("http://localhost:5557/wd/hub");
                driver = new RemoteWebDriver(url, firefoxOptions);
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
            driver.manage().window().maximize();
            return driver;
        }
    
    public void leventarNodosGrid() throws InterruptedException, FileNotFoundException{
        Properties Config = this.getPropetiesFile("C:\\Ambiente\\entradas\\config.properties");
        try {
            String cmd = "cmd /c start cmd.exe /K java -jar "+Config.getProperty("seleniumServer")+" -role hub -port "+Config.getProperty("portHub");
            Runtime.getRuntime().exec(cmd); 
        } catch (IOException ioe) {
            System.out.println ("Hub: "+ioe);
        }
        Thread.sleep(5000);
        try {
            String cmd = "cmd /c start cmd.exe /K java -Dwebdriver.gecko.driver="+Config.getProperty("firefoxRuta")+" -jar "+Config.getProperty("seleniumServer")+" -role webdriver -hub http://localhost:5555/grid/register -port "+Config.getProperty("portFirefox")+" -browser browserName=firefox";
            Runtime.getRuntime().exec(cmd); 
        } catch (IOException ioe) {
            System.out.println ("Gecko node: "+ioe);
        }
        Thread.sleep(5000);
        try {
            String cmd = "cmd /c start cmd.exe /K java -Dwebdriver.chrome.driver="+Config.getProperty("chromeRuta")+" -jar "+Config.getProperty("seleniumServer")+" -role webdriver -hub http://localhost:5555/grid/register -port "+Config.getProperty("portChrome")+" -browser browserName=chrome";
            Runtime.getRuntime().exec(cmd); 
        } catch (IOException ioe) {
            System.out.println ("Chrome node: "+ioe);
        }
        Thread.sleep(5000);
        /*try {
            String cmd = "cmd /c start cmd.exe /K java -Dwebdriver.edge.driver="+Config.getProperty("edge")+" -jar "+Config.getProperty("seleniumServer")+" -role webdriver -hub http://localhost:5555/grid/register -port "+Config.getProperty("edgePort")+" -browser browserName=iexplorer";
            Runtime.getRuntime().exec(cmd); 
        } catch (IOException ioe) {
            System.out.println ("Edege node: "+ioe);
        } 
        Thread.sleep(5000);*/
        try {
            String cmd = "cmd /c start cmd.exe /K java -Dwebdriver.ie.driver="+Config.getProperty("ieRuta")+" -jar "+Config.getProperty("seleniumServer")+" -role webdriver -hub http://localhost:5555/grid/register -port "+Config.getProperty("portIe")+" -browser browserName=iexplorer";
            Runtime.getRuntime().exec(cmd); 
        } catch (IOException ioe) {
            System.out.println ("IE node: "+ioe);
        }
    }
    
    public void cierraNodosGrid(){
        try {
            String cmd = "cmd /c start cmd.exe /K TASKKILL /F /IM cmd.exe /T";
            Runtime.getRuntime().exec(cmd); 
        } catch (IOException ioe) {
            System.out.println ("Edege node: "+ioe);
        }
        
    }
    
    public void GenerarEvidencias(RemoteWebDriver driver, String Escenario, String Resultado, int contador, List<String> Pasos, String RutaEvidencia, String Modulo, String Version, String navegador){
        try{
            System.out.println("Lista: "+Pasos);
            if(Resultado.length()>10){
                if("Ejecución Fallida".equals(Resultado.substring(0, 17))){
                    this.capturaDriver(driver, RutaEvidencia, contador, Escenario, navegador);
                }
            }
            //Generamos PDF
            this.crearPDF(Escenario, Resultado, contador, Pasos, RutaEvidencia, Modulo, Version, navegador);
            //Generamos PDF
            this.crearXML(Escenario, Resultado, contador, Pasos, RutaEvidencia, navegador);
            //Generamos HTML
            this.crearHTML(Escenario, Resultado, contador, Pasos, RutaEvidencia, Modulo, Version, navegador);

        }catch(Exception e){
            System.out.println("MEnsaje Evidencia: "+e);
        }
    }
    /*
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    ASSERTS
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    
    /**
     * En este método vamos a validar que dos mensajes sean iguales.
     * @param driver 
     * @param msjActual Es el valr del texto que se compara.
     * @param Elemento Es el elemento del que se va a comparr el texto.
     */
    public String AssertMsjElemento(RemoteWebDriver driver, String msjActual, String Elemento){
        String msj = "";
        try{
            Assert.assertEquals(this.obtenerTexto(driver, "xpath", Elemento), msjActual);
            msj = "Exitoso";
        }catch(AssertionError e){
            System.out.println("Mensaje Assert Fail: "+e);
            msj = "Fallido, Reusltado Esperado: "+e;
        }
        
        return msj;
    }
    
    
    /*
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    METODOS
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    
    /***
     * El método nos ayuda a sweleccionar un texto de un cbo, select o dripdownlist.
     * @param driver es el webDriver en el que se ejecuta la pruebas automatizada.
     * @param findby Es el tipo de selector selenium id, name o XPATH.
     * @param Elemento Es el selector selenium del cbo, select o dropdownlist.
     * @param Texto Es el valor del texto que se busca en el elemento.
     */
    public void seleccionar_combo(RemoteWebDriver driver, String findby, String Elemento, String Texto){
        Select cbo;
        switch(findby) {
            case "id":
                cbo = new Select(driver.findElement(By.id(Elemento)));
                cbo.selectByVisibleText(Texto);
                break;
            case "name":
                cbo = new Select(driver.findElement(By.name(Elemento)));
                cbo.selectByVisibleText(Texto);
                break;
            case "xpath":
                cbo = new Select(driver.findElement(By.xpath(Elemento)));
                cbo.selectByVisibleText(Texto);
                break;
            case "class":
                cbo = new Select(driver.findElement(By.className(Elemento)));
                cbo.selectByVisibleText(Texto);
                break;
        }
    }
    
    
    
    /***
     * El método nos ayuda a dar clic a un elemento.
     * @param driver es el webDriver en el que se ejecuta la pruebas automatizada.
     * @param findby Es el tipo de selector selenium id, name o XPATH.
     * @param Elemento Es el selector selenium que se le va a dar Clic.
     */
    public void clic_btn(RemoteWebDriver driver, String findby, String Elemento){
        switch(findby) {
            case "id":
                driver.findElement(By.id(Elemento)).click();
                break;
            case "name":
                driver.findElement(By.name(Elemento)).click();
                break;
            case "xpath":
                driver.findElement(By.xpath(Elemento)).click();
                break;
            case "xpathTexto":
                driver.findElement(By.xpath("//*[text() = '"+Elemento+"']")).click();
                break;
            case "class":
                driver.findElement(By.className(Elemento)).click();
                break;
        }

    }
    
    /***
     * El método nos ayuda a presionar enter sobre un elemento.
     * @param driver Es el webDriver en el que se ejecuta la pruebas automatizada.
     * @param findby Es el tipo de selector selenium id, name o XPATH.
     * @param Elemento Es el selector selenium al que le vamos agregar texto.
     */
    public void enter(RemoteWebDriver driver, String findby, String Elemento){
        switch(findby) {
            case "id":
                driver.findElement(By.id(Elemento)).submit();
                break;
            case "name":
                driver.findElement(By.name(Elemento)).submit();
                break;
            case "xpath":
                driver.findElement(By.xpath(Elemento)).submit();
                break;
            case "class":
                driver.findElement(By.className(Elemento)).submit();
                break;
        }
    }
    
    /***
     * El método nos ayuda a ingresar texto a un elemento.
     * @param driver Es el webDriver en el que se ejecuta la pruebas automatizada.
     * @param findby Es el tipo de selector selenium id, name o XPATH.
     * @param Elemento Es el selector selenium al que le vamos agregar texto.
     * @param Texto Es el texto que se va ingresar al campo.
     */
    public void ingresar_texto(RemoteWebDriver driver, String findby, String Elemento, String Texto){
        switch(findby) {
            case "id":
                driver.findElement(By.id(Elemento)).clear();
                driver.findElement(By.id(Elemento)).sendKeys(Texto);
                driver.findElement(By.id(Elemento)).submit();
                break;
            case "name":
                driver.findElement(By.name(Elemento)).clear();
                driver.findElement(By.name(Elemento)).sendKeys(Texto);
                driver.findElement(By.name(Elemento)).submit();
                break;
            case "xpath":
                
                    WebElement el = driver.findElement(By.xpath(Elemento));
                    el.sendKeys(Texto);
                    el.submit();
                
                break;
        }
    }

    /***
     * El método nos ayuda a cerrar un WebDriver.
     * @param driver Es el webDriver en el que se ejecuta la pruebas automatizada.
     */
    public void cerrar_driver(WebDriver driver){
            driver.close();
    }

    /***
     * El método le da un tiempo de 10 segundos al webDriver.
     * @exception InterruptedException Para manejar excepciones con el hilo de procesamiento que se esta deteniendo.
     */
    public void dormir10seg() throws InterruptedException{
        Thread.sleep(10000);
    }
    
    /***
     * El método le da un tiempo de 10 segundos al webDriver.
     * 
     * @exception InterruptedException Para manejar excepciones con el hilo de procesamiento que se esta deteniendo.
     */
    public void abrirURl(RemoteWebDriver driver, String URL){
        try{
            driver.get(URL);
        }catch(Exception e){
            System.out.println("Mensaje: "+e);
        }
    }
    
    /***
     * El método obtiene el texto de un objeto.
     * @param driver es el webDriver en el que se ejecuta la pruebas automatizada.
     * @param findby Es el tipo de selector selenium id, name o XPATH.
     * @param Elemento Es el selector selenium.
     * @return Regresa el texto del objeto o un vacio en caso de no encontrar el findby.
     */
    public String obtenerTexto(RemoteWebDriver driver, String findby, String Elemento){
        String texto = "";
        switch(findby) {
            case "id":
                texto = driver.findElement(By.id(Elemento)).getText();
                break;
            case "name":
                texto = driver.findElement(By.name(Elemento)).getText();
                break;
            case "xpath":
                texto = driver.findElement(By.xpath(Elemento)).getText();
                break;
        }
        return texto;
    }
    
    
    public void cambiaPestana(RemoteWebDriver driver){
        //driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"\t");
//        Actions action= new Actions(driver); 
//        action.keyDown(Keys.CONTROL).sendKeys(Keys.TAB).build().perform();
//        String oldTab=driver.getWindowHandle(); 
//        driver.findElement(pageObj.getL_Popup_Window()).click(); 
//        ArrayList<String> newTab = new ArrayList<String>(driver.getWindowHandles()); 
//        newTab.remove(oldTab); 
//        driver.switchTo().window(newTab.get(0)); 
//        WebElement ele = driver.findElement(pageObj.getI_input_name()); 
//        ele.click(); 
//        ele.sendKeys(name); 
//        driver.findElement(pageObj.getI_submit()).click(); 
//        driver.switchTo().window(oldTab);
        
    }
}
