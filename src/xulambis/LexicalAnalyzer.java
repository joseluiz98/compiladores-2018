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
            ArrayList<Character> token;
            Character punct;
            
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
            else if(floatToken(current))
            {
                SymbolsTable.insertToken("primitive-type", "float");
            }
            else if(boolToken(current))
            {
                SymbolsTable.insertToken("primitive-type", "bool");
            }
            else if(trueToken(current))
            {
                SymbolsTable.insertToken("reserved-word", "true");
            }
            else if(breakToken(current))
            {
                SymbolsTable.insertToken("reserved-word", "break");
            }
            else if((punct = punctuationToken(current)) != 0)
            {
                SymbolsTable.insertToken("punct", punct.toString());
            }
            else if((token = comparisonToken(current)) != null)
            {
                String idString = "";

                for(Character c : token)
                {
                    idString += c;
                }

                SymbolsTable.insertToken("comparison", idString);
            }
            else if(assignmentToken(current))
            {
                SymbolsTable.insertToken("assignment", "=");
            }
            else if((token = idToken(current)) != null)
            {
                String idString = "";

                for(Character c : token)
                {
                    idString += c;
                }

                SymbolsTable.insertToken("id", idString);
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
                if(current == ' ' || current == '(')
                {
                    currentByte--;
                    current = FileReader.getNextChar(currentByte);
                    return true;
                }
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
    
    private static ArrayList<Character> idToken(char current) throws IOException
    {
        ArrayList<Character> id = new ArrayList<>();
        int startByte = currentByte;
        current = Character.toLowerCase(current);
        
        if(Character.isLetter(current))
        {
            while(Character.isLetter(current))
            {
                id.add(current);
                currentByte++;
                current = FileReader.getNextChar(currentByte);
            }
        }
        else
        {
            return null;
        }
        if(id.isEmpty()) currentByte = startByte;
        return id;
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
    
    private static boolean floatToken(char current) throws FileNotFoundException, IOException
    {
        int startByte = currentByte;
        if(current == 'f')
        {
            currentByte++;
            current = FileReader.getNextChar(currentByte);
            if(current == 'l')
            {
                currentByte++;
                current = FileReader.getNextChar(currentByte);
                if(current == 'o')
                {
                    currentByte++;
                    current = FileReader.getNextChar(currentByte);
                    if(current == 'a')
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
            }
        }
        currentByte = startByte;
        return false;
    }
    
    private static boolean boolToken(char current) throws FileNotFoundException, IOException
    {
        int startByte = currentByte;
        if(current == 'b')
        {
            currentByte++;
            current = FileReader.getNextChar(currentByte);
            if(current == 'o')
            {
                currentByte++;
                current = FileReader.getNextChar(currentByte);
                if(current == 'o')
                {
                    currentByte++;
                    current = FileReader.getNextChar(currentByte);
                    if(current == 'l')
                    {
                        currentByte++;
                        current = FileReader.getNextChar(currentByte);
                        if(current == ' ') return true;
                    }
                }
            }
        }
        currentByte = startByte;
        return false;
    }
    
    private static boolean trueToken(char current) throws FileNotFoundException, IOException
    {
        int startByte = currentByte;
        if(current == 't')
        {
            currentByte++;
            current = FileReader.getNextChar(currentByte);
            if(current == 'r')
            {
                currentByte++;
                current = FileReader.getNextChar(currentByte);
                if(current == 'u')
                {
                    currentByte++;
                    current = FileReader.getNextChar(currentByte);
                    if(current == 'e') return true;
                }
            }
        }
        currentByte = startByte;
        return false;
    }
    private static boolean assignmentToken(char current) throws FileNotFoundException, IOException
    {
        int startByte = currentByte;
        if(current == '=')
        {
            currentByte++;
            current = FileReader.getNextChar(currentByte);
            return true;
        }
        return false;
    }
    private static Character punctuationToken(char current) throws FileNotFoundException, IOException
    {
        final String REGEX="[^.,%*$#@?^!&'|/\\\\~\\[\\]{}+-;\"-()]*";
        
        int startByte = currentByte;
        if (REGEX.indexOf(current) >= 0) return current;
        
        currentByte = startByte;
        return 0;
    }
    private static ArrayList<Character> comparisonToken(char current) throws FileNotFoundException, IOException
    {
        ArrayList<Character> comparisonToken = new ArrayList<>();
        int startByte = currentByte;
        
        char nextChar = '\f';
        if(currentByte+1 < FileReader.getLastByte()) nextChar = FileReader.getNextChar(currentByte+1);
        
        if(current == '<' || current == '>' || (current == '=' && nextChar == '='))
        {
            comparisonToken.add(current);
            currentByte++;
            current = FileReader.getNextChar(currentByte);
            if(current == '=')
            {
                comparisonToken.add(current);                
                currentByte++;
                current = FileReader.getNextChar(currentByte);
            }
            return comparisonToken;
        }
        currentByte = startByte;
        return null;
    }
}