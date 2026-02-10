/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc1;
import pc1.MiHilo;
/**
 *
 * @author famil
 */
public class UsoHilos {

    public static void main(String[] args) {
        System.out.println("Hilo principal iniciando.");
        //Primero, construye un objeto unHilo.
        MiHilo mh = new MiHilo("#1");
        //Luego, construye un hilo de ese objeto.
        Thread nuevoHilo = new Thread(mh);
        //Finalmente, comienza la ejecución del hilo.
        nuevoHilo.start();
        for (int i = 0; i < 50; i++) {
            System.out.print(" .");
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException exc) {
            System.out.println("Hilo principal interrumpido.");
        }
        System.out.println("Hilo principal finalizado.");
    }
}
