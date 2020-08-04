/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leonrunner;

import java.io.IOException;
import java.util.NoSuchElementException;
import junit.framework.ComparisonFailure;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *
 * @author Matias
 */
public class Acciones extends Generico{
    public String Resultado = "Exitoso";
    
    public String ElegirAcciones(RemoteWebDriver driver, String TipoAccion, String TipoObjeto, String Objeto, String Dato, String Paso, String Resultado, int contador, String Navegador, String CP) throws InterruptedException{
        try{
            switch(TipoAccion){
                case "AbrirURL":
                    this.abrirURl(driver, Dato);
                    this.capturaDriver(driver, "C://Ambiente//evidencia", contador, CP, Navegador);
                    break;
                case "Clic":
                    this.clic_btn(driver, TipoObjeto, Objeto);
                    this.capturaDriver(driver, "C://Ambiente//evidencia", contador, CP, Navegador);
                    break;
                case "IngresarTexto":
                    this.ingresar_texto(driver, TipoObjeto, Objeto, Dato);
                    this.capturaDriver(driver, "C://Ambiente//evidencia", contador, CP, Navegador);
                    break;
                case "CambiarPestana":
                    this.cambiaPestana(driver);
                    this.capturaDriver(driver, "C://Ambiente//evidencia", contador, CP, Navegador);
                    break;
                case "AssertTexto":
                    Resultado = this.AssertMsjElemento(driver, Dato, Objeto);
                    this.capturaDriver(driver, "C://Ambiente//evidencia", contador, CP, Navegador);
                    break;    

            }
        }
        catch(NoSuchElementException e){
            Resultado = "Ejecución Fallida: " + e;
            this.capturaDriver(driver, "C://Ambiente//evidencia", contador, CP, Navegador);
        }catch(ComparisonFailure e){
            Resultado = "Fallido: " + e;
            this.capturaDriver(driver, "C://Ambiente//evidencia", contador, CP, Navegador);
        }catch(Exception e){
            Resultado = "Ejecución Fallida: " + e;
            this.capturaDriver(driver, "C://Ambiente//evidencia", contador, CP, Navegador);
        }
        return Resultado;
    }
}
