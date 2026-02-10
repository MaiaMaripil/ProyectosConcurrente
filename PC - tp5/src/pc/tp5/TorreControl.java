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
public class TorreControl {

    public static void main(String[] args) {

        Pista p = new Pista();

//simulacion llegada de pasajeros
        for (int i = 1; i <= 20; i++) {
            int o = (int) (Math.random() * 2) + 1;
            Thread avion = new Thread(new Avion("AVION " + i, p, o));
            avion.start();
            try {
                Thread.sleep((int) (Math.random() * 3000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
}

class Avion implements Runnable {

    private String nombre;
    private Pista recurso;
    private int opcion;

    public Avion(String n, Pista p, int o) {
        this.nombre = n;
        this.recurso = p;
        opcion = o;
    }

    public void run() {
        try {
            switch (opcion) {
                case 1:
                    System.out.println(nombre+" DESPEGAR");
                    recurso.permisoDespegar(nombre);
                    break;
                case 2:
                    System.out.println(nombre+" ATERRIZAR");
                    recurso.permisoAterrizar(nombre);
                    break;
            }

        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Pista {

    Semaphore capacidad = new Semaphore(1);
    Semaphore turnoDespegar = new Semaphore(0);
    Semaphore turnoAterrizar = new Semaphore(0);
    Semaphore cantAterrizajes = new Semaphore(0);
    Semaphore esperandoAterrizar = new Semaphore(0);
    Semaphore esperandoDespegar = new Semaphore(0);

    public void permisoAterrizar(String n) throws InterruptedException {

        if ((cantAterrizajes.availablePermits() >= 10 && esperandoDespegar.availablePermits() > 0)
                ) {
            turnoDespegar.release();

        } else {
            if (capacidad.tryAcquire()) {
                cantAterrizajes.release();
                System.out.println(n + ":PUEDE ATERRIZAR. Aterrizando..."+ cantAterrizajes.availablePermits());
                Thread.sleep(2000);
                System.out.println(n + ":FIN ATERRIZAJE.");
                capacidad.release();
            } else {
                System.out.println(n + ": aterrizaje PISTA EN USO. DEBE ESPERAR");
                esperandoAterrizar.release();
                
                capacidad.acquire();
                cantAterrizajes.release();

                System.out.println(n + ":SE DESOCUPO LA PISTA. PUEDE ATERRIZAR. Aterrizando..."+ cantAterrizajes.availablePermits());
                Thread.sleep(2000);
                System.out.println(n + ":FIN ATERRIZAJE.");
                esperandoAterrizar.acquire();
                capacidad.release();
            }

        }

    }

    public void permisoDespegar(String n) throws InterruptedException {
        if (turnoDespegar.tryAcquire()|| esperandoAterrizar.availablePermits()==0)
                 {
            capacidad.acquire();
            System.out.println(n + ":PUEDE DESPEGAR. Despegando..."+esperandoAterrizar.availablePermits());
            Thread.sleep(2000);
            System.out.println(n + ":FIN DESPEGUE.");
            int a = cantAterrizajes.availablePermits();
            cantAterrizajes.acquire(a);
            capacidad.release();
                    

        } else {
            esperandoDespegar.release();
            System.out.println(n + ":despegue NO ESTA HABILITADO. DEBE ESPERAR"+esperandoDespegar.availablePermits());
            //error
            if(esperandoAterrizar.availablePermits()==0){
                turnoDespegar.release();
            }
            turnoDespegar.acquire();
            capacidad.acquire();

            System.out.println(n + ":SE DESOCUPO LA PISTA Y ESTA HABILITADO A DESPEGAR. Despegando...");
            Thread.sleep(2000);
            System.out.println(n + ":FIN DESPEGUE.");
            esperandoDespegar.acquire();
            
            int a = cantAterrizajes.availablePermits();
            cantAterrizajes.acquire(a);
capacidad.release();
        }

    }
}
