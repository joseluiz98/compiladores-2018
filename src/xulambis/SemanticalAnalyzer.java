/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xulambis;

import java.util.ArrayList;
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
        int numberOfTokens = TokenList.getTokens().size()-1;
        
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
            else if(isWhileOrIf(current))
            {
                System.out.println("while or if");
                analyzeCode();
            }
            else if(isDelimiter(current))
            {
                System.out.println("delimiter");
                analyzeCode();
            }
            else if("reserved-word".equals(current.getTokenName()))
            {
                System.out.println("reserved-word");
                currentToken++;
                analyzeCode();
            }
        }
        return true;
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
    
    private static boolean isAssignment(Token current) throws ScriptException, Exception
    {
        String mathExpression = "";
        int startToken = currentToken;
        Token nextToken = TokenList.getTokenAt(currentToken+1);
        Token varToStoreResults = current;
        ArrayList<String> memberTypes = new ArrayList();
        
        if("identifier".equals(current.getTokenName()))
            memberTypes.add(SymbolsTable.getToken(varToStoreResults.getLexem()).getTipo());

        if(current.getTokenName() == "identifier")
        {                    
            if(!SymbolsTable.isDeclared(varToStoreResults.getLexem()))
                throw new Exception("var " + varToStoreResults.getLexem() + " not declared ");
            
            currentToken++;
            current = TokenList.getTokenAt(currentToken);
            System.out.print(current.getLexem() + " ");
            if(current.getTokenName() == "assignment")
            {
                currentToken++;
                current = TokenList.getTokenAt(currentToken);
                ArrayList<Token> members = new ArrayList();

                // Pega os tipos de todos os ids da atribuição
                while(!";".equals(current.getLexem()))
                {
                    System.out.print(current.getLexem() + " ");
                    // Se for um id, faça
                    if("identifier".equals(current.getTokenName()))
                    {
                        if(SymbolsTable.isDeclared(current.getLexem()))
                        {
                            memberTypes.add(SymbolsTable.getTokens().get(current.getLexem()).getTipo());
//                                else throw new Exception("Invalid operand types on assignment! Expect int or float but got " + SymbolsTable.getTokens().get(current.getLexem()).getTipo());
                        }
                        else throw new Exception("Var " + current.getLexem() + " not declared.");
                    }
                    members.add(current);
                    currentToken++;
                    current = TokenList.getTokenAt(currentToken);
                }

                // Verifica se todos os tipos são iguais
                boolean allEqual = memberTypes.stream().distinct().limit(2).count() <= 1;
                if(!allEqual)
                {
                    memberTypes.add(1, "=");
                    throw new Exception("Incompatible types on assignment vars. Types given: " + memberTypes.toString());
                }

                // Se a variável a armazenar o resultado é bool, faça
                if("bool".equals(SymbolsTable.getToken(varToStoreResults.getLexem()).getTipo()))
                {
                    current = TokenList.getTokenAt(currentToken-1);
                    if("true".equals(current.getLexem()) || "false".equals(current.getLexem()))
                        return true;
                    else if(SymbolsTable.isDeclared(current.getLexem()) && "bool".equals(SymbolsTable.getToken(current.getLexem()).getTipo()))
                        return true;
                    else throw new Exception("Invalid value to store in bool var, expect true or false but got " + current.getLexem());
                }
                // Senão, a variável é numérica, portanto
                // verifique se x é de um tipo numérico
                else
                {
                    for(Token token : members)
                    {
                        if("reserved-word".equals(token.getTokenName()))
                            throw new Exception("Invalid attribution.");
                    }
                    if("float".equals(SymbolsTable.getTokens().get(varToStoreResults.getLexem()).getTipo()) || "int".equals(SymbolsTable.getTokens().get(varToStoreResults.getLexem()).getTipo()))
                        return true;
                    else throw new Exception("Invalid store var type, expect float or int but got a " + SymbolsTable.getTokens().get(varToStoreResults.getLexem()).getTipo());
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
            
            if(parenthesisCounter != 0) System.out.print(aux + " ");
        } while(parenthesisCounter != 0);
            
        currentToken--;
        current = TokenList.getTokenAt(currentToken);
        
        // Remove o último parênteses, que é colocado desnecessariamente na lista
        comparison.remove(comparison.size()-1);
        
        mathExpression = mathExpression.replaceAll("[a-zA-Z]", "1");
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        
        int nOfMembersInComparison = comparison.size();
        
        // Se só existe um membro na comparação: while(true) ou while(false)
        if(nOfMembersInComparison == 1)
        {
            Token firstMember = comparison.get(0);
            // Se esse membro é um id
            if("identifier".equals(firstMember.getTokenName()))
            {
                // esse id foi declarado
                if(!SymbolsTable.isDeclared(firstMember.getLexem()))
                    throw new Exception("Var " + firstMember.getLexem() + " was never declared.");
                // E é do tipo boolean, é uma comparação válida
                if("bool".equals(SymbolsTable.getTokens().get(firstMember.getLexem()).getTipo())) return true;
                else throw new Exception("While or If with one member expect bool type. Got a " + SymbolsTable.getTokens().get(firstMember.getLexem()).getTipo() + ".");
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
                    // e estiver sido declarado
                    if(!SymbolsTable.isDeclared(firstMember.getLexem()))
                        throw new Exception("Var " + firstMember.getLexem() + " was never declared.");
                    
                    if("identifier".equals(secondMember.getTokenName()))
                    {
                        // E o segundo também for um id também já declarado
                        if(!SymbolsTable.isDeclared(secondMember.getLexem()))
                            throw new Exception("Var " + secondMember.getLexem() + " was never declared.");

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
                                    throw new Exception("Invalid comparison between two bool vars.");
                                }
                                return true;
                            }
                        }
                        else throw new Exception("Foribdden comparison between " + SymbolsTable.getTokens().get(firstMember.getLexem()).getTipo() + " and " 
                                + SymbolsTable.getTokens().get(secondMember.getLexem()).getTipo());
                    }
                    // Se o segundo não for um id, e o primeiro for uma var float ou int
                    else if("float".equals(SymbolsTable.getToken(firstMember.getLexem()).getTipo()) || "int".equals(SymbolsTable.getToken(firstMember.getLexem()).getTipo()))
                    {
                        // então o segundo tem de ser um número
                        if("number".equals(secondMember.getTokenName())) return true;
                        else throw new Exception("Invalid comparison type between numerical var and " + secondMember.getLexem());
                    }
                    // Se o segundo não for um id, e o primeiro for uma var bool
                    else if("bool".equals(SymbolsTable.getToken(firstMember.getLexem()).getTipo()))
                    {
                        // o segundo tem que ser true, false, 1 ou 0
                        ArrayList<String> validOptions = new ArrayList();
                        validOptions.add("1");
                        validOptions.add("0");
                        validOptions.add("true");
                        validOptions.add("false");
                        
                        if(validOptions.contains(secondMember.getLexem()))
                        {
                            if("==".equals(operator.getLexem()) || "!=".equals(operator.getLexem())) return true;
                            else throw new Exception("Invalid operand " + operator.getLexem() + " for bool type.");
                        }
                        else throw new Exception("Invalid comparison between bool and " + secondMember.getLexem());
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
    
    private static boolean isWhileOrIf(Token current) throws ScriptException, Exception
    {
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
                     return true;
                }
            }   
        }
        return false;
    }

    private static boolean isDelimiter(Token current) {
        ArrayList<String> delimiters = new ArrayList<>();
        delimiters.add("{");
        delimiters.add("}");
        delimiters.add(";");
        
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
    
    private static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
}