/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.barberodormilon;

import java.util.concurrent.Semaphore;

/**
 *
 * @author maiamaripil
 */
public class BarberoDormilon {

    public static void main(String[] args) {
        Barberia b = new Barberia(3);

        Barbero barbero = new Barbero("BARBERO", b);
        Thread t1 = new Thread(barbero);
        t1.start();

        for (int i = 1; i <= 10; i++) {
            Thread cliente = new Thread(new Cliente("CLIENTE " + i, b));
            cliente.start();
            try {
                Thread.sleep((int) (Math.random() * 3000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

class Barbero implements Runnable {

    private String nombre;
    private Barberia recurso;

    public Barbero(String n, Barberia b) {
        this.nombre = n;
        this.recurso = b;
    }

    public void run() {
        try {
            while (true) {
                recurso.atender(nombre);
                Thread.sleep(500);
                recurso.terminarAtender(nombre);
            }
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Cliente implements Runnable {

    private String nombre;
    private Barberia recurso;

    public Cliente(String n, Barberia b) {
        this.nombre = n;
        this.recurso = b;
    }

    public void run() {
        try {
            boolean encontroLugar = false;
            while (!encontroLugar) {
                encontroLugar = recurso.irBarberia(nombre);
                if (!encontroLugar) {
                    Thread.sleep(1000); //espera afuera para despues volver
                }

            }
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

//simplificar y modularizar
class Barberia {

    Semaphore sillonLibre = new Semaphore(1);//semaforo para que el cliente sepa si el sillon esta desocupado
    Semaphore clienteEnSillon = new Semaphore(0); //semaforo del barbero, se libera si hay un cliente en el sillon
    Semaphore corteTerminado = new Semaphore(0);//semaforo del barbero, avisa al cliente que termino el corte
    Semaphore sillasEnEspera;

    public Barberia(int n) {
        this.sillasEnEspera = new Semaphore(n);
    }

    public boolean irBarberia(String n) throws InterruptedException {
        boolean hayLugar = true;

        System.out.println(n + ": verifica si el sillon esta desocupado");

        if (sillonLibre.tryAcquire()) {
            System.out.println(n + ": el sillon esta desocupado, avisa al barbero ");
            ocuparSillon(n);
        } else if (sillasEnEspera.tryAcquire()) {
            System.out.println(n + ": Sillon ocupado, espera en las sillas ");
            sillonLibre.acquire();
            sillasEnEspera.release();//libera la silla en espera
            System.out.println(n + ": el sillon se desocupo, avisa al barbero ");
            ocuparSillon(n);
        } else {
            hayLugar = false;
        }
        return hayLugar;
     
    }

    public void ocuparSillon(String n) throws InterruptedException {

        clienteEnSillon.release();//avisa al barbero
        corteTerminado.acquire(); //Cliente espera a que termine el corte
        System.out.println(n + ": se retira de la berberia");
        sillonLibre.release();//se libera el sillon para que pueda usarlo otro cliente
    }

    public void atender(String n) throws InterruptedException {
        clienteEnSillon.acquire(); //En espera de que llegue algun cliente
        System.out.println(n + ": atiende al cliente y realiza el corte");
    }

    public void terminarAtender(String n) throws InterruptedException {
        System.out.println(n + ": corte terminado, avisa al cliente que puede retirarse");
        corteTerminado.release(); //Avisa al cliente que se puede retirar

    }
}
