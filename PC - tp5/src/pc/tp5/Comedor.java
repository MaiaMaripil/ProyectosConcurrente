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
public class Comedor {

    public static void main(String[] args) {
        GestionComedor c = new GestionComedor();

//simulacion llegada de animales
        for (int i = 1; i <= 10; i++) {
            Thread perro = new Thread(new Perro("PERRO " + i, c));
            perro.start();
            Thread gato = new Thread(new Gato("GATO " + i, c));
            gato.start();
            try {
                Thread.sleep((int) (Math.random() * 3000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
}

class Perro implements Runnable {

    private String nombre;
    private GestionComedor recurso;

    public Perro(String n, GestionComedor c) {
        this.nombre = n;
        this.recurso = c;
    }

    public void run() {
        try {
            recurso.comerPerro(nombre);

        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Gato implements Runnable {

    private String nombre;
    private GestionComedor recurso;

    public Gato(String n, GestionComedor c) {
        this.nombre = n;
        this.recurso = c;

    }

    public void run() {
        try {
            
                recurso.comerGato(nombre);
            
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class GestionComedor {

    Semaphore capacidad = new Semaphore(5);
    Semaphore habilitarPerros = new Semaphore(1);
    Semaphore habilitarGatos = new Semaphore(0);
    private int topePerros = 5;
    private int topeGatos = 5;

    public void comerPerro(String n) throws InterruptedException {

        habilitarPerros.acquire();
        topePerros--;
        if (topePerros > 0) {
            habilitarPerros.release();
        }
        System.out.println(n + ": esta habilitado, ahora verifica si hay comederos disponibles");
        capacidad.acquire(2);
        System.out.println(n + ":comiendo. TOPE: " + topePerros+" CAPACIDAD: "+capacidad.availablePermits());
        Thread.sleep(2000);
        System.out.println(n + ":termino de comer. ");
        capacidad.release(2);
        if (topePerros == 0 && capacidad.availablePermits() == 5) {
            System.out.println("TURNO DE GATOS");
            topePerros = 5;
            habilitarGatos.release();

        }

    }

    public void comerGato(String n) throws InterruptedException {

        habilitarGatos.acquire();

        topeGatos--;
        if (topeGatos > 0) {
            habilitarGatos.release();
        }
        System.out.println(n + ": esta habilitado, ahora verifica si hay comederos disponibles");
        capacidad.acquire();
        System.out.println(n + ":comiendo. TOPE: " + topeGatos);
        Thread.sleep(2000);
        System.out.println(n + ":termino de comer. ");
        capacidad.release();
        if (topeGatos == 0 && capacidad.availablePermits() == 5) {
            System.out.println("TURNO DE PERROS");
            habilitarPerros.release();
            topeGatos = 5;
        }

    }
}
