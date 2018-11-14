/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xulambis;

import static xulambis.FileReader.getInstance;

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
            LexicalAnalyzer.getInstance().analyzeChar();
            System.out.println("Tabela de Símbolos da Xulambis\n");
            SymbolsTable.getInstance().printTokens();
        }
        catch(Exception e)
        {
            System.out.println("Fatal Error: " + e.getMessage());
        }
    }
    
}