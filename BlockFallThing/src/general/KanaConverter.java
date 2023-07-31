package general;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class KanaConverter {

    ArrayList<String> kanaList = new ArrayList();
    ArrayList<String> romajiList = new ArrayList();
    
    KanaConverter() throws IOException{
        BufferedReader br = new BufferedReader(
            new InputStreamReader(new FileInputStream("D:\\Downloads\\kana_to_romaji.csv"), "UTF-8"));
        String line = br.readLine();
        while(line != null) {
            
            String[] splitString = line.split(",", 2);
            kanaList.add(splitString[0]);
            romajiList.add(splitString[1]);
            line = br.readLine();
        }
    }
    
    String convertKanaToRomaji(String kana){
       
        
        ArrayList<String> romaji = new ArrayList();
        
        for (int i = 0; i < kana.length(); i++) {
            if(!kanaList.contains(String.valueOf(kana.charAt(i)))){
                //System.out.println("ERROR: KANA NOT FOUND - " + String.valueOf(kana.charAt(i)));
                if(kana.charAt(i) == '-' || kana.charAt(i) == 'ー'){
                    String targetChar = romaji.get(romaji.size()-1);
                    char duplicateChar = targetChar.charAt(targetChar.length() - 1);
                    romaji.add(String.valueOf(duplicateChar));
                }
            } else {
                romaji.add(romajiList.get(kanaList.indexOf(String.valueOf(kana.charAt(i)))));
            }
        }
        
        String finalRomaji = "";
        
        for (int i = 0; i < romaji.size(); i++) {
            finalRomaji = finalRomaji + romaji.get(i);
        }
        
        //finalRomaji = finalRomaji.replace("ixy", "");
        finalRomaji = finalRomaji.replace("ux", "");
        finalRomaji = finalRomaji.replace("ix", "");
        finalRomaji = finalRomaji.replace("ex", "");
        
        return finalRomaji;
    }
    
}
