/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp3;
import pc.tp3.Energia;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author famil
 */
public class VerificarEnergia implements Runnable {
    private Energia energia = new Energia();

   
    private synchronized void quitarEnergia(int valor) throws InterruptedException {

        if (energia.getValor() >= valor) {
            System.out.println(Thread.currentThread().getName() + " ha drenado " + valor + " de energia");
            Thread.sleep(1000);
            energia.quitarEnergia(valor);

            System.out.println( "Energia total: " + energia.getValor());
        } else {
            System.out.println("No hay energia suficiente.");
            Thread.sleep(1000);
        }
    }
    
     private synchronized void sumarEnergia(int valor) throws InterruptedException {

        if (energia.getValor() >= valor) {
            System.out.println(Thread.currentThread().getName() + " ha sumado " + valor + " de energia");
            Thread.sleep(100);
            energia.sumarEnergia(valor);

            System.out.println( "Energia total: " + energia.getValor());
        } else {
            System.out.println("No hay energia suficiente.");
            Thread.sleep(100);
        }
    }

    
    public void run() {
        for (int i = 0; i <= 2; i++) {
            try {
                if (Thread.currentThread().getName().equals("Criatura Oscura")) {
                    this.quitarEnergia(3);
                }else if(Thread.currentThread().getName().equals("Sanador")){
                    this.sumarEnergia(3);
                }
                
            } catch (InterruptedException ex) {
                Logger.getLogger(VerificarCuenta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
