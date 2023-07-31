package engine;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class LevelObject {
    
    public boolean deleted = false;
    public int objectLayer;
    public Group parentGroup;
    public Node object;

    public boolean isImage = false;
    public String imageCategory = ""; // e.g. "field"
    
    public boolean widthSizable;
    public boolean heightSizable;
    
    public int objectWidth;
    public int objectHeight;
    
    public int objectX;
    public int objectY;
    
    public String objectName;
    
    // For visual help
    public ImageView followIV = null;
    public int ivOffsetX;
    public int ivOffsetY;
    
    LevelObject(Node node, Group group, int width, int height, boolean canChangeWidth, boolean canChangeHeight, String name, String... imageCategory){
        object = node;
        objectName = name;
        parentGroup = group;
        objectLayer = Main.selectedLayerNumber;
        widthSizable = canChangeWidth;
        heightSizable = canChangeHeight;
        objectX = 0;
        objectY = 0;
        node.setLayoutY(Main.spaceForUI);
        
        if(node instanceof ImageView){
            this.imageCategory = imageCategory[0];
        } else {
            objectWidth = width;
            objectHeight = height;
        }
        
        parentGroup.getChildren().add(object);
        
        selectThisObject();
        
        if(followIV != null){
            followIV.setX(objectX + ivOffsetX);
            followIV.setY(objectY + ivOffsetY);
        }
        
        object.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent m) -> {
            if(!deleted && !Main.isGridCreateOn){
                selectThisObject();
            }
            m.consume();
        });
    }
    
    public void setObjectImg(Image img, int offsetX, int offsetY){
        followIV = new ImageView(img);
        followIV.setMouseTransparent(true); // Ignore mouse clicks to not block being selected
        parentGroup.getChildren().add(followIV);
        ivOffsetX = offsetX;
        ivOffsetY = offsetY;
        followIV.setX(objectX + ivOffsetX);
        followIV.setY(objectY + ivOffsetY + Main.spaceForUI);
    }
    
    private void selectThisObject(){
        Main.selectedLayerGroup = parentGroup;
        Main.selectedLayerNumber = objectLayer;
        Main.selectedObject = this;
        Main.selectedLayerTooltip.setText("Selected : " + objectName + " - L" + objectLayer);
        
        Main.xField.setText("" + (int)objectX);
        Main.yField.setText("" + (int)objectY);
        Main.widthField.setText("" + (int)objectWidth);
        Main.heightField.setText("" + (int)objectHeight);
        Main.xField.setDisable(false);
        Main.yField.setDisable(false);
        Main.widthField.setDisable(!widthSizable);
        Main.heightField.setDisable(!heightSizable);
    }
    
    public void modifyObject(){
        if(!deleted){
            
            String intRegex = "^-?\\d+$"; // a regular expression that validates a number, but doesn't require a digit after the decimal
            
            if(Main.xField.getText().matches(intRegex) && Main.yField.getText().matches(intRegex)){
                setPosition(Integer.parseInt(Main.xField.getText()), Integer.parseInt(Main.yField.getText()));
            }else {
                Main.xField.setText("" + (int)objectX);
                Main.yField.setText("" + (int)objectY);
            }
            
            if(Main.widthField.getText().matches(intRegex)){
                if(widthSizable && (Rectangle)object != null){
                    objectWidth = Integer.parseInt(Main.widthField.getText());
                    Rectangle objectToRect = (Rectangle)object;
                    objectToRect.setWidth(objectWidth);
                }
            } else {
                Main.widthField.setText("" + (int)objectWidth);
            }
            
            if(Main.heightField.getText().matches(intRegex)){
                if(heightSizable && (Rectangle)object != null){
                    objectHeight = Integer.parseInt(Main.heightField.getText());
                    Rectangle objectToRect = (Rectangle)object;
                    objectToRect.setHeight(objectHeight);
                }
            } else {
                Main.heightField.setText("" + (int)objectHeight);
            }
            
        }
    }
    
    public void setPosition(int x, int y){
        objectX = x;
        objectY = y;
        if(object instanceof Rectangle){
            Rectangle objToRect = (Rectangle)object;
            objToRect.setX(x);
            objToRect.setY(y + Main.spaceForUI);
        } else {
            object.setLayoutX(x);
            object.setLayoutY(y + Main.spaceForUI);
        }
        if(followIV != null){
            followIV.setX(objectX + ivOffsetX);
            followIV.setY(objectY + ivOffsetY + Main.spaceForUI);
        }
    }
    
    public void changeLayer(){
        if(Main.selectedLayerGroup != parentGroup && !deleted){
            parentGroup.getChildren().remove(object);
            parentGroup = Main.selectedLayerGroup;
            parentGroup.getChildren().add(object);
            objectLayer = Main.selectedLayerNumber;
            Main.selectedLayerTooltip.setText("Selected : " + objectName + " - L" + objectLayer);
        }
    }
    
    public void delete(){
        deleted = true;
        parentGroup.getChildren().remove(object);
        Main.selectedLayerTooltip.setText("Selected : None - L" + Main.selectedLayerNumber);
        Main.xField.setDisable(true); Main.xField.setText("");
        Main.yField.setDisable(true); Main.yField.setText("");
        Main.widthField.setDisable(true); Main.widthField.setText("");
        Main.heightField.setDisable(true); Main.heightField.setText("");
        
        if(followIV != null){
            parentGroup.getChildren().remove(followIV);
        }
        
        Main.selectedObject = null;
    }
    
    public void findImageInTileset(){
        
    }
    
}
