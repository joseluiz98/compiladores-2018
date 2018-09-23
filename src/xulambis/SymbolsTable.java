/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xulambis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jose_
 */
public class SymbolsTable {
    private static SymbolsTable table;
    private static HashMap<String, List<String>> tokens = new HashMap<>();

    public static SymbolsTable getInstance()
    {
        if(table == null) table = new SymbolsTable();
        return table;
    }
    
    public HashMap<String, List<String>> getTokens() {
        return tokens;
    }
    
    public void putToken(String[] line) throws Exception
    {
//        str = str.replaceAll(";"," ; ");
//        String[] line = str.split("\\s+");
        List<String> ar = new ArrayList<>();
        String lastTokenTested = "";
        
        for(String lineSemiColonSplitted : line)
        {
            lastTokenTested = "";
            String[] lexemes = lineSemiColonSplitted.split("\\s+");
            for(String lexem : lexemes)
            {
                if(lexem.isEmpty())
                {
                    continue;
                }
                else
                {
                    if(isNumber(lexem)) insertToken("number",lexem);
                    else if(isPunct(lexem)) insertToken("punct",lexem);
                    else if(lastTokenTested != null && isPrimitiveType(lastTokenTested)) insertToken("id",lexem);
                    else if(isReservedWord(lexem)) insertToken("reserved-word",lexem);
                    if(lexem != "=") lastTokenTested = lexem;
                }
            }
        }
    }
    
    public static void insertToken(String key, String lexem)
    {
        List<String> list = tokens.get(key);
        if(list == null)
        {
            list = new ArrayList();
            list.add(lexem);
        }
        else
        {
            if(!list.contains(lexem)) list.add(lexem);
        }
        tokens.put(key, list);
    }
    
    public static void printTokens()
    {
        for (Map.Entry<String, List<String>> entry : tokens.entrySet())
        {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            
            for(String element : value)
                System.out.println ("<" + key + "," + element + ">");
        }
    }
    private static boolean isNumber(String str)
    {
        try
        {
          double d = Double.parseDouble(str);  
        }  
        catch(NumberFormatException nfe)  
        {  
          return false;  
        } 
        return true;
    }
    
    private static boolean isPunct(String str)
    {
        return !Character.isLetter(str.charAt(0)) && !Character.isDigit(str.charAt(0));
    }
    
    private static boolean isPrimitiveType(String str)
    {
        List<String> primitiveTypes = new ArrayList();
        primitiveTypes = Arrays.asList("int","float","double","const","bool");
        
        if (primitiveTypes.contains(str.toLowerCase())) return true;
        else return false;
    }
    
    private static boolean isReservedWord(String str)
    {
        List<String> reservedWords = new ArrayList();
        reservedWords = Arrays.asList("while","break","if","true","int","float","double","const","bool");
        
        if (reservedWords.contains(str.toLowerCase())) return true;
        else return false;        
    }
}
