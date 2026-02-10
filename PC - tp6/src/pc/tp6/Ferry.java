/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp6;

/**
 *
 * @author maiamaripil
 */
public class Ferry {

    public static void main(String[] args) {

        GestionFerry gF = new GestionFerry(20);
        Thread f= new Thread(new FerryHilo("FERRY",gF));
        f.start();

        for (int i = 1; i <= 10; i++) {

            Thread p = new Thread(new Pasajero("PASAJERO " + i, gF));
            int o = (int) (Math.random() * 3) + 2;
            Thread a = new Thread(new Automovil("AUTOMOVIL " + i, gF, o));
            p.start();
            a.start();
            try {
                Thread.sleep((int) (Math.random() * 3000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
}

class FerryHilo implements Runnable {

    private String nombre;
    private GestionFerry recurso;

    public FerryHilo(String n, GestionFerry g) {
        this.nombre = n;
        this.recurso = g;
    }

    public void run() {
        try {
            while (true) {
                Thread.sleep(10000);
                recurso.llegarDestino(nombre);
            }
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Pasajero implements Runnable {

    private String nombre;
    private GestionFerry recurso;

    public Pasajero(String n, GestionFerry g) {
        this.nombre = n;
        this.recurso = g;
    }

    public void run() {
        try {
            recurso.embarcarPasajero(nombre);
            recurso.desembarcarPasajero(nombre);

        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Automovil implements Runnable {

    private String nombre;
    private GestionFerry recurso;
    private int espacio;

    public Automovil(String n, GestionFerry a, int e) {
        this.nombre = n;
        this.recurso = a;
        this.espacio = e;
    }

    public void run() {
        try {
            recurso.embarcarAutomovil(nombre, espacio);
            recurso.desembarcarAutomovil(nombre, espacio);

        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class GestionFerry {

    private int capacidad;
    private int lugarOcupado = 0;
    private boolean fin = false;

    public GestionFerry(int c) {
        this.capacidad = c;
    }

    public synchronized void embarcarPasajero(String n) throws InterruptedException {
        while (capacidad == lugarOcupado || fin) {
            //no hay lugar, debe esperar
            System.out.println(n + " NO PUEDE EMBARCAR.");
            wait();

        }
        lugarOcupado++;
        System.out.println(n + " hay lugar, embarcando... . " + lugarOcupado);

    }

    public synchronized void desembarcarPasajero(String n) throws InterruptedException {
        while (!fin) {
            System.out.println(n + " TODAVIA NO LLEGA A DESTINO. No puede desembarcar.");
            wait();
        }

        lugarOcupado--;
        System.out.println(n + " llego a su lugar de destino, desembarca . " + lugarOcupado);
        notifyAll();

    }

    public synchronized void embarcarAutomovil(String n, int e) throws InterruptedException {

        while (lugarOcupado + e > capacidad || fin) {
            //no hay lugar, debe esperar
            System.out.println(n + " NO PUEDE EMBARCAR.");
            wait();

        }
        lugarOcupado = lugarOcupado + e;
        System.out.println(n + " hay lugar suficiente, embarcando... . " + lugarOcupado);

    }

    public synchronized void desembarcarAutomovil(String n, int e) throws InterruptedException {
        while (!fin) {
            System.out.println(n + " TODAVIA NO LLEGA A DESTINO. No puede desembarcar.");
            wait();
        }

        lugarOcupado = lugarOcupado - e;
        System.out.println(n + " llego a su lugar de destino, desembarca . " + lugarOcupado);
        notifyAll();

    }

    public synchronized void llegarDestino(String n) throws InterruptedException {
        System.out.println(n + "LUGAR OCUPADO: . " + lugarOcupado);
        fin = true;

        System.out.println(n + " LLEGO A DESTINO . " + lugarOcupado);
        notifyAll();

    }

}
