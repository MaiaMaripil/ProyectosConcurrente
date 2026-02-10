/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc1;

import java.util.Scanner;

/**
 *
 * @author famil
 */
public class PruebaExcep {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int edad = 0;
       /* System.out.println("Ingrese la edad: ");
        edad = sc.nextInt();

        try {
            boolean mayor = mayorDeEdad(edad);
            if (mayor) {
                System.out.println("Es mayor de edad");
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }*/
       coleccion();

    }

    public static boolean mayorDeEdad(int edad) {

        if (edad < 18) {
            throw new RuntimeException("La persona es menor de edad");
        }
        return true;
    }
    
     public static void coleccion() {
          Scanner sc = new Scanner(System.in);
         int[] array= new int[5];
         for(int i=0;i<array.length; i++){
         System.out.println((i+1)+")ingrese un numero");
         array[i]=sc.nextInt();
     }
         try{
             for(int j=0;j<=7;j++){
                 System.out.println(array[j]);
             }
         }catch (RuntimeException e){
             System.err.println("ERROR");
         }

        
    }
}
