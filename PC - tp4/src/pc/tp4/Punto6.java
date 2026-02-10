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
public class Punto6 {

    public static void main(String[] args) {
        Proceso p = new Proceso();

        Cliente c1 = new Cliente("Pasajero", p);
        Taxi taxi = new Taxi("Taxista", p);

        Thread t1 = new Thread(c1);
        Thread t2 = new Thread(taxi);

        t1.start();

        t2.start();

    }
}

class Cliente implements Runnable {

    private String nombre;
    private Proceso p;

    public Cliente(String n, Proceso x) {
        this.nombre = n;
        this.p = x;
    }

    public void run() {
        try {
            p.tomarTaxi(nombre);
        } catch (InterruptedException e) {
            System.out.println("ERROR");
        }
    }
}

class Taxi implements Runnable {

    private String nombre;
    private Proceso p;

    public Taxi(String n, Proceso x) {
        this.nombre = n;
        this.p = x;
    }

    public void run() {
        try {
            p.esperarCliente(nombre);
        } catch (InterruptedException e) {
            System.out.println("ERROR");
        }
    }
}

class Proceso {

    Semaphore enEspera = new Semaphore(0);//de taxi
    Semaphore viajeTerminado = new Semaphore(0);

    public void tomarTaxi(String n) throws InterruptedException {
        System.out.println(n + " esta pidiendo un taxi");
        enEspera.release();
        System.out.println(n + " subio al taxi, en espera de que termine el viaje");
        viajeTerminado.acquire();
        System.out.println("El viaje ha terminado");

    }

    public void esperarCliente(String n) throws InterruptedException {
        System.out.println(n + " esta durmiendo, esperando que un cliente lo solicite");
        enEspera.acquire();
        System.out.println(n + " comienza a conducir");
        Thread.sleep(1000);
        System.out.println("Viaje terminado. " + n + " avisa al pasajero.");
        viajeTerminado.release();

    }
}
