/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp3;

import pc.tp3.Jaula;

/**
 *
 * @author maiamaripil
 */
public class VerificarJaula implements Runnable {

    private Jaula jaula = new Jaula();
    

    private void usarPlato() throws InterruptedException {
        synchronized (jaula.plato) {
            if (jaula.getPlato().equals(false)) {
                System.out.println(Thread.currentThread().getName() + " esta usando  el plato de comida");
                jaula.setPlato(true);
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " termino de comer");
                jaula.setPlato(false);
            }
        }
    }

    private void usarRueda() throws InterruptedException {
        synchronized (jaula.rueda) {
            if (jaula.getRueda().equals(false)) {
                System.out.println(Thread.currentThread().getName() + " esta usando  la rueda");
                jaula.setRueda(true);
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " termino de usar la rueda");
                jaula.setRueda(false);
            }
        }
    }

    private synchronized void usarHamaca() throws InterruptedException {
        synchronized (jaula.hamaca) {
            if (jaula.getHamaca().equals(false)) {
                System.out.println(Thread.currentThread().getName() + " esta usando  la hamaca");
                jaula.setHamaca(true);
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " termino de usar la hamaca");
                jaula.setHamaca(false);
            }
        }
    }

    public void run() {

        try {
            this.usarPlato();
            this.usarRueda();
            this.usarHamaca();
        } catch (InterruptedException ex) {
            System.out.println("No se pudo realizar ninguna actividadd");
        }

    }

    public class Jaula {

        public Object plato = false;
        private Object rueda = false;
        private Object hamaca = false;

        public Jaula() {
        }

        public Object getPlato() {
            return this.plato;
        }

        public Object getRueda() {
            return this.rueda;
        }

        public Object getHamaca() {
            return this.hamaca;
        }

        public void setPlato(boolean a) {
            this.plato = a;
        }

        public void setRueda(boolean a) {
            this.rueda = a;
        }

        public void setHamaca(boolean a) {
            this.hamaca = a;
        }
    }

    public static void main(String[] args) {

        VerificarJaula jaula = new VerificarJaula();
        Thread h1 = new Thread(jaula, "h1");
        Thread h2 = new Thread(jaula, "h2");
        Thread h3 = new Thread(jaula, "h3");

        h1.start();
        h2.start();
        h3.start();

    }

}
