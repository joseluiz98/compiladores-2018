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
        
        if(current != '\f')
        {            
            if(ifToken(current))
            {
                SymbolsTable.insertToken("reserved-word", "if");
            }
            if(breakToken(current))
            {
                SymbolsTable.insertToken("reserved-word", "break");
            }
            
            List<Character> ifNumberFunctionReturns = numberToken(current);
            if(!ifNumberFunctionReturns.isEmpty())
            {
                String number = "";
                
                for(Character c : ifNumberFunctionReturns)
                {
                    number += c;
                }
                
                SymbolsTable.insertToken("number", number);
            }
            
            return analyzeChar();
        }
        return true;
    }

    private static boolean ifToken(char current) throws FileNotFoundException, IOException
    {
        if(current == 'i')
        {
            current = FileReader.getNextChar();
            System.out.println(current);
            if(current == 'f')
            {
                current = FileReader.getNextChar();
                if(current == ' ' || current == '(') return true;
            }
        }
        return false;
    }
    
    private static boolean breakToken(char current) throws FileNotFoundException, IOException
    {
        if(current == 'b')
        {
            current = FileReader.getNextChar();
            System.out.println(current);
            if(current == 'r')
            {
                current = FileReader.getNextChar();
                System.out.println(current);
                
                if(current == 'e')
                {
                    current = FileReader.getNextChar();
                    System.out.println(current);
                    if(current == 'a')
                    {
                        current = FileReader.getNextChar();
                        System.out.println(current);
                        if(current == 'k')
                        {
                            current = FileReader.getNextChar();
                            if(current == ';') return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private static List<Character> numberToken(char current) throws IOException
    {
        List<Character> number = new ArrayList<>();
        
        if(current >= '0' && current <= '9')
        {
            while(current >= '0' && current <= '9' || current == '.')
            {
                System.out.println(current);
                number.add(current);
                current = FileReader.getNextChar();
            }
            
            if(number.get(number.size()-1) == '.') throw new IOException("Number empty in second member");
        }
            return number;
    }
}