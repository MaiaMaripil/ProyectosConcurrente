/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp3;

/**
 *
 * @author famil
 */
public class ParqueTematico {

    class Area {

        private int espacioDisp;
        private String nombre;

        public Area(String n, int d) {
            this.nombre = n;
            this.espacioDisp = d;
        }

        public String getNombre() {
            return this.nombre;
        }

        public int espacioDisp() {
            return this.espacioDisp;
        }

        public void setEspacioDisp() {
            espacioDisp = espacioDisp--;
        }

        public synchronized boolean reservar(String n) {
            boolean exito = false;
            if (espacioDisp > 0) {
                System.out.println(n + " realizo con exito su reserva en el area " + nombre);
                exito = true;
            } else {
                System.out.println(n + " no pudo realizar su reserva por no haber disponibilidad en el area " + nombre);

            }
            return exito;
        }
    }

    class Visitante implements Runnable {

        private Area area;
        private String nombre;

        public Visitante(Area a, String n) {
            this.area = a;
            this.nombre = n;
        }
    
        public void run() {
            try {
                area.reservar(nombre);
            } catch (InterruptedException e) {
                System.out.println("ERROR");
            }

        }
    }

    public void main(String[] args) {

        Area[] areas = new Area[3];
        areas[0] = new Area("a1", 5);
        areas[1] = new Area("a2", 3);
        areas[2] = new Area("a3", 2);

        Visitante v1 = new Visitante(areas[0], "v1");
        Visitante v2 = new Visitante(areas[2], "v2");
        Visitante v3 = new Visitante(areas[2], "v3");

        Thread t1 = new Thread(v1, "v1");
        Thread t2 = new Thread(v2, "v2");
        Thread t3 = new Thread(v3, "v3");

        t1.start();
        t2.start();
        t3.start();

    }

}
