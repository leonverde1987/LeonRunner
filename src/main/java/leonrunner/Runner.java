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

/**
 *
 * @author Matias
 */
public class Runner{
   public static void main(String[] args) throws IOException {
       new leonrunner.Controlador().LeerArchivo();
   }
}
