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
public class ProyectoSoftware {

    public static void main(String[] args) {

        GestionRecursos gR = new GestionRecursos(2, 1);

        for (int i = 1; i <= 5; i++) {
            Thread p = new Thread(new Programador("PROGRAMADOR " + i, gR));

            p.start();

            try {
                Thread.sleep((int) (Math.random() * 3000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
}

class Programador implements Runnable {

    private String nombre;
    private GestionRecursos recurso;

    public Programador(String n, GestionRecursos g) {
        this.nombre = n;
        this.recurso = g;
    }

    public void run() {
        try {
            while (true) {
                recurso.usarRecursos(nombre);
                Thread.sleep(3000);
                recurso.desocuparRecursos(nombre);
            }

        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class GestionRecursos {

    private int computadoras;
    private int libros;
    private int cEnUso = 0;
    private int lEnUso = 0;

    private Lock lock = new ReentrantLock();
    private Condition recursosDisponibles = lock.newCondition();
  

    public GestionRecursos(int c, int l) {
        this.computadoras = c;
        this.libros = l;
    }

    public void usarRecursos(String n) throws InterruptedException {
        lock.lock();
        try {
            while (computadoras == cEnUso || libros==lEnUso) {
                System.out.println(n + " NO HAY RECURSOS DISPONIBLES. DEBE ESPERAR.");
                recursosDisponibles.await();

            }
            cEnUso++;
            lEnUso++;
           
            System.out.println(n + " ADQUIRIO TODOS LOS RECURSOS.");
        } finally {
            lock.unlock();
        }
    }

    public void desocuparRecursos(String n) throws InterruptedException {
        lock.lock();
        try {
            cEnUso--;
            lEnUso--;
            System.out.println(n + " DESOCUPA RECURSOS.");
            recursosDisponibles.signal();
            
        } finally {
            lock.unlock();
        }
    }
}
