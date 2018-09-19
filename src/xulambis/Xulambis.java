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
            FileReader xulambis = new FileReader("teste.xul");
            xulambis.readFile();
            SymbolsTable xulambisSymbols = xulambis.getTokens();
            System.out.println("Tokens da Xulambis\n");
            xulambisSymbols.showTokens();
            System.out.println("\n\nTabela de Simbolos (Identificador e Tipo)");
            xulambisSymbols.showSymbols();
            
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
}