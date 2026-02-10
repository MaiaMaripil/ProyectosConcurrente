/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc1;

/**
 *
 * @author famil
 */
public class RunnableEjemplo implements Runnable {
    private String nombre;
    
    public RunnableEjemplo(String n){
        this.nombre=n;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i + " " + nombre);
        }
        System.out.println("Termina thread " + nombre);
    }

    public static void main(String[] args) {
        RunnableEjemplo mj= new RunnableEjemplo("Maria Jose");
        RunnableEjemplo jm=new RunnableEjemplo("Jose Maria");
        
        Thread t1=new Thread(mj);
        Thread t2=new Thread(jm);
        
        t1.start();
        t2.start();
        System.out.println("Termina thread main");
    }
}
