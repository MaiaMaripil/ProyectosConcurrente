/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp7;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author famil
 */
public class Cocina {

    public static void main(String[] args) {

        GestionRecursos gR = new GestionRecursos();
        Thread c1 = new Thread(new Cocinero("COCINERO CP ", gR, 1));
        Thread c2 = new Thread(new Cocinero("COCINERO EV ", gR, 2));
        Thread c3 = new Thread(new Cocinero("COCINERO PP ", gR, 3));
        c1.start();
        c2.start();
        c3.start();

    }
}

class Cocinero implements Runnable {

    private String nombre;
    private GestionRecursos recurso;
    private int tipo;

    public Cocinero(String n, GestionRecursos g, int t) {
        this.nombre = n;
        this.recurso = g;
        this.tipo = t;
    }

    public void run() {
        try {
            switch (tipo) {
                case 1:
                    while (true) {
                        recurso.cocineroCP(nombre);
                        Thread.sleep(3000);
                        recurso.reponer(nombre, tipo);
                    }

                case 2:
                    while (true) {
                        recurso.cocineroEV(nombre);
                        Thread.sleep(3000);
                        recurso.reponer(nombre, tipo);
                    }
                case 3:
                    while (true) {
                        recurso.cocineroPP(nombre);
                        Thread.sleep(3000);
                        recurso.reponer(nombre, tipo);
                    }
            }

        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class GestionRecursos {

    private int carne=1;
    private int vegetales=1;
    private int pasta=1;

    private Lock lock = new ReentrantLock();
    private Condition ingredientesDisponibles = lock.newCondition();

    public void cocineroCP(String n) throws InterruptedException {

        lock.lock();
        try {
            while (vegetales == 0 || pasta == 0) {
                System.out.println(n + " NO HAY SUFICIENTES RECURSOS DISPONIBLES. DEBE ESPERAR.");
                ingredientesDisponibles.await();
            }
            vegetales--;
            pasta--;
            System.out.println(n + " ADQUIRIO LOS INGREDIENTES, cocinando...");
        } finally {
            lock.unlock();
        }
    }

    public void cocineroEV(String n) throws InterruptedException {
        lock.lock();
        try {
            while (carne == 0 || pasta == 0) {
                System.out.println(n + " NO HAY SUFICIENTES RECURSOS DISPONIBLES. DEBE ESPERAR.");
                ingredientesDisponibles.await();
            }
            carne--;
            pasta--;
            System.out.println(n + " ADQUIRIO LOS INGREDIENTES, cocinando...");
        } finally {
            lock.unlock();
        }
    }

    public void cocineroPP(String n) throws InterruptedException {
        lock.lock();
        try {
            while (carne == 0 || vegetales == 0) {
                System.out.println(n + " NO HAY SUFICIENTES RECURSOS DISPONIBLES. DEBE ESPERAR.");
                ingredientesDisponibles.await();
            }
            carne--;
            vegetales--;
            System.out.println(n + " ADQUIRIO LOS INGREDIENTES, cocinando...");
        } finally {
            lock.unlock();
        }
    }

    public void reponer(String n, int t) throws InterruptedException{
        lock.lock();
        try {
            switch (t) {
                case 1:
                    System.out.println(n + " REPONE LOS INGREDIENTES(vegetales,pasta");
                    vegetales++;
                    pasta++;
                    ingredientesDisponibles.signalAll();
                    break;
                case 2:
                    System.out.println(n + " REPONE LOS INGREDIENTES(carne,pasta");
                    carne++;
                    pasta++;
                    ingredientesDisponibles.signalAll();
                    break;
                case 3:
                    System.out.println(n + " REPONE LOS INGREDIENTES(carne,vegetales");
                    vegetales++;
                    carne++;
                    ingredientesDisponibles.signalAll();
                    break;
            }
        } finally {
            lock.unlock();

        }
    }
}
