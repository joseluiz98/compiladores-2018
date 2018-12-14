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
            System.setProperty("file.encoding", "UTF-8");
            FileReader.setFilePath("teste13.xul");
            
            if(!LexicalAnalyzer.getInstance().analyzeCode())
            {
                System.out.println("Erro na análise léxica");
                System.exit(0); 
            }
            
            System.out.println("Tokens do código-fonte\n");
            TokenList.getInstance().printTokens();
            
            System.out.println("Tabela de Símbolos do código-fonte\n");
            SymbolsTable.getInstance().printTokens();
            
            
            
            System.out.println("Analisador sintático código-fonte\n");
            
            if(SintaticalAnalyzer.getInstance().analyzeCode())
            {
                System.out.println("\n\nAnalisando sematicamente");
                if(SemanticalAnalyzer.getInstance().analyzeCode())
                    System.out.println("Código correto");
            }
            else
            {
                System.out.println("Código errado");
            }
        }
        catch(Exception e)
        {
            System.out.println("Fatal Error: " + e.getMessage());
        }
    }
    
}