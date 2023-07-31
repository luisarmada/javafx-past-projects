package engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.imageio.ImageIO;


public class Main extends Application {

    private final int levelWidth = 1280;
    private final int levelHeight = 720;
    private final int OuterLevelLeftSize = 64;
    private final int OuterLevelRightSize = 64;
    private final int OuterLevelUnderSize = 48;
    private final int OuterLevelUpperSize = 64;
    
    public static final int spaceForUI = 70;
    
    public Group ROOT = new Group();
    public Group UI_Group = new Group();
    public Group gridGroup = new Group();
    public Group playerViewGroup = new Group();
    public static Group collisionGroup = new Group();
    
    public static Group selectedLayerGroup;
    public static LevelObject selectedObject;
    public static int selectedLayerNumber = 1; // 0 for game objects

    ArrayList<Group> imageLayers = new ArrayList();
    public boolean gridHidden = false;
    public boolean playerViewHidden = false;
    public boolean collisionLayerHidden = false;
    public boolean imageLayersHidden = false;
    
    public static ArrayList<LevelObject> levelObjectList = new ArrayList();
    
    public Stage rootStage;
    
    public static TextField widthField;
    public static TextField heightField;
    public static TextField xField;
    public static TextField yField;
    
    public static Text selectedLayerTooltip;
    
    public double dragAnchorX;
    public double dragAnchorY;
    
    public static boolean isGridCreateOn = false;
    public static boolean isGridLockOn = false;
    
    public TextField objectPath;
    public TextField objectName;
    
    @Override public void start(Stage stage){
        Scene scene = new Scene(ROOT, levelWidth + OuterLevelLeftSize + OuterLevelRightSize, levelHeight + OuterLevelUnderSize + OuterLevelUpperSize + spaceForUI, Color.web("#D7BDE2"));
        rootStage = stage;
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Level Editor");
        stage.show();
        
        Rectangle UIBlock = new Rectangle(0, 0, rootStage.getWidth(), spaceForUI);
        UIBlock.setFill(Color.web("#D7BDE2"));
        UI_Group.getChildren().add(UIBlock);
        UI_Group.getChildren().add(new Line(0, spaceForUI, ROOT.getScene().getWidth(), spaceForUI));
        
        // CREATING LAYERS, THE GREATER THE LAYER, THE CLOSER IT IS TO THE CAMERA
        int totalLayers = 6;
        selectedLayerTooltip = new Text();
        selectedLayerTooltip.setX(215 + (40 * (totalLayers)));
        selectedLayerTooltip.setLayoutY(27);
        UI_Group.getChildren().add(selectedLayerTooltip);
        for(int i= 0; i < totalLayers; i++){ // LAYERS
            int layerSelected = i + 1;
            Button layerButton = new Button("L" + layerSelected);
            layerButton.setLayoutX(215 + (40 * i)); //250
            layerButton.setLayoutY(10);
            UI_Group.getChildren().add(layerButton);
            
            Group layerGroup = new Group();
            imageLayers.add(layerGroup);
            ROOT.getChildren().add(layerGroup);
            selectedLayerGroup = layerGroup;
            selectedLayerTooltip.setText("Selected : None - L" + layerSelected);
            selectedLayerNumber = layerSelected;
            
            layerButton.setOnAction((ActionEvent e) -> {
                selectedLayerGroup = layerGroup;
                selectedLayerNumber = layerSelected;
                if(selectedObject != null){
                  selectedObject.changeLayer();  
                } else {
                    selectedLayerTooltip.setText("Selected : None - L" + layerSelected);
                }
            });
        }
        ROOT.getChildren().add(collisionGroup); 
        
        Button toggleCollisionLayer = new Button("Toggle L. Collision [L0]");
        toggleCollisionLayer.setLayoutX(375);
        toggleCollisionLayer.setLayoutY(40);
        toggleCollisionLayer.setOnAction((ActionEvent e) -> {
            collisionLayerHidden = !collisionLayerHidden;
            collisionGroup.setVisible(!collisionLayerHidden);
        });
        Button toggleImageLayers = new Button("Toggle Ls. Image");
        toggleImageLayers.setLayoutX(515);
        toggleImageLayers.setLayoutY(40);
        toggleImageLayers.setOnAction((ActionEvent e) -> {
            imageLayersHidden = !imageLayersHidden;
            imageLayers.forEach(g -> {
                g.setVisible(!imageLayersHidden);
            });
        });
        UI_Group.getChildren().addAll(toggleCollisionLayer, toggleImageLayers);
        
        // ALTERING SELECTED IMAGE OR OBJECT
        xField = new TextField();
        yField = new TextField();
        widthField = new TextField();
        heightField = new TextField();
        xField.setDisable(true); yField.setDisable(true);
        widthField.setDisable(true); heightField.setDisable(true);
        xField.setPromptText("X-Coordinate");
        yField.setPromptText("Y-Coordinate");
        widthField.setPromptText("Width");
        heightField.setPromptText("Height");
        xField.setLayoutX(650); xField.setLayoutY(10);
        yField.setLayoutX(650); yField.setLayoutY(40);
        widthField.setLayoutX(810); widthField.setLayoutY(10);
        heightField.setLayoutX(810); heightField.setLayoutY(40);
        
        Button modifyButton = new Button("Modify");
        modifyButton.setLayoutX(970);
        modifyButton.setLayoutY(10);
        modifyButton.setOnAction((ActionEvent e) -> {
            if(selectedObject != null){
                selectedObject.modifyObject();
            }
        });
        UI_Group.getChildren().addAll(xField, yField, widthField, heightField, modifyButton);
        
        // CREATING IMAGES AND OBJECTS
        objectName = new TextField();
        objectPath = new TextField();
        objectName.setPromptText("Name");
        objectPath.setPromptText("Folder");
        objectName.setLayoutX(5); objectName.setLayoutY(10);
        objectPath.setLayoutX(5); objectPath.setLayoutY(40);
        
        Button newObjectButton = new Button("Create");
        newObjectButton.setLayoutX(160);
        newObjectButton.setLayoutY(10);
        newObjectButton.setOnAction((ActionEvent e) -> {
            createObject(0, 0);
        });
        Text gridCreateTooltip = new Text("Off");
        gridCreateTooltip.setX(295);
        gridCreateTooltip.setY(58);
        
        Button gridCreateButton = new Button("GridCreate");
        gridCreateButton.setLayoutX(215);
        gridCreateButton.setLayoutY(40);
        gridCreateButton.setOnAction((ActionEvent e) -> {
           isGridCreateOn = !isGridCreateOn;
           gridCreateTooltip.setText(isGridCreateOn ? "On" : "Off");
        });
        Button deleteObjectButton = new Button("Delete");
        deleteObjectButton.setLayoutX(160);
        deleteObjectButton.setLayoutY(40);
        deleteObjectButton.setOnAction((ActionEvent e) -> {
            if(selectedObject != null){
                levelObjectList.remove(selectedObject);
                selectedObject.delete();
            }
        });
        UI_Group.getChildren().addAll(newObjectButton, gridCreateButton, gridCreateTooltip, deleteObjectButton, objectName, objectPath);
        
        // GRID STUFF
        drawGridAndPlayerView();
        
        // OPTION TO HIDE GRID
        Button toggleGridButton = new Button("Toggle Grid");
        toggleGridButton.setLayoutX(1090);
        toggleGridButton.setLayoutY(10);
        toggleGridButton.setOnAction((ActionEvent e) -> {
            gridHidden = !gridHidden;
            gridGroup.setVisible(!gridHidden);
        });
        Button togglePlayerViewButton = new Button("Toggle PlayerView");
        togglePlayerViewButton.setLayoutX(1090);
        togglePlayerViewButton.setLayoutY(40);
        togglePlayerViewButton.setOnAction((ActionEvent e) -> {
            playerViewHidden = !playerViewHidden;
            playerViewGroup.setVisible(!playerViewHidden);
        });
        // Lock objects to grid
        Text gridLockTooltip = new Text("Off");
        gridLockTooltip.setX(1040); gridLockTooltip.setY(58);
        Button gridLockButton = new Button("Grid Lock");
        gridLockButton.setLayoutX(970);
        gridLockButton.setLayoutY(40);
        gridLockButton.setOnAction((ActionEvent e) -> {
            isGridLockOn = !isGridLockOn;
            gridLockTooltip.setText(isGridLockOn ? "On" : "Off");
        });
        UI_Group.getChildren().addAll(toggleGridButton, togglePlayerViewButton, gridLockButton, gridLockTooltip);
        
        // PLAY TEST AND FINISH
        Button playTestButton = new Button("Playtest Level");
        playTestButton.setLayoutX(1230);
        playTestButton.setLayoutY(10);
        playTestButton.setOnAction((ActionEvent e) -> {
            
        });
        Button finishLevelButton = new Button("Finish Level");
        finishLevelButton.setLayoutX(1230);
        finishLevelButton.setLayoutY(40);
        finishLevelButton.setOnAction((ActionEvent e) -> {
            
        });
        UI_Group.getChildren().addAll(playTestButton, finishLevelButton);
        
        ROOT.getChildren().add(UI_Group);
        
        // OBJECT DRAGGING
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if(e.getButton() == MouseButton.SECONDARY && selectedObject != null){
                System.out.println(e.getX() + " ; " + e.getY());
                dragAnchorX = e.getX() - selectedObject.objectX;
                dragAnchorY = e.getY() - selectedObject.objectY;
            }
            if(e.getButton() == MouseButton.PRIMARY && isGridCreateOn && e.getY() > spaceForUI){
                // CREATE OBJECTS LOCKED TO GRID
                int tileSize = 64;
                createObject((int)(tileSize * Math.floor(e.getX()/tileSize)), (int)(tileSize * Math.floor((e.getY() - spaceForUI)/tileSize)));
            }
        });
        
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if(e.isSecondaryButtonDown() && selectedObject != null){
                int tileSize = 64;
                double newObjectX = e.getX() - dragAnchorX;
                double newObjectY = e.getY() - dragAnchorY;
                
                xField.setText("" + (int)(isGridLockOn ? (tileSize * Math.floor(newObjectX/tileSize)) : newObjectX));
                yField.setText("" + (int)(isGridLockOn ? (tileSize * Math.floor(newObjectY/tileSize)) : newObjectY));
                selectedObject.modifyObject();
            }
        });
        
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
           if(e.getCode() == KeyCode.DELETE && selectedObject != null){
               selectedObject.delete();
           }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void createObject(int x, int y){
        if(LevelObjectData.isImageFromCategory(objectPath.getText())){ // Spawn coded objects when create button is pressed (if path is empty)
            selectedLayerGroup = collisionGroup;
            selectedLayerNumber = 0;
        } else if(selectedLayerNumber == 0){
            selectedLayerGroup = imageLayers.get(imageLayers.size() - 1);
            selectedLayerNumber = imageLayers.size();
        }
        LevelObject newObj = LevelObjectData.getObjectData(objectName.getText(), objectPath.getText(), selectedLayerGroup);
        levelObjectList.add(newObj);
        selectedObject = newObj;
    }
    
    private void drawGridAndPlayerView(){
        int tileSize = 64;
        
        // Vertical lines
        for(int i = 0; i <= ROOT.getScene().getWidth() / tileSize; i++){
            int xLocation = (tileSize * i);
            Line verticalLine = new Line(xLocation, spaceForUI, xLocation, ROOT.getScene().getHeight());
            verticalLine.setOpacity(0.4);
            gridGroup.getChildren().add(verticalLine);
        }
        
        // Horizontal Lines
        for(int i = 0; i <= (ROOT.getScene().getHeight() - spaceForUI) / tileSize; i++){
            int yLocation =  spaceForUI + (tileSize * i);
            Line horizontalLine = new Line(0, yLocation, ROOT.getScene().getWidth(), yLocation);
            horizontalLine.setOpacity(0.4);
            gridGroup.getChildren().add(horizontalLine);
        }
        UI_Group.getChildren().add(gridGroup);
        
        Line topLine = new Line(OuterLevelLeftSize, spaceForUI + OuterLevelUpperSize, ROOT.getScene().getWidth() - OuterLevelRightSize, spaceForUI + OuterLevelUpperSize);
        Line botLine = new Line(OuterLevelLeftSize, ROOT.getScene().getHeight() - OuterLevelUnderSize, ROOT.getScene().getWidth() - OuterLevelRightSize, ROOT.getScene().getHeight() - OuterLevelUnderSize);
        Line leftLine = new Line(OuterLevelLeftSize, spaceForUI + OuterLevelUpperSize, OuterLevelLeftSize, ROOT.getScene().getHeight() - OuterLevelUnderSize);
        Line rightLine = new Line(ROOT.getScene().getWidth() - OuterLevelRightSize, spaceForUI + OuterLevelUpperSize, ROOT.getScene().getWidth() - OuterLevelRightSize, ROOT.getScene().getHeight() - OuterLevelUnderSize);
        topLine.setStroke(Color.RED);
        botLine.setStroke(Color.RED);
        leftLine.setStroke(Color.RED);
        rightLine.setStroke(Color.RED);
        playerViewGroup.getChildren().addAll(topLine, botLine, leftLine, rightLine);
        UI_Group.getChildren().add(playerViewGroup);
        
    }
    
    private void outputLevel() { // Converts scene to an image and outputs
        
        String levelObjectData = "";
        
        for (LevelObject obj : levelObjectList) {
            // +1 for collision layer
            // layerNumber; objectName; xLocation; yLocation; width; height; isImage; <Optional> imageCategory; imageXTile; imageYTile
            for(int j = 0; j < imageLayers.size() + 1; j++){
                System.out.println("");
            }
        }
        
        
    }
    
}
