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
    private static int currentByte = 0;
    
    public static LexicalAnalyzer getInstance()
    {
        if(lexicalAnalyzer == null) lexicalAnalyzer = new LexicalAnalyzer();
        return lexicalAnalyzer;
    }

    static boolean analyzeChar() throws IOException
    {
        char current = FileReader.getInstance().getNextChar(currentByte);
        
        if(current != '\f')
        {
            
            if(ifToken(current))
            {
                SymbolsTable.insertToken("reserved-word", "if");
            }
            else if(whileToken(current))
            {
                SymbolsTable.insertToken("reserved-word", "while");
            }
            else if(intToken(current))
            {
                SymbolsTable.insertToken("primitive-type", "int");
            }
            else if(breakToken(current))
            {
                SymbolsTable.insertToken("reserved-word", "break");
            }            
            else
            {
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
            }
            
            currentByte++;
            return analyzeChar();
        }
        return true;
    }

    private static boolean ifToken(char current) throws FileNotFoundException, IOException
    {
        int startByte = currentByte;
        if(current == 'i')
        {
            currentByte++;
            current = FileReader.getNextChar(currentByte);
            
            if(current == 'f')
            {
                currentByte++;
                current = FileReader.getNextChar(currentByte);
                if(current == ' ' || current == '(') return true;
            }
        }
        currentByte = startByte;
        return false;
    }
    
    private static boolean intToken(char current) throws FileNotFoundException, IOException
    {
        int firstByte = currentByte;
        if(current == 'i')
        {
            currentByte++;
            current = FileReader.getNextChar(currentByte);
            if(current == 'n')
            {
                currentByte++;
                current = FileReader.getNextChar(currentByte);
                if(current == 't')
                {
                    currentByte++;
                    current = FileReader.getNextChar(currentByte);
                    if(current == ' ') return true;
                }
            }
        }
        currentByte = firstByte;
        return false;
    }
    
    private static boolean whileToken(char current) throws FileNotFoundException, IOException
    {
        int startByte = currentByte;
        if(current == 'w')
        {
            currentByte++;
            current = FileReader.getNextChar(currentByte);
            if(current == 'h')
            {
                currentByte++;
                current = FileReader.getNextChar(currentByte);
                if(current == 'i')
                {
                    currentByte++;
                    current = FileReader.getNextChar(currentByte);
                    if(current == 'l')
                    {
                        currentByte++;
                        current = FileReader.getNextChar(currentByte);
                        if(current == 'e')
                        {
                            currentByte++;
                            current = FileReader.getNextChar(currentByte);
                            if(current == ' ' || current == '(') return true;
                        }
                    }
                }
            }
        }
        currentByte = startByte;
        return false;
    }
    
    private static boolean breakToken(char current) throws FileNotFoundException, IOException
    {
        int startByte = currentByte;
        if(current == 'b')
        {
            currentByte++;
            current = FileReader.getNextChar(currentByte);
            if(current == 'r')
            {
                currentByte++;
                current = FileReader.getNextChar(currentByte);
                
                if(current == 'e')
                {
                    currentByte++;
                    current = FileReader.getNextChar(currentByte);
                    if(current == 'a')
                    {
                        currentByte++;
                        current = FileReader.getNextChar(currentByte);
                        if(current == 'k')
                        {
                            currentByte++;
                            current = FileReader.getNextChar(currentByte);
                            if(current == ';') return true;
                        }
                    }
                }
            }
        }
        currentByte = startByte;
        return false;
    }
    
    private static List<Character> numberToken(char current) throws IOException
    {
        int startByte = currentByte;
        List<Character> number = new ArrayList<>();
        
        if(current >= '0' && current <= '9')
        {
            while(current >= '0' && current <= '9' || current == '.')
            {
                number.add(current);
                currentByte++;
                current = FileReader.getNextChar(currentByte);
            }
            
            if(number.get(number.size()-1) == '.') throw new IOException("Number empty in second member");
        }
        else
        {
            currentByte = startByte;
        }
        return number;
    }
}