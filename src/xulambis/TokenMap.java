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
public class TokenMap {
    private HashMap<String, List<String>> tokens = new HashMap<>();
    
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
            for(String token : lexemes)
            {
                if(token.isEmpty())
                {
                    continue;
                }
                else
                {
                    if(isNumber(token)) insertHash("number",token);
                    else if(isPunct(token)) insertHash("punct",token);
                    else if(lastTokenTested != null && isPrimitiveType(lastTokenTested)) insertHash("identificator",token);
                    else if(isReservedWord(token)) insertHash("reserved-word",token);
                    if(token != "=") lastTokenTested = token;
                }
            }
        }
    }
    
    private void insertHash(String key, String token)
    {
        List<String> list = tokens.get(key);
        if(list == null)
        {
            list = new ArrayList();
            list.add(token);
        }
        else
        {
            if(!list.contains(token)) list.add(token);
        }
        tokens.put(key, list);
    }
    
    public void showTokens()
    {
        for (Map.Entry<String, List<String>> entry : tokens.entrySet()) {
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
