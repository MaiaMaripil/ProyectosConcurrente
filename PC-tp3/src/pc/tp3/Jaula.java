/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc.tp3;

/**
 *
 * @author famil
 */
public class Jaula {
    private boolean plato=false;
    private boolean rueda=false;
    private boolean hamaca=false;
    
    public Jaula(){
    }
    
    public boolean getPlato(){
        return this.plato;
    }
    
     public boolean getRueda(){
        return this.rueda;
    }
     
      public boolean getHamaca(){
        return this.hamaca;
    }
      
      public void setPlato(boolean a){
          this.plato=a;
      }
      
      public void setRueda(boolean a){
          this.rueda=a;
      }
      public void setHamaca(boolean a){
          this.hamaca=a;
      }
}
