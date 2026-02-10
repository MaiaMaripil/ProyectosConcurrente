/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp6;

/**
 *
 * @author famil
 */
public class ProductorConsumidor {

    public static void main(String[] args) {

        Almacen s = new Almacen(3);

        Thread p = new Thread(new Productor("PRODUCTOR",s));
        Thread c = new Thread(new Consumidor("CONSUMIDOR",s));

        p.start();
        c.start();


        /*  for (int i = 1; i <= 10; i++) {
            
            int o = (int) (Math.random() * 2) + 1;
            Thread est = new Thread(new Estudiante("ESTUDIANTE " + i, s));
            est.start();
            try {
                Thread.sleep((int) (Math.random() * 3000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }*/
    }
}

class Productor implements Runnable {

    private String nombre;
    private Almacen recurso;

    public Productor(String n, Almacen a) {
        this.nombre = n;
        this.recurso = a;
    }

    public void run() {
        try {
            while(true){
            recurso.producir(nombre);
            }
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Consumidor implements Runnable {

    private String nombre;
    private Almacen recurso;

    public Consumidor(String n, Almacen a) {
        this.nombre = n;
        this.recurso = a;
    }

    public void run() {
        try {
            while(true){
            recurso.consumir(nombre);
            Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Almacen {

    private int espacio;
    private int cont = 0;

    public Almacen(int c) {
        this.espacio = c;
    }

    public synchronized void producir(String n) throws InterruptedException {
        while (espacio == cont) {
            //no hay lugar disponible, debe esperar
            System.out.println(n + " no hay lugar disponible. Espera a que se desocupe.");
            wait();
        }

        System.out.println(n + " hay lugar, genera el producto." + cont);
        Thread.sleep(2000);
        cont++;
        System.out.println(n + " termina de producir, y coloca el producto." + cont);
        notifyAll();

    }

    public synchronized void consumir(String n) throws InterruptedException {
        while (cont == 0) {
            System.out.println(n + " no hay productos para consumir, espera a que coloquen algun producto." + cont);
            wait();

        }
        cont--;
        System.out.println(n + " consumio un producto . " + cont);
        notifyAll();

    }
}
