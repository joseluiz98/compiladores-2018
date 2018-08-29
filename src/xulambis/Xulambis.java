/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xulambis;

/**
 *
 * @author aluno
 */
public class Xulambis {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try
        {
            file teste = new file("teste.xul");
            teste.readFile();
            teste.showFile();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
}