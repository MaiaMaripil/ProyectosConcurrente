/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp4;

import java.util.concurrent.Semaphore;

/**
 *
 * @author maiamaripil
 */
public class CentroCopiado {

    public static void main(String[] args) {
        GestionImpresoras impresoras = new GestionImpresoras();
        Cliente c1 = new Cliente(impresoras, "c1");
        Cliente c2 = new Cliente(impresoras, "c2");
        Cliente c3 = new Cliente(impresoras, "c3");
        Cliente c4 = new Cliente(impresoras, "c4");
        Cliente c5 = new Cliente(impresoras, "c5");

        Thread t1 = new Thread(c1);
        Thread t2 = new Thread(c2);
        Thread t3 = new Thread(c3);
        Thread t4 = new Thread(c4);
        Thread t5 = new Thread(c5);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}

class Cliente implements Runnable {

    private GestionImpresoras recurso;
    private String nombre;

    public Cliente(GestionImpresoras r, String n) {
        this.recurso = r;
        this.nombre = n;
    }

    public void run() {
        try {
            this.recurso.imprimir(nombre);
        } catch (InterruptedException e) {
            System.out.println("ERROR");
        }
    }
}

class GestionImpresoras {

    private Semaphore impresoras = new Semaphore(3);

    public void imprimir(String nombre) throws InterruptedException {
        impresoras.acquire();

        System.out.println(nombre + " esta usando una impresora");
        Thread.sleep(1000);
        System.out.println(nombre + " termino de imprimir");
        impresoras.release();
    }
}
