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
public class PiscinaComunitaria {

    public static void main(String[] args) {
        GestorPiscina p = new GestorPiscina(2);

        for (int i = 1; i <= 10; i++) {
            Thread persona = new Thread(new Persona("PERSONA " + i, p));
            persona.start();
            try {
                Thread.sleep((int) (Math.random() * 3000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

class Persona implements Runnable {

    private String nombre;
    private GestorPiscina recurso;

    public Persona(String n, GestorPiscina p) {
        this.nombre = n;
        this.recurso = p;
    }

    public void run() {
        try {
            recurso.irPiscina(nombre);
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class GestorPiscina {

    Semaphore espacioDisponible;

    public GestorPiscina(int n) {
        this.espacioDisponible = new Semaphore(n);
    }

    public void irPiscina(String n) throws InterruptedException {
        System.out.println(n + " verifica si hay lugar disponible");

        //Si no hay lugar en la piscina
        if (espacioDisponible.availablePermits() == 0) {
            System.out.println(n + " No hay lugar entonces debe esperar afuera");
            espacioDisponible.acquire();
            System.out.println(n + "Puede ingresar a la piscina ya que se libero un lugar");
            Thread.sleep(5000);
            System.out.println(n + ": deja su lugar disponible ");
            espacioDisponible.release();

        } else {
            System.out.println(n + ": Hay lugar entonces ingresa a la piscina ");
            espacioDisponible.acquire();
            Thread.sleep(5000);
            System.out.println(n + ": deja su lugar disponible ");
            espacioDisponible.release();

        }

    }

}
