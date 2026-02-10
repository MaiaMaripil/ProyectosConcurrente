/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc1;

/**
 *
 * @author famil
 */
public class prueba {

    public static void main(String[] args) throws InterruptedException {

        final Dato unContador = new Dato();

        RunnableCdor unRunCdor = new RunnableCdor(unContador);

        Thread h1 = new Thread(unRunCdor);
        Thread h2 = new Thread(unRunCdor);
        h1.start();
        h2.start();
        h1.join();
        h2.join();
        System.out.println("en main-" + unContador.obtenerValor());

    }
}

class RunnableCdor implements Runnable {

    Dato unContador;

    public RunnableCdor(Dato elCdor) {

        unContador = elCdor;
        

    

    }

    public void run() {

        for (int x = 0; x < 10000; ++x) {
            unContador.contar();
        }

    }

}

class Dato {

    private int valor;

    void contar() {
        valor = ++valor;
    }

    int obtenerValor() {
        return valor;
    }

}
