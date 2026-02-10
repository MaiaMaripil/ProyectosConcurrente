/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp8;

import java.util.concurrent.Semaphore;

/**
 *
 * @author famil
 */
public class ComedorSolados {

    public static void main(String[] args) {

        Comedor c = new Comedor();

        for (int i = 1; i <= 10; i++) {
            Thread s = new Thread(new Soldado("SOLDADO " + i, c));

            s.start();

            try {
                Thread.sleep((int) (Math.random() * 3000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
}

class Soldado implements Runnable {

    private String nombre;
    private Comedor recurso;

    public Soldado(String n, Comedor c) {
        this.nombre = n;
        this.recurso = c;
    }

    public void run() {
        try {
            int o = (int) ((Math.random() * 2) + 1);
            boolean p = (Math.random() > 0.5);
            recurso.entrarComedor(nombre, o, p);
            Thread.sleep(4000);
            recurso.salirComedor(nombre);
        } catch (InterruptedException e) {
            System.out.println("Error");
        }
    }
}

class Comedor {

    private Semaphore capacidad= new Semaphore(5);;
    private Semaphore mostradorAlmuerzo=new Semaphore(3);
    private Semaphore mostradorPostre= new Semaphore(2);
    private Semaphore abridores= new Semaphore(3);

   

    public void entrarComedor(String n, int o, boolean p) throws InterruptedException {
        capacidad.acquire();
        System.out.println(n + " Entro al comedor. C:"+ capacidad.availablePermits());
        mostradorAlmuerzo.acquire();
        System.out.println(n + " Tomo una bandeja con comida. mA:"+ capacidad.availablePermits());
        if (o == 2) {
            //Eligio botella de gaseosa
            abridores.acquire();
            System.out.println(n + " Eligio gaseosa. Abriendo... A:"+abridores.availablePermits());
            Thread.sleep(2000);
            abridores.release();
        }
        mostradorAlmuerzo.release();
        if (p) {
            mostradorPostre.acquire();
            System.out.println(n + " Tomando postre del mostrador...");
            Thread.sleep(2000);
            System.out.println(n + " ya eligio postre");
            mostradorPostre.release();
        }
        System.out.println(n + " Comiendo...");
    }

    public void salirComedor(String n) {

        System.out.println(n + " Termino de comer y dejo su bandeja. C:"+capacidad.availablePermits());
        capacidad.release();
    }

}
