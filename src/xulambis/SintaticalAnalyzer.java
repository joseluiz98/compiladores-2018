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
    private static String lastTokenFound;
    
    public static SintaticalAnalyzer getInstance()
    {
        if(sintaticalAnalyzer == null) sintaticalAnalyzer = new SintaticalAnalyzer();
        return sintaticalAnalyzer;
    }
    
    public static boolean analyzeCode() throws ScriptException, Exception
    {
        if(startAnalysis())
        {
            if(bodyAnalysis()) return true;
        }
        return false;
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

    private static boolean bodyAnalysis() throws ScriptException, Exception
    {
        int numberOfTokens = TokenList.getTokens().size();
        
        if(currentToken < numberOfTokens)
        {
            Token current = TokenList.getTokenAt(currentToken);
            System.out.println();
            System.out.print("Analisando: " + current.getLexem() + " ");
        
            if(curlyBracketToken(current))
            {
                System.out.println("chave");
                lastTokenFound = "chave";
                bodyAnalysis();
            }
            else if(semicolonToken(current))
            {
                System.out.println("ponto-e-virgula");
                lastTokenFound = "ponto-e-virgula";
                bodyAnalysis();
            }
            else if(breakToken(current))
            {
                System.out.println("break");
                lastTokenFound = "break";
                bodyAnalysis();
            }
            else if(ifToken(current))
            {
                System.out.println("if");
                lastTokenFound = "if";
                bodyAnalysis();
            }
            else if(whileToken(current))
            {
                System.out.println("while");
                lastTokenFound = "while";
                bodyAnalysis();
            }
            else if(assignToken(current))
            {
                System.out.println("assign");
                lastTokenFound = "assign";
                bodyAnalysis();
            }
            else if(isVarDeclaration(current))
            {
                System.out.println("var declaration");
                lastTokenFound = "var declaration";
                bodyAnalysis();
            }
            else
            {
                throw new Exception("Erro de compilação");
            }
            return bodyAnalysis();
        }
        else
        {
            if(curlyBrackets == 0) return true;
            else
            {
                return false;
            }
        }
        
    }
    
    private static boolean comparisonToken(Token current) throws ScriptException
    {
        int parenthesisCounter = 0;
        String mathExpression = "";
        int startToken = currentToken;
        
        String aux = current.getLexem();
        if("(".equals(current.getLexem()))
        {
            do
            {
                if("(".equals(current.getLexem())) parenthesisCounter++;
                else if(")".equals(current.getLexem())) parenthesisCounter--;
                
                mathExpression += aux;
                currentToken++;
                current = TokenList.getTokenAt(currentToken);
                aux = current.getLexem();
                System.out.println(aux + " ");
            } while(parenthesisCounter != 0);
        }
        else
        {
            return false;
        }

        mathExpression = mathExpression.replaceAll("[a-zA-Z]", "1");
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        
        System.out.println("mano, funciona plmdds");
        System.out.println(mathExpression);
        
        if(engine.eval(mathExpression) != null)
        {
            return true;
        }
        
        return false;
    }
    
    
    private static boolean assignToken(Token current) throws ScriptException
    {
        String mathExpression = "";
        int startToken = currentToken;
        Token nextToken = TokenList.getTokenAt(currentToken+1);
        
        if(current.getTokenName() == "identifier")
        {
            currentToken++;
            current = TokenList.getTokenAt(currentToken);
            System.out.print(current.getLexem() + " ");
            if(current.getTokenName() == "assignment")
            {
                currentToken++;
                current = TokenList.getTokenAt(currentToken);
                System.out.print(current.getLexem() + " ");

                String aux = current.getLexem();
                while(!";".equals(aux))
                {
                    mathExpression += aux;
                    currentToken++;
                    current = TokenList.getTokenAt(currentToken);
                    aux = current.getLexem();
                }

                mathExpression = mathExpression.replaceAll("[a-zA-Z]", "1");
                ScriptEngineManager mgr = new ScriptEngineManager();
                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                
                if(engine.eval(mathExpression) != null)
                {
                    currentToken++;
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean isVarDeclaration(Token current) throws ScriptException
    {
        String mathExpression = "";
        int startToken = currentToken;
        Token nextToken = TokenList.getTokenAt(currentToken+1);
        
        if(current.getTokenName()== "primitive-type")
        {
            currentToken++;
            current = TokenList.getTokenAt(currentToken);
            System.out.print(current.getLexem() + " ");
            
            if(current.getTokenName()== "identifier")
            {
                currentToken++;
                current = TokenList.getTokenAt(currentToken);
                System.out.print(current.getLexem() + " ");
                
                if(";".equals(current.getLexem()))
                {
                    currentToken++;
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean ifToken(Token current) throws ScriptException
    {
//        FALTA TRATAR IF(TRUE) OU IF(FALSE)
        int startToken = currentToken;
        if("if".equals(current.getLexem()))
        {
            currentToken++;
            current = TokenList.getTokenAt(currentToken);
            System.out.print(current.getLexem() + " ");
            
            if("(".equals(current.getLexem()))
            {
                currentToken++;
                current = TokenList.getTokenAt(currentToken);
                System.out.print(current.getLexem() + " ");
                
               if(comparisonToken(current))
               {
                    currentToken++;
                    return true;
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
            System.out.print(current.getLexem() + " ");

            if("(".equals(current.getLexem()))
            {
                currentToken++;
                current = TokenList.getTokenAt(currentToken);
                System.out.print(current.getLexem() + " ");

                if(comparisonToken(current))
                {
                    currentToken++;
                    current = TokenList.getTokenAt(currentToken);
                    System.out.print(current.getLexem() + " ");
                    
                    if("{".equals(current.getLexem()))
                    {
                        curlyBrackets++;
                        currentToken++;
                        return true;
                    }
                 }
            }   
        }
        return false;
    }

    private static boolean breakToken(Token current)
    {
        int startToken = currentToken;
        if(current.getLexem() == "break" && (lastTokenFound == "if" || lastTokenFound == "while"))
        {
            currentToken++;
            current = TokenList.getTokenAt(currentToken);
            
            if(";".equals(current.getLexem()))
            {
                currentToken++;
                return true;
            }
        }
        return false;
    }
    private static boolean curlyBracketToken(Token current)
    {
        int startToken = currentToken;
        if("{".equals(current.getLexem()))
        {
            curlyBrackets++;
            currentToken++;
            return true;
        }
        else if("}".equals(current.getLexem()))
        {
            curlyBrackets--;
            currentToken++;
            return true;
        }
        currentToken = startToken;
        return false;
    }
    private static boolean semicolonToken(Token current)
    {
        int startToken = currentToken;
        if(";".equals(current.getLexem()))
        {
            currentToken++;
            return true;
        }
        currentToken = startToken;
        return false;
    }
}