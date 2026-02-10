/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp6;

import java.util.concurrent.Semaphore;

/**
 *
 * @author maiamaripil
 */
public class PuenteEstrecho {

    public static void main(String[] args) {

        PuenteMonitor p = new PuenteMonitor();

//simulacion llegada de pasajeros
        for (int i = 1; i <= 10; i++) {
            int o = (int) (Math.random() * 2) + 1;
            Thread coche = new Thread(new Coche("COCHE " + i, p, o));
            coche.start();
            try {
                Thread.sleep((int) (Math.random() * 3000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
}

class Coche implements Runnable {

    private String nombre;
    private PuenteMonitor recurso;
    private int opcion;

    public Coche(String n, PuenteMonitor p, int o) {
        this.nombre = n;
        this.recurso = p;
        opcion = o;
    }

    public void run() {
        try {
            switch (opcion) {
                case 1:
                    // System.out.println(nombre + " NORTE");
                    recurso.entrarNorte(nombre);
                    Thread.sleep(2000);
                    System.out.println(nombre + " salir");
                    recurso.salirNorte(nombre);
                    break;
                case 2:
                    // System.out.println(nombre + " SUR");
                    recurso.entrarSur(nombre);
                    Thread.sleep(2000);
                    System.out.println(nombre + " salir");
                    recurso.salirSur(nombre);
                    break;
            }

        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Puente {

    Semaphore pista = new Semaphore(1);
    private int cochesSur = 0;
    private int cochesNorte = 0;

    public void entrarNorte(String n) throws InterruptedException {
        synchronized (this) {
            cochesNorte++;
        }

        if (cochesNorte == 1) {
            System.out.println("Esperando disponibilidad de la pista autos Norte");
            pista.acquire();

        }
        System.out.println(n + ": ingreso a la pista desde el Norte." + cochesNorte);
        Thread.sleep(2000);
        salirNorte(n);

    }

    public void salirNorte(String n) throws InterruptedException {
        synchronized (this) {
            cochesNorte--;
        }
        System.out.println(n + ": salio de la pista desde el Norte. " + cochesNorte);
        if (cochesNorte == 0) {
            System.out.println("no hay mas autos desde el norte, libera la pista.");
            pista.release();
        }

    }

    public void entrarSur(String n) throws InterruptedException {
        synchronized (this) {
            cochesSur++;
        }
        if (cochesSur == 1) {
            System.out.println("Esperando disponibilidad de la pista autos Sur");
            pista.acquire();

        }

        System.out.println(n + ": ingreso a la pista desde el Sur." + cochesSur);
        Thread.sleep(2000);
        salirSur(n);
    }

    public void salirSur(String n) throws InterruptedException {
        synchronized (this) {
            cochesSur--;
        }
        System.out.println(n + ": salio de la pista desde el Sur. " + cochesSur);

        if (cochesSur == 0) {
            System.out.println("no hay mas autos desde el Sur, libera la pista.");
            pista.release();
        }

    }
}

class PuenteMonitor {

    boolean surUso = false;
    boolean norteUso = false;
    private int cochesSur = 0;
    private int cochesNorte = 0;
    private int esperaSur = 0;
    private int esperaNorte = 0;
    private int contador = 0;
    private Semaphore cruzar= new Semaphore(1);

    public synchronized void entrarNorte(String n) throws InterruptedException {
        while (surUso || (contador >= 10 && cochesSur > 0)) {
            wait();//espera a que cambie la condicion
        }

        cochesNorte++;
        contador++;
        norteUso = true;
        //cruzar.acquire();
        System.out.println(n + " está cruzando el puente desde el norte."+ cochesNorte);

    }

    public synchronized void salirNorte(String n) {

        cochesNorte--;
        
        System.out.println(n + ": salio de la pista desde el Norte. " + cochesNorte);
       // cruzar.release();
        if (cochesNorte == 0) {
            norteUso = false;
            if (contador >= 10 || cochesSur > 0) {
                contador = 0;
                System.out.println("ya pasaron + de 10 autos del norte y hay autos esperando del sur, libera la pista.");
                notifyAll();
            } else if (cochesSur == 0) {
                System.out.println("no hay mas autos desde el norte, libera la pista para el primero que llegue.");

                notifyAll();
                
            }
        }
    }

    public synchronized void entrarSur(String n) throws InterruptedException {
        while (norteUso || (contador >= 10 && cochesNorte > 0)) {
            wait();
        }
        cochesSur++;
        contador++;
        surUso = true;
       // cruzar.acquire();
        System.out.println(n + ": ingreso a la pista desde el Sur." + cochesSur);

    }

    public synchronized void salirSur(String n) {

        cochesSur--;
        
        System.out.println(n + ": salio de la pista desde el Sur. " + cochesSur);
        //cruzar.release();

        if (cochesSur == 0) {
            surUso = false;
            if (contador >= 10 && cochesNorte > 0) {
                contador = 0;
                System.out.println("ya pasaron + de 10 autos del sur y hay autos esperando del norte, libera la pista.");
                notifyAll();
            } else if (cochesNorte == 0) {
                System.out.println("no hay mas autos desde el sur, libera la pista para el primero que llegue.");
                notifyAll();
            }
        }

    }
}
