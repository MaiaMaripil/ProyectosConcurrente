/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pc1;

import pc1.prueba;
import pc1.PruebaExcep;
import pc1.MiEjecucion;

/**
 *
 * @author famil
 */
public class PC1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Thread miHilo = new MiEjecucion();

        miHilo.start();
        try {
            /*espera a que termine de ejecutarse el hilo miHilo*/
            miHilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        };

        System.out.println("En el main");
    }
}

}
