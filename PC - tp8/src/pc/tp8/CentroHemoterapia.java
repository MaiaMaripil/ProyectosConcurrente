/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp8;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author famil
 */
public class CentroHemoterapia {

    public static void main(String[] args) {

        GestionCentro c = new GestionCentro(1, 3);

        for (int i = 1; i <= 10; i++) {
            Thread p = new Thread(new Persona("PERSONA " + i, c));

            p.start();

            try {
                Thread.sleep((int) (Math.random() * 2000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
}

class Persona implements Runnable {

    private String nombre;
    private GestionCentro recurso;

    public Persona(String n, GestionCentro c) {
        this.nombre = n;
        this.recurso = c;
    }

    public void run() {
        try {
            recurso.donarSangre(nombre);
            Thread.sleep(5000);
            recurso.salirCentro(nombre);

        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class GestionCentro {

    private int camillas;
    private int enEspera;
    private int revistas;
    private Lock lock = new ReentrantLock();
    private Condition camillaLibre = lock.newCondition();
    private Condition revistaLibre = lock.newCondition();

    public GestionCentro(int c, int r) {
        camillas = c;
        revistas = r;

    }

    public void donarSangre(String n) throws InterruptedException {
        lock.lock();
        boolean tieneRevista = false;
        System.out.println(n + " Entro al centro");
        try {
            while (camillas == 0) {
                if (!tieneRevista && revistas > 0) {
                    revistas--;
                    System.out.println(n + " Esperando con revista");
                    tieneRevista = true;
                } else {
                    if (tieneRevista) {
                        System.out.println(n + " ya tenia revista");
                    } else {
                        System.out.println(n + " Esperando viendo tv");
                    }
                }

                camillaLibre.await();
            }
            if (tieneRevista) {
                revistas++;
                System.out.println(n + " Deja revista");
                camillaLibre.signalAll();
            }
            camillas--;
            System.out.println(n + " Realizando extraccion...");

        } finally {
            lock.unlock();
        }

    }

    public void salirCentro(String n) throws InterruptedException {
        lock.lock();
        try {
            camillas++;
            System.out.println(n + " Salio del centro");
            camillaLibre.signal();

        } finally {
            lock.unlock();
        }

    }
}

