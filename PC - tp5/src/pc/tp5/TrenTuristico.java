/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp5;

import java.util.concurrent.Semaphore;

/**
 *
 * @author maiamaripil
 */
public class TrenTuristico {

    public static void main(String[] args) {
        GestionTren t = new GestionTren(5);
        VendedorTickets v = new VendedorTickets("VENDEDOR", t);
        ControlTren ct = new ControlTren("CONTROL", t);

        Thread t1 = new Thread(v);
        Thread t2 = new Thread(ct);
        t1.start();
        t2.start();

//simulacion llegada de pasajeros
        for (int i = 1; i <= 10; i++) {
            Thread pasajero = new Thread(new Pasajero("PASAJERO " + i, t));
            pasajero.start();
            try {
                Thread.sleep((int) (Math.random() * 3000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
}

class Pasajero implements Runnable {

    private String nombre;
    private GestionTren recurso;

    public Pasajero(String n, GestionTren t) {
        this.nombre = n;
        this.recurso = t;
    }

    public void run() {
        try {
            recurso.comprarTicket(nombre);
            recurso.subirTren(nombre);
            recurso.bajarTren(nombre);

        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class VendedorTickets implements Runnable {

    private String nombre;
    private GestionTren recurso;

    public VendedorTickets(String n, GestionTren t) {
        this.nombre = n;
        this.recurso = t;

    }

    public void run() {
        try {
            while (true) {

                recurso.venderTickets(nombre);
            }
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class ControlTren implements Runnable {

    private String nombre;
    private GestionTren recurso;

    public ControlTren(String n, GestionTren t) {
        this.nombre = n;
        this.recurso = t;

    }

    public void run() {
        try {
            while (true) {

                recurso.iniciarRecorrido(nombre);
            }
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class GestionTren {

    Semaphore capacidad;
    Semaphore trenVacio = new Semaphore(1);
    Semaphore trenLleno = new Semaphore(0);
    Semaphore comprarTicket = new Semaphore(0);
    Semaphore clienteEspera = new Semaphore(0);
    Semaphore finRecorrido = new Semaphore(0);
    int pasajeros = 0;
    int capacidadMax;

    public GestionTren(int n) {
        this.capacidad = new Semaphore(n);
this.capacidadMax=n;
    }

    public void iniciarRecorrido(String n) throws InterruptedException {
        trenLleno.acquire();
        System.out.println(n + ":EL TREN ESTA LLENO, iniciando recorrido");
        Thread.sleep(5000);
        System.out.println(n + ": Termino el recorrido");
        finRecorrido.release(pasajeros);

    }

    public void subirTren(String n) throws InterruptedException {
       

        capacidad.acquire();
        System.out.println(n + ": Subio al tren");
        synchronized (this) {
            pasajeros++;
            if (capacidad.availablePermits() == 0) {
                trenLleno.release();
            }
        }
    }

    public void bajarTren(String n) throws InterruptedException {

        finRecorrido.acquire();

        System.out.println(n + ": bajo del tren");
        
        synchronized (this) {
            pasajeros--;
            if (pasajeros == 0) {
                System.out.println( "EL TREN ESTA VACIO");
                
                capacidad.release(capacidadMax);
            }
        }
    }

    public void venderTickets(String n) throws InterruptedException {
        clienteEspera.acquire();

        System.out.println(n + ": Vende ticket");
        comprarTicket.release();

    }

    public void comprarTicket(String n) throws InterruptedException {
        clienteEspera.release();
        comprarTicket.acquire();
        System.out.println(n + ": compro ticket");

    }
}
