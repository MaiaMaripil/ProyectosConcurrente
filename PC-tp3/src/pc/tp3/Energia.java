/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp3;

/**
 *
 * @author famil
 */
public class Energia {

    private int valor = 10;

    public Energia() {
    }

    public int getValor() {
        return valor;
    }

    public void quitarEnergia(int unValor) {
        this.valor = valor - unValor;
        
    }
    public void sumarEnergia(int unValor) {
        this.valor = valor + unValor;
        
    }
}

