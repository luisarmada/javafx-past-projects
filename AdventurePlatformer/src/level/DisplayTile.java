package level;

import engine.MainClass;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class DisplayTile {
    
    static ArrayList<Integer> tilex = new ArrayList();
    static ArrayList<Integer> tiley = new ArrayList();
    
    static ArrayList<Rectangle> tileMainRect = new ArrayList();
    
    static Rectangle tileCheck;
    
    static boolean leftCheck, rightCheck, topCheck, bottomCheck;
    
    static int blockSize = LevelGenerator.BLOCK_SIZE;
    
    private static int chance(){
        return new Random().nextInt(101);
    }
    
    
    public static void initialiseTile(){
        
        for(int i = 0; i < tilex.size(); i++) {
            Rectangle mainBlock = new Rectangle(tilex.get(i), tiley.get(i), blockSize, blockSize);
            tileMainRect.add(mainBlock);
        }
        
        for(int i = 0; i < tilex.size(); i++) {
            topCheck = bottomCheck = leftCheck = rightCheck = false;
            // top check;
            Rectangle tileCheckTop = new Rectangle(tilex.get(i) + 5, (tiley.get(i) - blockSize) + 5, blockSize - 10, blockSize - 10);
            Rectangle tileCheckBottom = new Rectangle(tilex.get(i) + 5, (tiley.get(i) + blockSize) + 5, blockSize - 10, blockSize - 10);
            Rectangle tileCheckLeft = new Rectangle((tilex.get(i) - blockSize) + 5, tiley.get(i) + 5, blockSize - 10, blockSize - 10);
            Rectangle tileCheckRight = new Rectangle((tilex.get(i) + blockSize) + 5, tiley.get(i) + 5, blockSize - 10, blockSize - 10);
            
            if(tileCheckTop.getY() < 0){
                topCheck = true;
            }
            if(tileCheckBottom.getY() > MainClass.WINDOW_HEIGHT){
                bottomCheck = true;
            }
            if(tileCheckLeft.getX() < 0){
                leftCheck = true;
            }
            if(tileCheckRight.getX() > MainClass.WINDOW_WIDTH){
                rightCheck = true;
            }
            
            for (int j = 0; j < tileMainRect.size(); j++) {
                if(topCheck && bottomCheck && leftCheck && rightCheck){
                    break;
                }
                if(i == j){
                    continue;
                }
                if(!topCheck && tileCheckTop.getBoundsInParent().intersects(tileMainRect.get(j).getBoundsInParent())){
                    topCheck = true;
                }
                if(!bottomCheck && tileCheckBottom.getBoundsInParent().intersects(tileMainRect.get(j).getBoundsInParent())){
                    bottomCheck = true;
                }
                if(!leftCheck && tileCheckLeft.getBoundsInParent().intersects(tileMainRect.get(j).getBoundsInParent())){
                    leftCheck = true;
                }
                if(!rightCheck && tileCheckRight.getBoundsInParent().intersects(tileMainRect.get(j).getBoundsInParent())){
                    rightCheck = true;
                }
            } 

            String tilePath = "/images/tileset/";
            String tileName = "";

            if(!topCheck && bottomCheck && leftCheck && rightCheck){
                if(chance() < 30){
                    tileName = "dBrick_top";
                } else if(chance() < 30){
                    tileName = "dBrick_top2";
                } else if (chance() < 30){
                    tileName = "dBrick_top3";
                } else {
                    tileName = "dBrick_top4";
                }
            } else if(topCheck && !bottomCheck && leftCheck && rightCheck){
                if(chance() < 45){
                    tileName = "dBrick_bottom";
                } else if(chance() < 50){
                    tileName = "dBrick_bottom2";
                } else {
                    tileName = "dBrick_bottom3";
                }
            } else if(topCheck && bottomCheck && !leftCheck && rightCheck){
                tileName = "dBrick_left";
            } else if(topCheck && bottomCheck && leftCheck && !rightCheck){
                tileName = "dBrick_right";
            } else if(topCheck && bottomCheck && leftCheck && rightCheck){
                tileName = "dBrick_fill";
            } else if(!topCheck && bottomCheck && !leftCheck && rightCheck){
                tileName = "dBrick_topleft";
            } else if(!topCheck && bottomCheck && leftCheck && !rightCheck){
                tileName = "dBrick_topright";
            } else if(topCheck && !bottomCheck && !leftCheck && rightCheck){
                tileName = "dBrick_bottomleft";
            } else if(topCheck && !bottomCheck && leftCheck && !rightCheck){
                tileName = "dBrick_bottomright";
            } else {
                tileName = "dBrick_fill";
                System.out.println("error");
            }

            tilePath = tilePath + tileName + ".png";
            System.out.println(tilePath);
            System.out.println(i);
            
            ImageView ivTile = new ImageView(new Image(DisplayTile.class.getResourceAsStream(tilePath), LevelGenerator.BLOCK_SIZE, LevelGenerator.BLOCK_SIZE, true, false));
            ivTile.setLayoutX(tilex.get(i));
            ivTile.setLayoutY(tiley.get(i));
            MainClass.addToRoot(ivTile);
        }
        
    }
    
}
