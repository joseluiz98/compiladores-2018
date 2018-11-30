/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xulambis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aluno
 */
public class FileReader
{
    private static FileInputStream reader;
    private static FileReader fileReader;
    private static RandomAccessFile raf;
    private static String filePath = "teste8.xul";
    private File file = new File(filePath);
    private List<String> fileContent = new ArrayList();
    private SymbolsTable lexems = new SymbolsTable();

    public static FileReader getInstance() throws FileNotFoundException
    {
        if(fileReader == null) fileReader = new FileReader(filePath);
        return fileReader;
    }
    
    public FileReader(String filePath) throws FileNotFoundException
    {
        this.filePath = filePath;
        if (!file.exists())
        {
            throw new FileNotFoundException(filePath + " does not exist.");
        
        }
        if (!(file.isFile() && file.canRead()))
        {
//          return;
        }
        
        reader = new FileInputStream(file);
        raf = new RandomAccessFile(file, "r");
    }
    
    public static char getNextChar(int startByte) throws IOException
    {
        try
        {
            if(startByte >= 0 && startByte < raf.length()-1)
            {
                raf.seek(startByte);
                byte[] bytes = new byte[1];
                raf.read(bytes);
                String currentChar = new String(bytes);

               return currentChar.charAt(0);
            }
            raf.close();
            return '\f';
        }
        catch(IOException e)
        {
            throw new IOException("File reader closed! " + e.getMessage());
        }
    }
    
    public static long getLastByte() throws IOException
    {
        return raf.length()-1;
    }
}