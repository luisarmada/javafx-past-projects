package general;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;


public final class BlockGenerator extends AnimationTimer{

    ArrayList<FallingBlock> blockList = new ArrayList();
    ArrayList<Vocab> vocabList = new ArrayList();
    ArrayList<Vocab> spawnedVocab = new ArrayList();
    
    Group rootGroup;
    Rectangle groundCol;
    int windowWidth;
    Random rand = new Random();
    
    long lastUpdate;
    
    TextField inputField;
    KanaConverter kanaConverter;
    
    MediaPlayer mediaPlayer;
    
    Text score;
    
    int points = 0;
    
    BlockGenerator(int winX, Group parentGrp, Rectangle groundCollision, TextField tf, KanaConverter kCon, Text score, String listFile) throws IOException{
        rootGroup = parentGrp;
        groundCol = groundCollision;
        kanaConverter = kCon;
        vocabList = readFile(listFile);
        windowWidth = winX;
        inputField = tf;
        this.score = score;
        String musicFile = "D:\\Downloads\\correct.wav";     // For example

        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        
        
        Collections.shuffle(vocabList);
        System.out.println(vocabList.size());
    }
    
    double fallSpeed = 1.25;
    long fallRate = 300_000_000_0L;
    
    @Override
    public void handle(long now) {
        
        // Block gravity
        for (int i = 0; i < blockList.size(); i++) {
            FallingBlock b = blockList.get(i);
            
            b.collision.setY(b.collision.getY() + fallSpeed);
            b.text.setLayoutY(b.collision.getY() + 50);
            if(b.collision.getBoundsInParent().intersects(
                groundCol.getBoundsInParent())){
                b.isDisabled = true;
            }
        }
        
        //fallRate -= 0.0000000001;
        //fallSpeed += 0.0000001;
        
        // Block creation
        if(now - lastUpdate >= fallRate){
            lastUpdate = now;
            createBlock();
        }
        
        // Answer Checking
        for (int i = 0; i < spawnedVocab.size(); i++) {
            if(blockList.get(i).isDisabled) continue;
            if(inputField.getText().equalsIgnoreCase(spawnedVocab.get(i).romajiReading)){
                blockList.get(i).collision.setFill(Color.web("#77ba99"));
                blockList.get(i).isDisabled = true;
                inputField.clear();
                
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
                
                points++;
                score.setText(String.valueOf(points));
            }
        }
        
        groundCol.toFront();
        score.toFront();
        inputField.toFront();
        
    }
    
    public void createBlock(){
        Vocab v = vocabList.get(vocabList.size() - 1);
        vocabList.remove(v);
        spawnedVocab.add(v);
        
        System.out.println(v.romajiReading);
        
        FallingBlock fb = new FallingBlock(rand.nextInt(windowWidth), windowWidth, v.word);
        blockList.add(fb);
        fb.text.setLayoutX(fb.collision.getX() + 30);
        rootGroup.getChildren().addAll(fb.collision, fb.text);
        
    }
    
    ArrayList<Vocab> readFile(String filePath) throws FileNotFoundException, IOException{
        ArrayList<Vocab> returnList = new ArrayList();
        
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
        String line = br.readLine();
        while(line != null) {
            
            String[] splitString = line.split(",", 3);
            returnList.add(new Vocab(splitString[0], splitString[1], kanaConverter.convertKanaToRomaji(splitString[1]), splitString[2]));
            line = br.readLine();
        }
        
        return returnList;
    }
    
    class Vocab{
        String word, reading, romajiReading, definition;
        
        Vocab(String word, String reading, String romajiReading, String definition){
            this.word = word;
            this.reading = reading;
            this.romajiReading = romajiReading;
            this.definition = definition;
        }
    }
    
}
