/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xulambis;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jose_
 */
public class SymbolsTable {
    private static SymbolsTable table;
    private static HashMap<String, Identificador> tokens = new HashMap<>();

    public static SymbolsTable getInstance()
    {
        if(table == null) table = new SymbolsTable();
        return table;
    }
    
    public boolean insertToken(String key, String tipo) throws Exception
    {
        System.out.println(key);
        Identificador id = tokens.get(key);
        if(id == null)
        {
            tokens.put(key, new Identificador(tipo, true));
            //Nao tinha na hash
            return true;
        }
        //id já declarado.
        return false;
        /*List<String> list = tokens.get(key);
        if(list == null)
        {
            list = new ArrayList();
            list.add(lexem);
        }
        else
        {
            throw new Exception("Redeclaração da variável " + key);
        }*/
        
    }
    
    public static void printTokens()
    {
        for (Map.Entry<String, Identificador> entry : tokens.entrySet())
        {
            String key = entry.getKey();
            Identificador id = entry.getValue();
            
            System.out.println ("<" + key + "," + id.getTipo() +"," +
                    ((id.isDeclarada()? "Sim" : "Não" ))+ ">");
        }
    }

    public static HashMap<String, Identificador> getTokens() {
        return tokens;
    }
    
    //Classe Interna
    public class Identificador{
        private String tipo;
        private boolean declarada = false;

        public Identificador(String tipo, boolean declarada) {
            this.tipo = tipo;
            this.declarada = declarada;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public boolean isDeclarada() {
            return declarada;
        }

        public void setDeclarada(boolean declarada) {
            this.declarada = declarada;
        }
    }
}
