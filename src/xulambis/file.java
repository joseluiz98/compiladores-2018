/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xulambis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aluno
 */
public class file {
    private String filePath;
    private List<String> fileContent = new ArrayList();
    private TokenMap tokens = new TokenMap();
    
    public file(String filePath)
    {
        this.filePath = filePath;
    }
    
    public void readFile() throws Exception
    {
        File file = new File(filePath);

        BufferedReader buffer = new BufferedReader(new FileReader(file));

        String line;
        while ((line = buffer.readLine()) != null)
        {
//            line = line.replaceAll(" ","");
            tokens.putToken(line.split(";"));
        }
    }
    
    public void showFile()
    {
        for(String line : fileContent)
            System.out.println(line);
    }

    public TokenMap getTokens() {
        return tokens;
    }
    
}