package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {
    
    static String filePath = "src\\files\\";
    
    public String readFile(String fileName){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(filePath + fileName)));
        } catch (FileNotFoundException ex) { Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex); }
        
        String wholeText = "";
        String line;
        
        try {
            while((line = br.readLine()) != null){
                wholeText = wholeText + line + "\n";
            } } catch (IOException ex) { Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex); }
        
        try {
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return wholeText;
    }
    
    public void writeFile(String fileName, String writeText){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath + fileName), true /* append */));
            bw.write(writeText);
            bw.newLine();
            bw.close();
        } catch (IOException ex) { Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex); }
    }
}
