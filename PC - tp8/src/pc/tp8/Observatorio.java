/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp8;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author maiamaripil
 */
public class Observatorio {

    public static void main(String[] args) {

        GestionObservatorio o = new GestionObservatorio();
        Thread inv1 = new Thread(new Investigador("INVESTIGADOR 1" , o));
        Thread inv2 = new Thread(new Investigador("INVESTIGADOR 2" , o));
        Thread p1 = new Thread(new Personal("PERSONAL 1" , o));
        Thread p2 = new Thread(new Personal("PERSONAL 2" , o));
        Thread p3 = new Thread(new Personal("PERSONAL 3" , o));
        
        inv1.start();
        inv2.start();
            p1.start();
            p2.start();
            p3.start();
            

        for (int i = 1; i <= 10; i++) {
                       boolean sR = (Math.random() > 0.5);
            Thread v = new Thread(new Visitante("VISITANTE " + i, o, sR));
          
            v.start();

            try {
                Thread.sleep((int) (Math.random() * 5000));  // Clientes llegan en intervalos aleatorios.
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
}

class Visitante implements Runnable {

    private String nombre;
    private GestionObservatorio recurso;
    private boolean sillaRuedas;

    public Visitante(String n, GestionObservatorio gO, boolean sR) {
        this.nombre = n;
        this.recurso = gO;
        this.sillaRuedas = sR;
    }

    public void run() {
        try {
            recurso.entrarVisitante(nombre, sillaRuedas);
            Thread.sleep(2000);
            recurso.salirVisitante(nombre, sillaRuedas);
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Investigador implements Runnable {

    private String nombre;
    private GestionObservatorio recurso;

    public Investigador(String n, GestionObservatorio gO) {
        this.nombre = n;
        this.recurso = gO;
    }

    public void run() {
        try {
            while (true) {
                recurso.entrarInvestigador(nombre);
                Thread.sleep(3000);
                recurso.salirInvestigador(nombre);
            }
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class Personal implements Runnable {

    private String nombre;
    private GestionObservatorio recurso;

    public Personal(String n, GestionObservatorio gO) {
        this.nombre = n;
        this.recurso = gO;
    }

    public void run() {
        try {
            while (true) {
                 Thread.sleep((int) (Math.random() * 3000));
                recurso.entrarPersonal(nombre);
                Thread.sleep(3000);
                recurso.salirPersonal(nombre);
            }
        } catch (InterruptedException e) {
            System.out.println("error");
        }
    }
}

class GestionObservatorio {

    private int capacidad = 50;
    private int visitantes = 0;
    private int personal = 0;
    private boolean investigador = false;
    private int vEsperando=0;
    private int pEsperando=0;
    private int iEsperando=0;

    private Lock lock = new ReentrantLock();
    private Condition puedeEntrarV = lock.newCondition();
    private Condition puedeEntrarI = lock.newCondition();
    private Condition puedeEntrarP = lock.newCondition();

    public void entrarVisitante(String n, boolean sR) throws InterruptedException {
        lock.lock();
        try {
            while (personal > 0 || investigador || visitantes == capacidad) {
                System.out.println(n + " No puede entrar. Debe esperar.");
                puedeEntrarV.await();
            }
            if (sR) {
                capacidad = 30;
                System.out.println(n+ " Se redujo la capacidad");
            }
            visitantes++;
            System.out.println(n + " Entro al observatorio");

        } finally {
            lock.unlock();
        }
    }

    public void entrarPersonal(String n) throws InterruptedException {
        lock.lock();
        try {
            while (investigador || visitantes > 0 || personal >= capacidad) {
                System.out.println(n + " No puede entrar.");
                puedeEntrarP.await();
            }
            personal++;
            System.out.println(n + " Entro al obervatorio");
        } finally {
            lock.unlock();
        }
    }

    public void entrarInvestigador(String n) throws InterruptedException {
        lock.lock();
        try {
            while (visitantes > 0 || personal > 0 || investigador) {
                System.out.println(n + "Debe esperar");
                puedeEntrarI.await();
            }
            investigador = true;
            System.out.println(n + " Entro al observatorio");
        } finally {
            lock.unlock();
        }
    }

    public void salirVisitante(String n, boolean sR) {
        lock.lock();
        try {
            visitantes--;
            System.out.println(n + " Salio del observatorio");
            if (sR) {
                capacidad = 50;
            }
            puedeEntrarI.signal();
            puedeEntrarP.signal();
        } finally {
            lock.unlock();
        }
    }

    public void salirInvestigador(String n) {
        lock.lock();
        try {
            investigador = false;
            System.out.println(n + " Salio del observatorio");
            puedeEntrarP.signal();
            puedeEntrarV.signal();
        } finally {
            lock.unlock();
        }
    }

    public void salirPersonal(String n) {
        lock.lock();
        try {
            personal--;
            System.out.println(n + " Salio del observatorio");
            puedeEntrarV.signal();
            puedeEntrarI.signal();
        } finally {
            lock.unlock();
        }
    }
}
