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
            
            LexicalAnalyzer.getInstance().analyzeChar();
            
            System.out.println("Tokens do código-fonte\n");
            TokenList.getInstance().printTokens();
            
            System.out.println("Tabela de Símbolos do código-fonte\n");
            SymbolsTable.getInstance().printTokens();
            
            System.out.println("Analisador sintático código-fonte\n");
            SintaticalAnalyzer.getInstance().analyzeCode();
        }
        catch(Exception e)
        {
            System.out.println("Fatal Error: " + e.getMessage());
        }
    }
    
}