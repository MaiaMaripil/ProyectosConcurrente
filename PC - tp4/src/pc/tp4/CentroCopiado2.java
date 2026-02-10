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
public class CentroCopiado2 {

    public static void main(String[] args) {
        GestionImpresoras impresoras = new GestionImpresoras();
        Cliente c1 = new Cliente(impresoras, "c1", "tipo A");
        Cliente c2 = new Cliente(impresoras, "c2", "tipo A");
        Cliente c3 = new Cliente(impresoras, "c3", "tipo B");
        Cliente c4 = new Cliente(impresoras, "c4", "cualquiera");
        Cliente c5 = new Cliente(impresoras, "c5", "tipo A");

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
    private String tipo;

    public Cliente(GestionImpresoras r, String n, String t) {
        this.recurso = r;
        this.nombre = n;
        this.tipo = t;
    }

    public void run() {
        try {
            if (tipo.equals("tipo A")) {
                recurso.imprimirA(nombre);
            } else if (tipo.equals("tipo B")) {
                recurso.imprimirB(nombre);
            } else {
                recurso.imprimir(nombre);
            }
        } catch (InterruptedException e) {

        }
    }
}

class GestionImpresoras {

    private Semaphore impresorasA = new Semaphore(3);
    private Semaphore impresorasB = new Semaphore(2);

    public void imprimirA(String nombre) throws InterruptedException {
        impresorasA.acquire();

        System.out.println(nombre + " esta usando una impresora de tipo A");
        Thread.sleep(1000);
        System.out.println(nombre + " termino de imprimir");
        impresorasA.release();
    }

    public void imprimirB(String nombre) throws InterruptedException {
        impresorasB.acquire();

        System.out.println(nombre + " esta usando una impresora de tipo B");
        Thread.sleep(1000);
        System.out.println(nombre + " termino de imprimir");
        impresorasB.release();
    }

    public void imprimir(String nombre) throws InterruptedException {
        if (impresorasA.tryAcquire()) {
            System.out.println(nombre + " esta usando una impresora de tipo A");
            Thread.sleep(1000);
            System.out.println(nombre + " termino de imprimir");
            impresorasA.release();

        } else if (impresorasB.tryAcquire()) {
            System.out.println(nombre + " esta usando una impresora de tipo B");
            Thread.sleep(1000);
            System.out.println(nombre + " termino de imprimir");
            impresorasB.release();
        }
    }
}

