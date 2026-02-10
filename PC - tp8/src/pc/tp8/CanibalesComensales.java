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
public class CanibalesComensales {

    public static void main(String[] args) {
        Olla o = new Olla(4);
        Thread cocinero = new Thread(new Cocinero("COCINERO", o));

        cocinero.start();

        for (int i = 1; i <= 10; i++) {
            Thread c = new Thread(new Canibal("CANIBAL " + i, o));
            c.start();

            try {
                Thread.sleep((int) (Math.random()) * 3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Canibal implements Runnable {

    private String nombre;
    private Olla recurso;

    public Canibal(String n, Olla o) {
        this.nombre = n;
        this.recurso = o;
    }

    public void run() {
        try {
            recurso.comer(nombre);
        } catch (InterruptedException e) {
            System.out.println("Error");
        }
    }
}

class Cocinero implements Runnable {

    private String nombre;
    private Olla recurso;

    public Cocinero(String n, Olla o) {
        this.nombre = n;
        this.recurso = o;
    }

    public void run() {
        try {
            while (true) {
                recurso.cocinar(nombre);
            }
        } catch (InterruptedException e) {
            System.out.println("Error");
        }
    }
}

class Olla {

    private Semaphore raciones;
    private Semaphore ollaVacia = new Semaphore(0);
    private Semaphore avisarOllaLlena = new Semaphore(0);
    private int cantRaciones;
    private int esperando = 0;

    public Olla(int r) {
        raciones = new Semaphore(r);
        cantRaciones = r;
    }

    public void comer(String n) throws InterruptedException {
        System.out.println(n + " Quiere comer");
        if (raciones.tryAcquire()) {

            System.out.println(n + " comio 1 racion. Raciones: " + raciones.availablePermits());
        } else {
            synchronized (this) {
                esperando++;
            }
            if (esperando == cantRaciones) {
                esperando = 0;
            }
            if (esperando == 1) {
                System.out.println(n + " La olla esta vacia, avisa al cocinero");

                ollaVacia.release();

                System.out.println(n + " esperando aviso de olla llena");
                avisarOllaLlena.acquire();
                System.out.println(n + " la olla esta llena");
                esperando = 0;
                raciones.acquire();
                System.out.println(n + " comio 1 racion. Raciones: " + raciones.availablePermits());
            } else {
                raciones.acquire();
                System.out.println(n + " comio 1 racion. Raciones: " + raciones.availablePermits());

            }
        }
    }

    public void cocinar(String n) throws InterruptedException {
        ollaVacia.acquire();
        System.out.println(n + " Cocinando...");
        Thread.sleep(3000);
        avisarOllaLlena.release();
        raciones.release(cantRaciones);
    }
}
