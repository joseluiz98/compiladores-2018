/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xulambis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aluno
 */
public class FileReader
{

    private String filePath;
    private File file = new File(filePath);
    private List<String> fileContent = new ArrayList();
    private SymbolsTable lexems = new SymbolsTable();

    public FileReader(String filePath) {
        this.filePath = filePath;
    }

    public List<Character> readFileByEachChar() throws FileNotFoundException
    {
        if (!file.exists())
        {
            System.out.println(filePath + " does not exist.");
            throw new FileNotFoundException(filePath + " does not exist.");
        
        }
        if (!(file.isFile() && file.canRead()))
        {
            System.out.println(file.getName() + " cannot be read from.");
//          return;
        }
        try
        {
            FileInputStream fis = new FileInputStream(file);
            char current;
            while (fis.available() > 0)
            {
                current = (char) fis.read();
                LexicalAnalyzer.getInstance().analyzeChar(current);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void readFileByWords() throws Exception {
        File file = new File(filePath);

        BufferedReader buffer = new BufferedReader(new java.io.FileReader(file));

        String line;
        while ((line = buffer.readLine()) != null) {
//            line = line.replaceAll(" ","");
            lexems.putToken(line.split(";"));
        }
    }

    public void showFile() {
        for (String line : fileContent) {
            System.out.println(line);
        }
    }

    public SymbolsTable getTokens() {
        return lexems;
    }

}