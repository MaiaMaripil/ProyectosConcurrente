/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp3;

/**
 *
 * @author famil
 */
public class TiendaMascotas {

    public static void main(String[] args) {
        Actividades act = new Actividades();
        Hamster h1 = new Hamster(act);
        Hamster h2 = new Hamster(act);

        Thread t1 = new Thread(h1, "Hamster 1");
        Thread t2 = new Thread(h2, "Hamster 2");

        t1.start();
        t2.start();
    }
}

class Hamster implements Runnable {

    private Actividades act;

    public Hamster(Actividades act) {
        this.act = act;
    }

    public void run() {
        try {
            act.comerDelPlato();
        } catch (InterruptedException e) {
            System.out.println("No pudo comer");
        }
        try {
            act.correrRueda();
        } catch (InterruptedException e) {
            System.out.println("No pudo correr en la rueda");
        }
        try {
            act.usarAmaca();
        } catch (InterruptedException e) {
            System.out.println("No pudo dormir");
        }
    }

}

class Actividades {

    private int amaca, rueda, plato;

    public Actividades() {
        amaca = 0;
        rueda = 0;
        plato = 0;
    }

    public synchronized void correrRueda() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
        rueda += 1;
        Thread.sleep(1000);
        //System.out.println("Corriendo:"+Thread.currentThread().getName());
    }

    public synchronized void usarAmaca() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
        amaca += 1;
        Thread.sleep(1000);
        //System.out.println("Descanzando:"+Thread.currentThread().getName());
    }

    public synchronized void comerDelPlato() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
        plato += 1;
        Thread.sleep(1000);
        //System.out.println("Comiendo:"+Thread.currentThread().getName());
    }

    public synchronized String toString() {
        return "Amaca: " + amaca + " Rueda: " + rueda + " Plato: " + plato;
    }
}


