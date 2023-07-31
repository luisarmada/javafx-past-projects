package general.rendering;

import general.rendering.LevelDrawer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javafx.scene.Group;

public class LevelGenerator{
    
    public final int T_SIZE = 32;
    
    private final Random rand = new Random();
    public final int wTiles;
    public final int hTiles;
    private Tile[][] tArray;
    private ArrayList<Tile> tList = new ArrayList();
    private ArrayList<Tile> connectorTileList;
    public Group tGroup = null;
    
    public ArrayList<Tile> pathList = new ArrayList();
    public ArrayList<Tile> wallList = new ArrayList();
    
    private final LevelDrawer lDrawer;
    
    private boolean shouldDraw;
    private boolean drawVisTiles;
    
    public LevelGenerator(Group grp, int wt, int ht, LevelDrawer lDrawer){
        this.tGroup = grp;
        this.wTiles = wt;
        this.hTiles = ht;
        this.lDrawer = lDrawer;
    }
    
    public void generateDungeon(Group grp, boolean draw, boolean drawVisTiles){
        if(tGroup != null){ // A level already exists
            tGroup.getChildren().clear();
            tGroup = null;
        }
        pathList.clear();
        wallList.clear();
        tGroup = grp;
        shouldDraw = draw;
        this.drawVisTiles = drawVisTiles;
        generateTiles(grp, T_SIZE);
        generateRooms(50, 1, 3, 1, 3);
        generateMaze();
    }
    
    private void generateTiles(Group grp, int size){
        //wTiles = LV_W / size; // Number of tiles along the width of the screen
        //hTiles = LV_H / size; // Number of tiles along the height of the screen
        
        tArray = new Tile[wTiles][hTiles];
        tList = new ArrayList();
        connectorTileList = new ArrayList();
        
        for (int i = 0; i < hTiles; i++) {
            for (int j = 0; j < wTiles; j++) {
                Tile t = new Tile(i, j, size);
                tArray[j][i] = t;
                tList.add(t);
                if(i == 0 || i == hTiles - 1 || j == 0 || j == wTiles - 1){
                    t.setState(1);
                    addToWallList(t);
                } else if(i % 2 == 0 && j % 2 == 0){
                    t.setState(1);
                    addToWallList(t);
                    connectorTileList.add(t);
                }
                grp.getChildren().add(t.tileBox);
                if (drawVisTiles) {
                    grp.getChildren().add(t.visibilityBox);
                    t.tileLightLevel = 0;
                }
            }
        }
        
        // Setup Adjacent Tiles
        for (int i = 0; i < tList.size(); i++) {
            Tile t = tList.get(i);
            boolean above, below, left, right;
            above = below = left = right = false;
            if(t.gy != 0){ // Adjacent Tile Above
                t.adjTileList.add(tArray[t.gx][t.gy - 1]);
                t.aboveTile = tArray[t.gx][t.gy - 1];
                above = true;
            }
            if(t.gy != hTiles - 1){ // Adjacent Tile Below
                t.adjTileList.add(tArray[t.gx][t.gy + 1]);
                t.belowTile = tArray[t.gx][t.gy + 1];
                below = true;
            }
            if(t.gx != 0){ // Adjacent Tile Left
                t.adjTileList.add(tArray[t.gx - 1][t.gy]);
                t.leftTile = tArray[t.gx - 1][t.gy];
                left = true;
            }
            if(t.gx != wTiles - 1){ // Adjacent Tile Right
                t.adjTileList.add(tArray[t.gx + 1][t.gy]);
                t.rightTile = tArray[t.gx + 1][t.gy];
                right = true;
            }
            t.adjTileWithCornerList.addAll(t.adjTileList);
            if(above){
                if(left){
                    t.topLeftTile = tArray[t.gx - 1][t.gy - 1];
                    t.adjTileWithCornerList.add(tArray[t.gx - 1][t.gy - 1]);
                }
                if(right){
                    t.topRightTile = tArray[t.gx + 1][t.gy - 1];
                    t.adjTileWithCornerList.add(tArray[t.gx + 1][t.gy - 1]);
                }
            }
            if(below){
                if(left){
                    t.botLeftTile = tArray[t.gx - 1][t.gy + 1];
                    t.adjTileWithCornerList.add(tArray[t.gx - 1][t.gy + 1]);
                }
                if(right){
                    t.botRightTile = tArray[t.gx + 1][t.gy + 1];
                    t.adjTileWithCornerList.add(tArray[t.gx + 1][t.gy + 1]);
                }
            }
        }
    }
    
    private void generateRooms(int attempts, int addWMin, int addWMax, int addHMin, int addHMax){
        ArrayList<Tile> roomTiles = new ArrayList(); 
        
        for (int i = 0; i < attempts; i++) {
            int rw = 3 + ((rand.nextInt(addWMin + addWMax) + addWMin) * 2);
            int rh = 3 + ((rand.nextInt(addHMin + addHMax) + addHMin) * 2);
            
            int rxMax = (((wTiles - 1) - 2) - rw) / 2; 
            int rx = (rand.nextInt(1 + rxMax) + 1) * 2;
            int ryMax = (((hTiles - 1) - 2) - rh) / 2; 
            int ry = (rand.nextInt(1 + ryMax) + 1) * 2;
            
            ArrayList<Tile> checkTiles = new ArrayList();
            boolean validRoom = true;
            for (int j = rx; j < rx+rw; j++) {
                for (int k = ry; k < ry+rh; k++) {
                    checkTiles.add(tArray[j][k]);
                    if (roomTiles.contains(tArray[j][k])){
                        validRoom = false;
                        break;
                    }
                }
            }
            
            if(validRoom){
                roomTiles.addAll(checkTiles);
                ArrayList<Tile> pDoorTiles = new ArrayList();
                for (int j = rx; j < rx+rw; j++) {
                    for (int k = ry; k < ry+rh; k++) {
                        if(j == rx || j == (rx+rw)-1 || k == ry || k == (ry+rh)-1){
                            tArray[j][k].setState(1);
                            addToWallList(tArray[j][k]);
                            if(j % 2 != 0 || k % 2 != 0){
                                pDoorTiles.add(tArray[j][k]);
                            }
                        } else {
                            tArray[j][k].setState(0);
                            pathList.add(tArray[j][k]);
                            if(wallList.contains(tArray[j][k])){
                                wallList.remove(tArray[j][k]);
                            }
                        }
                    }
                }
                int randDoor = rand.nextInt(pDoorTiles.size());
                Tile doorTile = pDoorTiles.get(randDoor);
                doorTile.isDoorTile = true;
                doorTile.setState(0);
                pathList.add(doorTile);
                wallList.remove(doorTile);
                
                
                if(shouldDraw){
                    checkTiles.remove(doorTile);
                    lDrawer.addDrawTiles(0, 1, null, new ArrayList<>(checkTiles));
                    lDrawer.addDrawTiles(0, 1, null, new ArrayList<>(Arrays.asList(doorTile))); 
                }
                
            }
            
        }
    }
    
    private void generateMaze(){
        ArrayList<Tile> pathTiles = new ArrayList();
        pathTiles.add(tArray[1][1]);
        
        int lastIndex = pathTiles.size() - 1;
        while(!pathTiles.isEmpty()){
            if(!tilePath(pathTiles, pathTiles.get(lastIndex))){
                pathTiles.remove(lastIndex);
            }
        }
    }
    
    private boolean tilePath(ArrayList tList, Tile t){
        t.setState(0);
        pathList.add(t);
        if(shouldDraw){
            lDrawer.addDrawTiles(0, 1, null, new ArrayList<>(Arrays.asList(t)));  
        }
        
        ArrayList<Tile> branchableTList = new ArrayList();
        for (int i = 0; i < t.adjTileList.size(); i++) {
            if(t.adjTileList.get(i).tState == -1){
                if(!t.adjTileList.get(i).checkVisited()){
                    branchableTList.add(t.adjTileList.get(i));
                }
            }
        }
        boolean canBranch = !branchableTList.isEmpty();
        if(canBranch){
            int randBranch = rand.nextInt(branchableTList.size());
            Tile nextTile = branchableTList.get(randBranch);
            tList.add(nextTile);
            tilePath(tList, nextTile);
        }
        return canBranch;
    }
    
    public Tile getTileAt(int x, int y){
        return tArray[x][y];
    }
    
    public void addToWallList(Tile t){
        if(!wallList.contains(t)){
            wallList.add(t);
        }
    }
    
    public void sendVisTilesToFront(){
        tList.forEach(t -> {
           t.visibilityBox.toFront(); 
        });
    }
    
}
