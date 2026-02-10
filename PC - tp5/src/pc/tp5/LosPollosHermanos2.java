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
public class LosPollosHermanos2 {

    public static void main(String[] args) {
        Cafeteria c = new Cafeteria();

        Mozo m1 = new Mozo("Mozo", c);
        Thread t1 = new Thread(m1);
        t1.start();
        Cocinero c1= new Cocinero("COCINERO",c);
        Thread t2= new Thread(c1);
        t2.start();

//simulacion llegada de empleados
        for (int i = 1; i <= 10; i++) {
            int o=(int) (Math.random() * 3) + 1;
            Thread empleado = new Thread(new Empleado("EMPLEADO " + i, c,o));
            empleado.start();
            try {
                Thread.sleep((int) (Math.random() * 3000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
}

class Mozo implements Runnable {

    private String nombre;
    private Cafeteria recurso;

    public Mozo(String n, Cafeteria c) {
        this.nombre = n;
        this.recurso = c;
    }

    public void run() {
        try {
            while (true) {
                recurso.atender(nombre);
            }
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}
class Cocinero implements Runnable {

    private String nombre;
    private Cafeteria recurso;

    public Cocinero(String n, Cafeteria c) {
        this.nombre = n;
        this.recurso = c;
        
    }

    public void run() {
        try {
            while (true) {
                recurso.cocinar(nombre);
            }
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Empleado implements Runnable {

    private String nombre;
    private Cafeteria recurso;
    private int opcion;

    public Empleado(String n, Cafeteria c, int o) {
        this.nombre = n;
        this.recurso = c;
        this.opcion = o;
    }

    public void run() {
        try {
            recurso.irCafeteria(nombre, opcion);
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Cafeteria {

    Semaphore espacioEmpl = new Semaphore(2);
    Semaphore clienteEsperandoBebida = new Semaphore(0);
    Semaphore bebidaServida = new Semaphore(0);
    Semaphore clienteEsperandoComida = new Semaphore(0);
    Semaphore comidaServida = new Semaphore(0);

    public void irCafeteria(String n, int opcion) throws InterruptedException {
        System.out.println(n + " verifica si hay lugar en la cafeteria");
        if (espacioEmpl.tryAcquire()) {
            switch (opcion) {
                case 1:
                    System.out.println(n + " solo desea tomar algo, entonces avisa al mozo que esta esperando a ser atendido");
                    clienteEsperandoBebida.release();
                    bebidaServida.acquire();
                    System.out.println(n + " : se le ha servido su bebida");
                    Thread.sleep(2000);
                    System.out.println(n + " agradece al mozo y deja la silla libre");
                    espacioEmpl.release();
                    break;
                case 2:System.out.println(n + " solo desea comer algo, entonces selecciona algo del menu");
                    clienteEsperandoComida.release();
                    comidaServida.acquire();
                    System.out.println(n + " : se le ha servido su comida");
                    Thread.sleep(2000);
                    System.out.println(n + " agradece al cocinero y deja la silla libre");
                    espacioEmpl.release();
                    break;
                case 3:System.out.println(n + " desea comer y beber algo, entonces avisa al mozo para elegir la bebida");
                    clienteEsperandoBebida.release();
                    bebidaServida.acquire();
                    System.out.println(n + " : se le ha servido su bebida, ahora puede pedir la comida");
                    clienteEsperandoComida.release();
                    comidaServida.acquire();
                    System.out.println(n + " : se le ha servido su comida");
                    Thread.sleep(2000);
                    System.out.println(n + " agradece al mozo y cocinero y deja la silla libre");
                    espacioEmpl.release();
                    
            }

           

        } else {
            System.out.println("No hay lugar en la cafeteria y el empleado se va");
        }

    }

    public void atender(String n) throws InterruptedException {

        clienteEsperandoBebida.acquire();
        System.out.println(n + " atiende al empleado, este selecciona lo que desea beber del menu");
        Thread.sleep(1000);
        System.out.println(n + " sirve la bebida");
        bebidaServida.release();
    }
    
     public void cocinar(String n) throws InterruptedException {

        clienteEsperandoComida.acquire();
        System.out.println(n + " Cocinero preparando comida");
        Thread.sleep(1000);
        System.out.println(n + " sirve la comida");
        comidaServida.release();
    }
    
}
