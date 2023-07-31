package engine;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LevelObjectData {
    
    public static LevelObject getObjectData(String name, String category, Group toLayer){
        
        boolean isImage = category.equals("field");
        if(isImage){
            String tilesetPath = "";
            int tileX = 0, tileY = 0, tileWidth = 0, tileHeight = 0; // Dimensions on the tileset
            switch(category){
                case "field": // FIELD TILESET - 106 tiles
                    tilesetPath = "/engine/fieldTileset.png";
                        switch(name){
                            case "Box_1": tileX = 0; tileY = 0; tileWidth = 32; tileHeight = 32; break;
                        }
                    break;
            }
            Image unscaledImg = new Image(tilesetPath);
            Image img = new Image(tilesetPath, unscaledImg.getWidth() * 2, unscaledImg.getHeight() * 2, true, false);
            ImageView iv = new ImageView(img);
            iv.setViewport(new Rectangle2D(tileX * 2, tileY * 2, tileWidth * 2, tileHeight * 2));
            return new LevelObject(iv, toLayer, tileWidth, tileHeight, false, false, name, category);
        }
        
        if(category.equals("collision")){
            Color debugColor = Color.BLACK;
            int width = 0, height = 0;
            boolean xSizable = true, ySizable = true;
            switch(name){
                case "Box": width = height = 64; break;
                case "Platform": width = 64; height = 1; ySizable = false; debugColor = Color.GREEN; break;
                case "Ladder": width = height = 64; xSizable = ySizable = false; debugColor = Color.YELLOW; break;
            }
            Rectangle col = new Rectangle(0, 0, width, height);
            col.setFill(debugColor);
            col.setOpacity(0.6);
            return new LevelObject(col, toLayer, width, height, xSizable, ySizable, name);
        }
        
        if(category.equals("character")){
            int width = 0, height = 0;
            int imgOffsetX = 0, imgOffsetY = 0;
            String displayImage = "";
            switch(name){
                case "Player": displayImage = "Woodcutter"; width = 48; height = 62; imgOffsetX = -10; imgOffsetY = -33; break;
            }
            Rectangle colBox = new Rectangle(0, 0, width, height);
            colBox.setFill(Color.web("#8E44AD"));
            String imgPath = "/characters/" + displayImage + ".png";
            Image unscaledImg = new Image(imgPath);
            Image img = new Image(imgPath, unscaledImg.getWidth() * 2, unscaledImg.getHeight() * 2, true, false);
            LevelObject newObj = new LevelObject(colBox, toLayer, width, height, false, false, name);
            newObj.setObjectImg(img, imgOffsetX, imgOffsetY);
            return newObj;
        }
        
        return null;
    }
    
    public static boolean isImageFromCategory(String category){
        return category.equals("field");
    }
    
}
