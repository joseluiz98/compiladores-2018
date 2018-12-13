/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xulambis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.C;
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
    private static int curlyBrackets = 0;
    
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
            else if(isDelimiter(current))
            {
                System.out.println("delimiter");
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
    
    private static boolean comparisonToken(Token current) throws ScriptException, Exception
    {
        ArrayList<Token> comparison = new ArrayList();
        
        int parenthesisCounter = 1;
        String mathExpression = "(";
        int startToken = currentToken;
        
        String aux = current.getLexem();
        do
        {
            if("(".equals(current.getLexem())) parenthesisCounter++;
            else if(")".equals(current.getLexem())) parenthesisCounter--;

            mathExpression += aux;
            comparison.add(current);
            
            currentToken++;
            current = TokenList.getTokenAt(currentToken);
            aux = current.getLexem();
            System.out.println(aux + " ");
        } while(parenthesisCounter != 0);
        
        // Remove o último parênteses, que é colocado desnecessariamente na lista
        comparison.remove(comparison.size()-1);
        
        mathExpression = mathExpression.replaceAll("[a-zA-Z]", "1");
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        
        System.out.println("exp " + mathExpression);
        int nOfMembersInComparison = comparison.size();
        
        // Se só existe um membro na comparação: while(true) ou while(false)
        if(nOfMembersInComparison == 1)
        {
            Token firstMember = comparison.get(0);
            // Se esse membro é um id
            if("identifier".equals(firstMember.getTokenName()))
            {
                // esse id foi declarado
                if(SymbolsTable.getTokens().get(firstMember.getLexem()).isDeclarada())
                {
                    System.out.println(SymbolsTable.getTokens().get(firstMember.getLexem()).getTipo());
                    // E é do tipo boolean, é uma comparação válida
                    if("bool".equals(SymbolsTable.getTokens().get(firstMember.getLexem()).getTipo())) return true;
                    else throw new Exception("While or If with one member expect bool type. Got a " + SymbolsTable.getTokens().get(firstMember.getLexem()).getTipo() + ".");
                }
            }
            // Porém, se é somente um membro, mas não é um id, teste se é uma palavra reservada
            else if("reserved-word".equals(firstMember.getTokenName()))
            {
                // Se é uma palavra, teste se ela é 'true' ou 'false
                if("true".equals(firstMember.getLexem())) return true;
                else if("false".equals(firstMember.getLexem())) return true;
            }
        }
        // Se é uma comparação com membro, operador e membro, faça
        else if(nOfMembersInComparison == 3)
        {
            Token firstMember = comparison.get(0);
            Token operator = comparison.get(1);
            Token secondMember = comparison.get(2);
        
            // Verifica matematicamente se a comparação é válida
            // Para isso substitui o que é id por um número qualquer
            // Por exemplo, 'x<y' vira '1<1', que matematicamente é válido
            
            if(engine.eval(mathExpression) != null)
            {
                // Se for matematicamente válido, vamos tratar cada possibilidade
                
                // Se o primeiro membro for um id
                if("identifier".equals(firstMember.getTokenName()))
                {
                    // E o segundo também for um id
                    if("identifier".equals(secondMember.getTokenName()))
                    {
                        // Compare seus tipos
                        if(SymbolsTable.getTokens().get(firstMember.getLexem()).getTipo() == SymbolsTable.getTokens().get(secondMember.getLexem()).getTipo())
                        {
                            // Caso válido, vamos verificar o comparador
                            if("comparison".equals(operator.getTokenName()))
                            {
                                // Qualquer operador é válido para comparar números
                                // Porém, para bool só é permitido '==' e '!='
                                if("bool".equals(SymbolsTable.getTokens().get(firstMember.getLexem()).getTipo()))
                                {
                                    // Se os membros são bool, então trate o operador
                                    if("==".equals(operator.getLexem()) || "!=".equals(operator.getLexem()))
                                        return true;
                                }
                            }
                        }
                    }
                    // Caso o segundo membro não seja um identificador, ele tem de ser um número,
                    // pois não existe String na xulambis
                    else if("number".equals(secondMember.getTokenName()))
                    {
                        // Estamos comparando um id a um número
                        // Portanto o id tem de ser de tipo numérico
                        if("float".equals(SymbolsTable.getTokens().get(secondMember.getLexem())) || "int".equals(SymbolsTable.getTokens().get(secondMember.getLexem())))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        
        
        currentToken = startToken;
        return false;
    }
    
    private static boolean whileOrIf(Token current) throws ScriptException, Exception
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
                     System.out.print(current.getLexem() + " ");

                    if(")".equals(current.getLexem()))
                    {
                        currentToken++;
                        current = TokenList.getTokenAt(currentToken);
                        System.out.print(current.getLexem() + " ");
                        return true;
                    }
                }
            }   
        }
        return false;
    }

    private static boolean isDelimiter(Token current) {
        ArrayList<String> delimiters = new ArrayList<>();
        delimiters.add("{");
        
        if(delimiters.contains(current.getLexem()))
        {
            currentToken++;
            return true;
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
}