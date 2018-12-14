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
import javax.script.ScriptException;

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

    public static boolean analyzeCode() throws ScriptException, Exception
    {
        if(startAnalysis())
        {
            return true;
        }
        return false;
    }
    
    private static boolean startAnalysis() throws IOException, Exception
    {        
        if(currentByte < FileReader.getInstance().getLastByte())
        {
            char current = FileReader.getInstance().getNextChar(currentByte);
            
            if(Character.isSpace(current) || current == '\n')
            {
                currentByte++;
                return startAnalysis();
            }
            System.out.println("Analisando '" + current + "'");
            Token token = new Token();
            ArrayList<Character> functionReturn;
            Character punct;
            
            if(isWhitespace(current))
            {
                // do nothing just call me again
                currentByte++;
                return startAnalysis();
            }
            else if(ifToken(current))
            {
                System.out.println("if");
                token.setTokenName("reserved-word");
                token.setLexem("if");
                TokenList.insertToken(token);
                currentByte++;
                return startAnalysis();
            }
            else if(whileToken(current))
            {
                System.out.println("while");
                token.setTokenName("reserved-word");
                token.setLexem("while");
                TokenList.insertToken(token);
                currentByte++;
                return startAnalysis();
            }
            else if(intToken(current))
            {
                System.out.println("int");
                token.setTokenName("primitive-type");
                token.setLexem("int");
                TokenList.insertToken(token);
                currentByte++;
                return startAnalysis();
            }
            else if(floatToken(current))
            {
                System.out.println("float");
                token.setTokenName("primitive-type");
                token.setLexem("float");
                TokenList.insertToken(token);
                currentByte++;
                return startAnalysis();
            }
            else if(boolToken(current))
            {
                System.out.println("bool");
                token.setTokenName("primitive-type");
                token.setLexem("bool");
                TokenList.insertToken(token);
                currentByte++;
                return startAnalysis();
            }
            else if(trueToken(current))
            {
                System.out.println("true");
                token.setTokenName("reserved-word");
                token.setLexem("true");
                TokenList.insertToken(token);
                currentByte++;
                return startAnalysis();
            }
            else if(falseToken(current))
            {
                System.out.println("false");
                token.setTokenName("reserved-word");
                token.setLexem("false");
                TokenList.insertToken(token);
                currentByte++;
                return startAnalysis();
            }
            else if(breakToken(current))
            {
                System.out.println("break");
                token.setTokenName("reserved-word");
                token.setLexem("break");
                TokenList.insertToken(token);
                currentByte++;
                return startAnalysis();
            }
            else if((punct = punctuationToken(current)) != null)
            {
                System.out.println("punct");
                token.setTokenName("delimiter");
                token.setLexem(punct.toString());
                TokenList.insertToken(token);
                currentByte++;
                return startAnalysis();
            }
            else if((functionReturn = comparisonToken(current)) != null)
            {
                String toString = "";

                for(Character c : functionReturn)
                {
                    toString += c;
                }
                
                token.setTokenName("comparison");
                token.setLexem(toString);
                TokenList.insertToken(token);
                currentByte++;
                return startAnalysis();
            }
            else if(assignmentToken(current))
            {
                System.out.println("assign");
                token.setTokenName("assignment");
                token.setLexem("=");
                TokenList.insertToken(token);
                currentByte++;
                return startAnalysis();
            }
            else if((functionReturn = idToken(current)) != null)
            {
                System.out.println("identifier");
                String varName = "";
                for(Character c : functionReturn)
                {
                    varName += c;
                }
                
                Token previousTokenRead = TokenList.getTokenAt(TokenList.getTokens().size()-1);
                if(previousTokenRead.getTokenName() == "primitive-type")
                {
                    token.setTokenName("identifier");
                    token.setLexem(varName);
                    TokenList.insertToken(token);
                    
                    String idType = previousTokenRead.getLexem();
                    SymbolsTable.getInstance().insertToken(varName, idType);
                    //SymbolsTable.insertToken(varName, idType);
                }
                else
                {
                    token.setTokenName("identifier");
                    token.setLexem(varName);
                    TokenList.insertToken(token);
                }
                currentByte++;
                return startAnalysis();
            }
            else if((functionReturn = numberToken(current)) != null)
            {
                System.out.println("number");
                if(!functionReturn.isEmpty())
                {
                    
                String toString = "";

                for(Character c : functionReturn)
                {
                    toString += c;
                }
                    token.setTokenName("number");
                    token.setLexem(toString);
                    TokenList.insertToken(token);
                }
                currentByte++;
                return startAnalysis();
            }
            
            return false;
        }
        return true;
    }

    private static boolean isWhitespace(char current) throws FileNotFoundException, IOException
    {
        int startByte = currentByte;
        
        if(Character.isSpace(current))
        {
            currentByte++;
            return true;
        }
        
        if(current == '/')
        {
            currentByte++;
            current = FileReader.getNextChar(currentByte);
            
            if(current == '/')
            {
                currentByte++;
                current = FileReader.getNextChar(currentByte);
                
                while(current != '\n')
                {
                    currentByte++;
                    current = FileReader.getNextChar(currentByte);
                }
                return true;
            }
        }
        currentByte = startByte;
        return false;
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
                System.out.println("Analisando '" + current + "'");
                currentByte++;
                current = FileReader.getNextChar(currentByte);
                if(current == 't')
                {
                    System.out.println("Analisando '" + current + "'");
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
                            
                            if(Character.isSpace(current) || current == '(')
                            {
                                currentByte--;
                                return true;
                            }
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
            currentByte--;
            return id;
        }
        else
        {
            if(id.isEmpty()) currentByte = startByte;
            return null;
        }
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
                            if(current == ';')
                            {
                                currentByte--   ;
                                current = FileReader.getNextChar(currentByte);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        currentByte = startByte;
        return false;
    }
    
    private static ArrayList<Character> numberToken(char current) throws IOException
    {
        int startByte = currentByte;
        ArrayList<Character> number = new ArrayList<>();
        
        if(current >= '0' && current <= '9')
        {
            while(current >= '0' && current <= '9' || current == '.')
            {
                number.add(current);
                currentByte++;
                current = FileReader.getNextChar(currentByte);
            }
            
            if(number.get(number.size()-1) == '.') throw new IOException("Number empty in second member");
            
            currentByte--;
            return number;
        }
        else
        {
            currentByte = startByte;
            return null;
        }
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
                    if(current == 'e')
                    {
                        currentByte++;
                        current = FileReader.getNextChar(currentByte);
                        if(current == ' ')
                            return true;
                    }
                }
            }
        }
        currentByte = startByte;
        return false;
    }
    
    private static boolean falseToken(char current) throws FileNotFoundException, IOException
    {
        int startByte = currentByte;
        if(current == 'f')
        {
            currentByte++;
            current = FileReader.getNextChar(currentByte);
            if(current == 'a')
            {
                currentByte++;
                current = FileReader.getNextChar(currentByte);
                if(current == 'l')
                {
                    currentByte++;
                    current = FileReader.getNextChar(currentByte);
                    if(current == 's')
                    {
                        currentByte++;
                        current = FileReader.getNextChar(currentByte);
                        if(current == 'e')
                            return true;
                    }
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
            return true;
        }
        return false;
    }
    private static Character punctuationToken(char current) throws FileNotFoundException, IOException
    {
        final String REGEX="[^.%*$#@?^&'|/\\\\~\\[\\]{}+-;\"-()]*";
        
        int startByte = currentByte;
        if (REGEX.indexOf(current) >= 0) return current;
        
        currentByte = startByte;
        return null;
    }
    private static ArrayList<Character> comparisonToken(char current) throws FileNotFoundException, IOException
    {
        ArrayList<Character> comparisonToken = new ArrayList<>();
        int startByte = currentByte;
        
        char nextChar = '\f';
        if(currentByte+1 < FileReader.getLastByte()) nextChar = FileReader.getNextChar(currentByte+1);
        
        if(current == '<' || current == '>' || current == '!' || (current == '=' && nextChar == '='))
        {
            comparisonToken.add(current);
            currentByte++;
            current = FileReader.getNextChar(currentByte);
            if(current == '=')
            {
                comparisonToken.add(current);
            }
            return comparisonToken;
        }
        currentByte = startByte;
        return null;
    }
}