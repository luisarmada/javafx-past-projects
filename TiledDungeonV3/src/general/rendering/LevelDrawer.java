package general.rendering;

import general.entity.SpriteAnimation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class LevelDrawer extends AnimationTimer{
    
    private final ArrayList<ArrayList<Tile>> drawTiles = new ArrayList();
    private final ArrayList<Double> tileTargetOpacity = new ArrayList();
    private final ArrayList<Color> tileTargetColour = new ArrayList();
    
    public void addDrawTiles(double startOpacity, double targetOpacity, Color targetColour, ArrayList<Tile> tiles){
        if(startOpacity != -1){
            tiles.forEach(t ->{
                t.tileBox.setOpacity(startOpacity);
            });
        }
        
        drawTiles.add(tiles);
        tileTargetOpacity.add(targetOpacity);
        tileTargetColour.add(targetColour);
    }
    
    public void clear(){
        drawTiles.clear();
        tileTargetOpacity.clear();
        tileTargetColour.clear();
    }

    @Override
    public void handle(long now) {
        if(!drawTiles.isEmpty()){
            drawNextTiles();
        } else {
            stop();
        }
    }
    
    private void drawNextTiles(){
        drawTiles.get(0).forEach(t ->{
                if(tileTargetOpacity.get(0) != -1){
                    t.tileBox.setOpacity(tileTargetOpacity.get(0));
                }
                if(tileTargetColour.get(0) != null){
                    t.tileBox.setFill(tileTargetColour.get(0));
                }
            });

            drawTiles.remove(0);
            tileTargetOpacity.remove(0);
            tileTargetColour.remove(0);
    }
        
    // Auto-Tiling
    public void drawTileSprites(Group grp, ArrayList<Tile> pathList, ArrayList<Tile> wallList){
        SpriteAnimation sAnim = new SpriteAnimation();
        
        // Draw the sprites for path tiles
        ArrayList<String> pathTileTypeNames = new ArrayList<>(Arrays.asList("", "none", "t", "l", "tl", "r", "tr", "lr", "tlr", "b",
                "tb", "bl", "tbl", "br", "tbr", "blr", "tblr", "", "", ""));
        ArrayList<Image> pathTileTypeImages = new ArrayList();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 5; x++) {
                pathTileTypeImages.add(sAnim.selectFromSheet("floor_tileset", x, y, 32, 32));
            }
        }
        pathList.forEach(t ->{
            String tileType = "";
            if(t.aboveTile.tState == 1){
                tileType = tileType + "t";
            }
            if(t.belowTile.tState == 1){
                tileType = tileType + "b";
            }
            if(t.leftTile.tState == 1){
                tileType = tileType + "l";
            }
            if(t.rightTile.tState == 1){
                tileType = tileType + "r";
            }
            if(tileType.equals("")){
                tileType = "none";
            }
            t.drawSprite(grp, pathTileTypeImages.get(pathTileTypeNames.indexOf(tileType)));
        });
        
        // Draw the sprites for wall tiles
        ArrayList<String> wallTileTypeNames = new ArrayList<>(Arrays.asList("", "blr", "tbr", "brtltrblbr", "", "tbl", "bltltrblbr", "",
                "tb", "btltrblbr", "", "", "b", "tlr", "lr", "trtltrblbr",
                "rtltrblbr", "", "tltltrblbr", "ltltrblbr", "", "ttltrblbr", "tltrblbr", "",
                "", "blbr", "", "", "r", "", "", "trbr",
                "", "br", "", "", "l", "", "", "",
                "tlbl", "bl", "t", "tltr", "tr", "tl", "none", ""));
        ArrayList<Image> wallTileTypeImages = new ArrayList();
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 8; x++) {
                wallTileTypeImages.add(sAnim.selectFromSheet("wall_tileset", x, y, 32, 32));
            }
        }
        wallList.forEach(t ->{
            String tileType = "";
            boolean up = t.aboveTile != null;
            boolean down = t.belowTile != null;
            boolean left = t.leftTile != null;
            boolean right = t.rightTile != null;
            if(up){
                if(t.aboveTile.tState == 0){
                    tileType = tileType + "t";
                }
            }
            if(down){
                if(t.belowTile.tState == 0){
                    tileType = tileType + "b";
                }
            }
            if(left){
                if(t.leftTile.tState == 0){
                    tileType = tileType + "l";
                } 
            }
            if(right){
                if(t.rightTile.tState == 0){
                    tileType = tileType + "r";
                } 
            }
            
            // If our tile sprite has to consider corners
            if(!(tileType.equals("blr") || tileType.equals("tbr") || tileType.equals("tbl") || tileType.equals("tlr") ||
                    tileType.equals("tb") || tileType.equals("lr"))){
                if(up && left){
                    if(t.topLeftTile.tState == 0){
                        tileType = tileType + "tl";
                    }
                }
                if(up && right){
                    if(t.topRightTile.tState == 0){
                        tileType = tileType + "tr";
                    }
                }
                if(down && left){
                    if(t.botLeftTile.tState == 0){
                        tileType = tileType + "bl";
                    }
                }
                if(down && right){
                    if(t.botRightTile.tState == 0){
                        tileType = tileType + "br";
                    }
                }
            }
            
            if(tileType.equals("ttl") || tileType.equals("ttr") || tileType.equals("ttltr")){
                tileType = "t";
            }
            if(tileType.equals("bbl") || tileType.equals("bbr") || tileType.equals("bblbr")){
                tileType = "b";
            }
            if(tileType.equals("ltl") || tileType.equals("lbl") || tileType.equals("ltlbl")){
                tileType = "l";
            }
            if(tileType.equals("rtr") || tileType.equals("rbr") || tileType.equals("rtrbr")){
                tileType = "r";
            }
            
            if(tileType.equals("") || wallTileTypeNames.indexOf(tileType) == -1){
                tileType = "none";
            }
            t.drawSprite(grp, wallTileTypeImages.get(wallTileTypeNames.indexOf(tileType)));
        });
        
    }
}
