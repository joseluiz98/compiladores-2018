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
    
    public static void insertToken(String key, String lexem) throws Exception
    {
        System.out.println(key);
        List<String> list = tokens.get(key);
        if(list == null)
        {
            list = new ArrayList();
            list.add(lexem);
        }
        else
        {
            throw new Exception("Redeclaração da variável " + key);
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
}
