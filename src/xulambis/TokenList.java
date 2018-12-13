/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xulambis;

import java.util.ArrayList;

/**
 *
 * @author jose
 */
public class TokenList
{
    private static TokenList tokenList;
    private static ArrayList<Token> tokens = new ArrayList<>();
    
    public static TokenList getInstance()
    {
        if(tokenList == null) tokenList = new TokenList();
        return tokenList;
    }
    
    public static void insertToken(Token token)
    {
        tokens.add(token);
    }

    public static ArrayList<Token> getTokens() {
        return tokens;
    }
    
    public static void printTokens()
    {
        for(Token token : tokens)
        {
            System.out.println ("<" + token.getTokenName() + "," + token.getLexem() + ">");
        }
        System.out.println("\r\n");
    }
    
    public static Token getTokenAt(int idx)
    {
        return tokens.get(idx);
    }
}