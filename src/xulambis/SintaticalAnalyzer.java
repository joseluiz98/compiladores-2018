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
    private static int curlyBrackets = 0;
    
    public static SintaticalAnalyzer getInstance()
    {
        if(sintaticalAnalyzer == null) sintaticalAnalyzer = new SintaticalAnalyzer();
        return sintaticalAnalyzer;
    }
    
    public static void analyzeCode() throws ScriptException
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
            System.out.println(TokenList.getTokenAt(0).getLexem());
            if("{".equals(TokenList.getTokenAt(0).getLexem()))
            {
                curlyBrackets++;
                currentToken++;
                return true;
            }
        }
        return false;
    }

    private static boolean bodyAnalysis() throws ScriptException
    {
        Token current = TokenList.getTokenAt(currentToken);
        System.out.println("Analisando: " + current.getLexem());
        
        int numberOfTokens = TokenList.getTokens().size();
        
        if(currentToken < numberOfTokens)
        {
            if(curlyBracketClosingToken(current))
            {
                System.out.println("}");
                curlyBrackets--;
                bodyAnalysis();
            }
            else if(ifToken(current))
            {
                System.out.println("if");
                bodyAnalysis();
            }
            else if(whileToken(current))
            {
                System.out.println("while");
                bodyAnalysis();
            }
            else if(assignToken(current))
            {
                System.out.println("assign");
                bodyAnalysis();
            }
            currentToken++;
            return bodyAnalysis();
        }
        return true;
    }
    
    private static boolean comparisonToken(Token current)
    {
        int startToken = currentToken;
        if(current.getTokenName() == "identifier" || current.getTokenName()== "number")
        {
            currentToken++;
            current = TokenList.getTokenAt(currentToken);
            if(current.getTokenName()== "comparison")
            {
                currentToken++;
                current = TokenList.getTokenAt(currentToken);
                if(current.getTokenName() == "identifier" || current.getTokenName()== "number")
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean assignToken(Token current) throws ScriptException
    {
        String mathExpression = "";
        int startToken = currentToken;
        if(current.getTokenName() == "primitive-type")
        {
            currentToken++;
            current = TokenList.getTokenAt(currentToken);
            if(current.getTokenName()== "identifier")
            {
                currentToken++;
                current = TokenList.getTokenAt(currentToken);
                if(current.getTokenName()== "assignment")
                {
                    String aux = current.getTokenName();
                    while(aux != ";")
                    {
                        mathExpression += aux;
                        currentToken++;
                        current = TokenList.getTokenAt(currentToken);
                        aux = current.getTokenName();
                    }
                    
                    mathExpression = mathExpression.replaceAll("[^a-zA-Z]", "1");
                    ScriptEngineManager mgr = new ScriptEngineManager();
                    ScriptEngine engine = mgr.getEngineByName("JavaScript");
                    
                    engine.eval(mathExpression);
                    return true;
                }
                else if(current.getLexem() == ";")
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean ifToken(Token current)
    {
//        FALTA TRATAR IF(TRUE) OU IF(FALSE)
        int startToken = currentToken;
        if(current.getLexem() == "if")
        {
            currentToken++;
            current = TokenList.getTokenAt(currentToken);
            if(current.getLexem() == "(")
            {
                currentToken++;
                current = TokenList.getTokenAt(currentToken);
                if(comparisonToken(current))
               {
                   currentToken++;
                    current = TokenList.getTokenAt(currentToken);
                   if(current.getLexem() == ")")
                    {
                        return true;
                    }
                }
            }   
        }
        currentToken = startToken;
        return false;
    }
    
    private static boolean whileToken(Token current) throws ScriptException
    {
//        FALTA TRATAR WHILE(TRUE) OU WHILE(FALSE)
        int startToken = currentToken;
        if("while".equals(current.getLexem()))
        {
            currentToken++;
            current = TokenList.getTokenAt(currentToken);
            if("(".equals(current.getLexem()))
            {
                currentToken++;
                current = TokenList.getTokenAt(currentToken);
                if(comparisonToken(current))
               {
                   currentToken++;
                    current = TokenList.getTokenAt(currentToken);
                   if(")".equals(current.getLexem()))
                    {
                        currentToken++;
                        current = TokenList.getTokenAt(currentToken);
                        if("{".equals(current.getLexem()))
                        {
                            curlyBrackets++;
                            currentToken++;
                            return true;
                        }
                    }
                }
            }   
        }
        return false;
    }

    private static boolean curlyBracketClosingToken(Token current) {
        int startToken = currentToken;
        if("}".equals(current.getLexem()))
        {
            currentToken++;
            return true;
        }
        return false;
    }
}