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
public class SalaMuseo {

    public static void main(String[] args) {

        GestionSala gS = new GestionSala();
        Thread temp=new Thread(new Temperatura("TEMPERATURA",gS));
        temp.start();
        
        for (int i = 1; i <= 10; i++) {
            int t = (int) (Math.random() * 2) + 1;
            Thread p = new Thread(new Persona("PERSONA " + i, gS, t));

            p.start();

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
    private GestionSala recurso;
    private int tipo;

    public Persona(String n, GestionSala g, int t) {
        this.nombre = n;
        this.recurso = g;
        this.tipo = t;
    }

    public void run() {
        try {
            switch (tipo) {
                case 1:
                    recurso.entrarSala(nombre);
                    Thread.sleep(5000);
                    recurso.salirSala(nombre);
                    break;
                case 2:
                    recurso.entrarSalaJubilado(nombre);
                    Thread.sleep(5000);
                    recurso.salirSala(nombre);
            }

        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Temperatura implements Runnable {

    private String nombre;
    private GestionSala recurso;
    private int t = 0;

    public Temperatura(String n, GestionSala g) {
        this.nombre = n;
        this.recurso = g;
    }

    public void run() {
        try {
            while (true) {
                t = (int) (Math.random() * 50) + 1;
                recurso.notificarTemperatura( t );
                Thread.sleep(3000);
            }

        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class GestionSala {

    private int capacidad = 5;
    private int enSala = 0;
    private int tUmbral = 30;
    private int temperatura = 20;
    private int jubiladosEsperando = 0;

    private Lock lock = new ReentrantLock();
    private Condition puedeEntrar = lock.newCondition();
    private Condition jubilados = lock.newCondition();

    public void entrarSala(String n) throws InterruptedException {
        lock.lock();
        try {
            while (jubiladosEsperando > 0 || enSala >= capacidad) {
                System.out.println(n + " DEBE ESPERAR PARA INGRESAR.");
                puedeEntrar.await();
            }
            System.out.println(n + " Puede entrar a la sala");
            enSala++;
        } finally {
            lock.unlock();
        }
    }

    public void entrarSalaJubilado(String n) throws InterruptedException {
        lock.lock();
        try {
            while (enSala >= capacidad ) {
                System.out.println(n + "jubilada: SALA LLENA. DEBE ESPERAR PARA INGRESAR.");
                jubiladosEsperando++;
                jubilados.await();
                
            }
            
            System.out.println(n + "jubilada: PUEDE INGRESAR.");
            jubiladosEsperando--;
            enSala++;

        } finally {
            lock.unlock();
        }
    }

    public void salirSala(String n) throws InterruptedException {
        lock.lock();
        try {
            enSala--;
            System.out.println(n + " salio de la sala.");
            if (jubiladosEsperando > 0) {
                System.out.println(n + "Hay jubilados esperando. Despierta a uno de esos hilos.");
                jubilados.signal();
            } else {
                System.out.println(n + "No hay jubilados esperando. Despierta a hilos comunes.");
                puedeEntrar.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public void notificarTemperatura(int t) throws InterruptedException {
        lock.lock();
        try {
            temperatura = t;
            System.out.println("NOTIFICACION: Temperatura actual ." + temperatura);
            if (temperatura >= tUmbral) {
                System.out.println("NOTIFICACION: Temperatura elevada! se reduce la capacidad de la sala.");
                capacidad = 3;
            } else {
                System.out.println("NOTIFICACION: temperatura normal.");
                capacidad = 5;
            }
            puedeEntrar.signal();
            jubilados.signal();
        } finally {
            lock.unlock();
        }
    }
}
