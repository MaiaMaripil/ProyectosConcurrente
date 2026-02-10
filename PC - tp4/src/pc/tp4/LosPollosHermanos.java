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
public class LosPollosHermanos {

    public static void main(String[] args) {
        Cafeteria c = new Cafeteria();

        Mozo m1 = new Mozo("Mozo", c);
        Empleado e1 = new Empleado("Empleado1", c);
        Empleado e2 = new Empleado("Empleado2", c);
        Empleado e3 = new Empleado("Empleado3", c);

        Thread t1 = new Thread(m1);
        Thread t2 = new Thread(e1);
        Thread t3 = new Thread(e2);
        Thread t4 = new Thread(e3);

        t1.start();
        t2.start();
        try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        t3.start();
        t4.start();

    }
}

class Mozo implements Runnable{

    private String nombre;
    private Cafeteria recurso;

    public Mozo(String n, Cafeteria c) {
        this.nombre = n;
        this.recurso = c;
    }

    public void run() {
        try {
            recurso.atender(nombre);
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Empleado implements Runnable {

    private String nombre;
    private Cafeteria recurso;

    public Empleado(String n, Cafeteria c) {
        this.nombre = n;
        this.recurso = c;
    }

    public void run() {
        try {
            recurso.irCafeteria(nombre);
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Cafeteria {

    Semaphore espacioEmpl = new Semaphore(1);
    Semaphore avisarMozo = new Semaphore(0);
    Semaphore comidaServida = new Semaphore(0);
    Semaphore esperaComida = new Semaphore(0);

    public void irCafeteria(String n) throws InterruptedException {
        System.out.println(n + " verifica si hay lugar en la cafeteria");
        if (espacioEmpl.tryAcquire()) {
            
            System.out.println(n + " hay lugar entonces avisa al mozo que esta esperando a ser atendido");
            avisarMozo.release();
            comidaServida.acquire();
            System.out.println(n + " : se le ha servido su comida, puede empezar a comer");
            Thread.sleep(1000);
            System.out.println(n + " agradece al mozo y deja la silla libre");
            espacioEmpl.release();

        } else {
            System.out.println("No hay lugar en la cafeteria y el empleado se va");
        }

    }

    public void atender(String n) throws InterruptedException {
        int cantEmpl=0;
        while(cantEmpl<3){
        avisarMozo.acquire();
         System.out.println(n + " atiende al empleado, este selecciona lo que desea pedir del menu");
        Thread.sleep(1000);
        System.out.println(n + " sirve la comida");
        comidaServida.release();
        cantEmpl++;
        }
    }
}
