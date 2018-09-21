/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xulambis;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aluno
 */
public class LexicalAnalyzer {
    private static LexicalAnalyzer lexicalAnalyzer;
    private List<Character> aux = new List<Character>();

    static void analyzeChar(char current)
    {
        if(ifToken(current))
        {
           return true; 
        }
    }
    
    private List<Character> code = new ArrayList<Character>();

    private LexicalAnalyzer() {
    }
    
    public static LexicalAnalyzer getInstance()
    {
        if(lexicalAnalyzer == null) lexicalAnalyzer = new LexicalAnalyzer();
        return lexicalAnalyzer;
    }
    
    static public Boolean ifToken(char i)
    {
        return true;
//        if(i == 'i') i = getProximo();
//        if(i == 'f') return true;
//        return false;
    }
}