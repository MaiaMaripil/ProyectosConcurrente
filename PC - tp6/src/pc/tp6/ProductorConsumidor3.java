/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp6;

/**
 *
 * @author famil
 */
public class ProductorConsumidor3 {
    
    public static void main(String[] args) {

        Almacen s = new Almacen();


          for (int i = 1; i <= 5; i++) {
            
            Thread p = new Thread(new Productor("PRODUCTOR " + i, s));
            Thread c = new Thread(new Consumidor("CONSUMIDOR "+i, s));
            p.start();
            c.start();
            try {
                Thread.sleep((int) (Math.random() * 3000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
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
            while (true) {
                recurso.consumir(nombre);
                Thread.sleep((int) (Math.random() * 5000));
            }
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Almacen {

    //Capacidad ilimitada
    private int productos = 0;

    

    public synchronized void producir(String n) throws InterruptedException {
      

        System.out.println(n + " Genera el producto." + productos);
        Thread.sleep(2000);
        productos++;
        System.out.println(n + " termina de producir, y coloca el producto." + productos);
        notifyAll();

    }

    public synchronized void consumir(String n) throws InterruptedException {
        while (productos == 0) {
            System.out.println(n + " no hay productos para consumir, espera a que coloquen algun producto." + productos);
            wait();

        }
        productos--;
        System.out.println(n + " consumio un producto . " + productos);
        notifyAll();

    }
}

