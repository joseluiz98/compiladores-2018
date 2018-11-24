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
public class SemanticalAnalyzer {
    private static SemanticalAnalyzer semanticalAnalyzer;
    private static int currentToken = 1;
    
    public static SemanticalAnalyzer getInstance()
    {
        if(semanticalAnalyzer == null) semanticalAnalyzer = new SemanticalAnalyzer();
        return semanticalAnalyzer;
    }
    
    public static boolean analyzeCode() throws ScriptException, Exception
    {
        int numberOfTokens = TokenList.getTokens().size();
        
        Token current = TokenList.getTokenAt(currentToken);
        System.out.println();
        System.out.print("Analisando: " + current.getLexem() + " ");
        
        if(currentToken < numberOfTokens)
        {
            if(isVarDeclaration(current))
            {
                System.out.println("var declaration");
                ///SymbolsTable
                analyzeCode();
            }
            
            else if(isAssignment(current))
            {
                System.out.println("assignment");
                ///SymbolsTable
                analyzeCode();
            }
            else if(whileOrIf(current))
            {
                System.out.println("while or if");
                analyzeCode();
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
                String id = current.getLexem();
                currentToken++;
                current = TokenList.getTokenAt(currentToken);
                System.out.print(current.getLexem() + " ");
                
                if(";".equals(current.getLexem()))
                {
                    currentToken++;
                    SymbolsTable.getTokens().get(id).setDeclarada(true);
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean isAssignment(Token current) throws ScriptException
    {
        String mathExpression = "";
        int startToken = currentToken;
        Token nextToken = TokenList.getTokenAt(currentToken+1);
        String id = current.getLexem();
        
        
        if(current.getTokenName() == "identifier")
        {
            if(SymbolsTable.getTokens().get(id) == null)
            {
                System.out.println("var not declared ");
                return false;
            }
                    
            if(SymbolsTable.getTokens().get(id).isDeclarada())
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
        }
        return false;
    }
    
    private static boolean comparisonToken(Token current)
    {
        int startToken = currentToken;
        Token operandToken = TokenList.getTokenAt(currentToken+1);
        
        if("identifier".equals(current.getTokenName()) && ")".equals(operandToken.getLexem()))
        {
            if(SymbolsTable.getTokens().get(current.getLexem()).getTipo() == "bool")
            {
                return true;
            }
        }
        else if(current.getTokenName() == "identifier" && operandToken.getTokenName()== "comparison")
        {
            String firstOperandType = SymbolsTable.getTokens().get(current.getLexem()).getTipo();
            currentToken+=2;
            current = TokenList.getTokenAt(currentToken);
            String secondOperandType = SymbolsTable.getTokens().get(current.getLexem()).getTipo();
            
            if(firstOperandType == secondOperandType)
            {
                if(firstOperandType == "bool" && (operandToken.getLexem() == "==" || operandToken.getLexem() == "!="))
                    return true;
                else
                {
                    System.out.println("cai aqui");
                    return true;
                }
            }
        }
//        if(current.getTokenName() == "identifier" || current.getTokenName()== "number")
//        {
//            currentToken++;
//            current = TokenList.getTokenAt(currentToken);
//            System.out.print(current.getLexem());
//            if(current.getTokenName() == "comparison")
//            {
//                currentToken++;
//                current = TokenList.getTokenAt(currentToken);
//                System.out.print(current.getLexem());
//                
//                if(current.getTokenName() == "identifier" || current.getTokenName() == "number")
//                {
//                    return true;
//                }
//            }
//            else if(")".equals(current.getLexem()))
//            {
//                currentToken--;
//                return true;
//            }
//        }
        return false;
    }
    
    private static boolean whileOrIf(Token current) throws ScriptException
    {
    //        FALTA TRATAR WHILE(TRUE) OU WHILE(FALSE)
        int startToken = currentToken;
        if(current.getLexem() == "while" || current.getLexem() == "if")
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

                    if(")".equals(current.getLexem()))
                     {
                         currentToken++;
                         current = TokenList.getTokenAt(currentToken);
                         System.out.print(current.getLexem() + " ");

                         if("{".equals(current.getLexem()))
                         {
                             currentToken++;
                             return true;
                         }
                     }
                 }
            }   
        }
        return false;
    }
}
