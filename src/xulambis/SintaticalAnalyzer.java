/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xulambis;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author aluno
 */
public class SintaticalAnalyzer
{
    private static SintaticalAnalyzer sintaticalAnalyzer;
    private static int currentToken = 0;
    
    public static SintaticalAnalyzer getInstance()
    {
        if(sintaticalAnalyzer == null) sintaticalAnalyzer = new SintaticalAnalyzer();
        return sintaticalAnalyzer;
    }
    
    public static void analyzeCode(int currentToken)
    {
        if(startAnalysis())
        {
            bodyAnalysis();
        }
    }

    private static boolean startAnalysis()
    {
        if(currentToken == 0)
        {
            if(TokenList.getTokenAt(0).getLexem() == "{")
            {
                currentToken++;
                return true;
            }
        }
        return false;
    }

    private static void bodyAnalysis()
    {
        if(ifToken())
        {
            currentToken++;
            bodyAnalysis();
        }
        else if(whileToken())
        {
            currentToken++;
            bodyAnalysis();
        }
        else if(varDeclarationToken())
        {
            currentToken++;
            bodyAnalysis();
        }
    }
    
    private static boolean comparisonToken()
    {
        int startToken = currentToken;
        if(TokenList.getTokenAt(currentToken).getTokenName() == "identifier" || TokenList.getTokenAt(currentToken).getTokenName()== "number")
        {
            currentToken++;
            if(TokenList.getTokenAt(currentToken).getTokenName()== "comparison")
            {
                currentToken++;
                if(TokenList.getTokenAt(currentToken).getTokenName() == "identifier" || TokenList.getTokenAt(currentToken).getTokenName()== "number")
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean assignToken() throws ScriptException
    {
        String mathExpression =  
        int startToken = currentToken;
        if(TokenList.getTokenAt(currentToken).getTokenName() == "primitive-type")
        {
            if(TokenList.getTokenAt(currentToken).getTokenName()== "identifier")
            {
                if(TokenList.getTokenAt(currentToken).getTokenName()== "assignment")
                {
                    String aux = TokenList.getTokenAt(currentToken).getTokenName();
                    while(aux != ";")
                    {
                        mathExpression += aux;
                        currentToken++;
                        aux = TokenList.getTokenAt(currentToken).getTokenName();
                    }
                    
                    mathExpression = mathExpression.replaceAll("[^a-zA-Z]", "1");
                    ScriptEngineManager mgr = new ScriptEngineManager();
                    ScriptEngine engine = mgr.getEngineByName("JavaScript");
                    
                    engine.eval(mathExpression);
                    return true;
                }
                else if(TokenList.getTokenAt(currentToken).getLexem() == ";")
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean ifToken()
    {
//        FALTA TRATAR IF(TRUE) OU IF(FALSE)
        int startToken = currentToken;
        if(TokenList.getTokenAt(currentToken).getLexem() == "if")
        {
            currentToken++;
            if(TokenList.getTokenAt(currentToken).getLexem() == "(")
            {
                if(comparisonToken())
               {
                   if(TokenList.getTokenAt(currentToken).getLexem() == ")")
                    {
                        return true;
                    }
                }
            }   
        }
        return false;
    }
    
    private static boolean whileToken()
    {
//        FALTA TRATAR WHILE(TRUE) OU WHILE(FALSE)
        int startToken = currentToken;
        if(TokenList.getTokenAt(currentToken).getLexem() == "while")
        {
            currentToken++;
            if(TokenList.getTokenAt(currentToken).getLexem() == "(")
            {
                if(comparisonToken())
               {
                   if(TokenList.getTokenAt(currentToken).getLexem() == ")")
                    {
                        return true;
                    }
                }
            }   
        }
        return false;
    }
}