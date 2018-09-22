/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xulambis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aluno
 */
public class LexicalAnalyzer {
    private static LexicalAnalyzer lexicalAnalyzer;
    private List<Character> aux = new ArrayList<>();
    
    public static LexicalAnalyzer getInstance()
    {
        if(lexicalAnalyzer == null) lexicalAnalyzer = new LexicalAnalyzer();
        return lexicalAnalyzer;
    }

    static boolean analyzeChar() throws IOException
    {
        char current = FileReader.getInstance().getNextChar();
        System.out.println(current);
        
        if(current != '\n')
        {
            if(ifToken(current))
            {
                SymbolsTable.getInstance().insertToken("reserved-word", "if");
            }
            return analyzeChar();
        }
        return true;
    }

    private static boolean ifToken(char current) throws FileNotFoundException, IOException
    {
        if(current == 'i')
        {
            current = FileReader.getInstance().getNextChar();
            System.out.println(current);
            if(current == 'f')
                return true;
        }
        return false;
    }
}