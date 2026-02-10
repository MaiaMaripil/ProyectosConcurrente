/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp6;

/**
 *
 * @author famil
 */
public class SalaDeEstudio {

    public static void main(String[] args) {

        SalaMonitor s = new SalaMonitor(2);


        for (int i = 1; i <= 10; i++) {
            Thread est = new Thread(new Estudiante("ESTUDIANTE " + i, s));
            est.start();
            try {
                Thread.sleep((int) (Math.random() * 3000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
}

class Estudiante implements Runnable {

    private String nombre;
    private SalaMonitor recurso;

    public Estudiante(String n, SalaMonitor s) {
        this.nombre = n;
        this.recurso = s;
    }

    public void run() {
        try {
            recurso.entrarSala(nombre);
            Thread.sleep(2000);
            System.out.println(nombre + " salir");
            recurso.salirSala(nombre);

        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class SalaMonitor {

    private int cantMesas;
    private int cont = 0;

    public SalaMonitor(int c) {
        cantMesas = c;
    }

    public synchronized void entrarSala(String n) throws InterruptedException {
        while (cantMesas == cont) {
            //Todas las mesas estan ocupadas, debe esperar
            System.out.println(n + " entro a la sala pero no hay mesas libres. Espera a que se desocupe alguna mesa.");
            wait();

        }
        cont++;
        System.out.println(n + " hay mesas libres, estudiando... . " + cont);

    }

    public synchronized void salirSala(String n) {

        cont--;
        System.out.println(n + " termino de estudiar,deja su mesa libre . " + cont);
        notifyAll();

    }

}
