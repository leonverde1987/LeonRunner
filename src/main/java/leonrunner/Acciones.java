/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leonrunner;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *
 * @author Matias
 */
public class Acciones extends Generico{
    public void ElegirAcciones(RemoteWebDriver driver, String TipoAccion, String TipoObjeto, String Objeto, String Dato, String Paso, String Resultado, int contador, String Navegador, String CP) throws InterruptedException{
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
                this.AssertMsjElemento(driver, Dato, Objeto);
                this.capturaDriver(driver, "C://Ambiente//evidencia", contador, CP, Navegador);
                break;    
                
        }
    }
}
