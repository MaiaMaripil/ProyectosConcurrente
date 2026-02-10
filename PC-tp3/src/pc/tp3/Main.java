/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp3;
import pc.tp3.VerificarCuenta;
import pc.tp3.Energia;
import pc.tp3.VerificarJaula;
/**
 *
 * @author famil
 */
public class Main {

    public static void main(String[] args) {
       /* VerificarCuenta vc = new VerificarCuenta();
        Thread luis = new Thread(vc, "Luis");
        Thread manuel = new Thread(vc, "Manuel");
        luis.start();
        manuel.start();*/
      /* VerificarEnergia energia= new VerificarEnergia();
       Thread criaturaOscura= new Thread(energia, "Criatura Oscura");
       Thread sanador= new Thread(energia,"Sanador");
       
       criaturaOscura.start();
       sanador.start();
       */
      VerificarJaula jaula= new VerificarJaula();
      Thread h1= new Thread(jaula,"h1");
      Thread h2= new Thread(jaula,"h2");
      Thread h3= new Thread(jaula,"h3");
      
      h1.start();
      h2.start();
      h3.start();
      
       
       
    }
    
   
}
