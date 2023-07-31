package level.lighting;

import java.util.ArrayList;
import engine.*;
import javafx.scene.Node;
import level.LevelGenerator;

public class lightBlockManager {
    
    static ArrayList<lightBlock> lBlocks = new ArrayList();
    static ArrayList<Node> lComps = new ArrayList();
    
    public void createLightBlock(int x, int y){
        lightBlock lBlock = new lightBlock(x, y);
        lBlocks.add(lBlock);
        RenderEngine.addToGroup("LIGHT", lBlock.displayBlock);
    }
    
    public static void updateLighting(){
        ArrayList<Double> minXRadius = new ArrayList();
        ArrayList<Double> maxXRadius = new ArrayList();
        ArrayList<Double> minYRadius = new ArrayList();
        ArrayList<Double> maxYRadius = new ArrayList();
        
        
        for(Node lComp : lComps){
            minXRadius.add(lComp.getLayoutX() - (5 * LevelGenerator.BLOCK_SIZE));
            maxXRadius.add(lComp.getLayoutX() + (5 * LevelGenerator.BLOCK_SIZE));
            maxYRadius.add(lComp.getLayoutY() + (5 * LevelGenerator.BLOCK_SIZE));
            minYRadius.add(lComp.getLayoutY() - (5 * LevelGenerator.BLOCK_SIZE));
        }
        
        for(lightBlock lBlock : lBlocks){
            for(int i = 0; i < minXRadius.size(); i++){
                if((lBlock.xLocation >= minXRadius.get(i) && lBlock.xLocation <= maxXRadius.get(i)) && (lBlock.yLocation >= minYRadius.get(i) && lBlock.yLocation <= maxYRadius.get(i))){
                    lBlock.displayBlock.setOpacity(0);
                    
                }
                System.out.println("HELLO");
            }
        }
    }
    
}
