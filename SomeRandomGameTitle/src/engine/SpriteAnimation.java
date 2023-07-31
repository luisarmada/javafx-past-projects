package engine;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpriteAnimation extends AnimationTimer {

    private ImageView imageView; //Image view that will display our sprite

    private int totalFrames; //Total number of frames in the sequence
    public double fps; //frames per second I.E. 24

    private int cols; //Number of columns on the sprite sheet
    private int rows; //Number of rows on the sprite sheet

    public double frameWidth; //Width of an individual frame
    public double frameHeight; //Height of an individual frame

    public int currentCol = 0;
    public int currentRow = 0;

    public long lastFrame = 0;
    
    public Image spriteSheet;
    
    public String filePath;
    
    public boolean flipped = false;
    public boolean hidden = false;
    public boolean loop = true;
   
    public String animTag = "";
    public boolean loopFinish = false;
    public double desiredOpacity = 1;
    
    public SpriteAnimation(ImageView imageView, String filePath, int columns, int rows, int totalFrames, int frameWidth, int frameHeight, double frameScale, double framesPerSecond) {
        this.imageView = imageView;
        Image image = new Image(filePath);
        
        spriteSheet = new Image(filePath, image.getWidth() * frameScale, image.getHeight() * frameScale, true, false);
        imageView.setImage(spriteSheet);
        imageView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));
        
        this.filePath = filePath;
        cols = columns;
        this.rows = rows;
        this.totalFrames = totalFrames;
        this.frameWidth = frameWidth * frameScale;
        this.frameHeight = frameHeight * frameScale;
        fps = framesPerSecond;
        
        lastFrame = System.nanoTime();
    }

    public void changeSprite(ImageView imageView, String filePath, int columns, int rows, int totalFrames, int frameWidth, int frameHeight, double frameScale, double framesPerSecond){
        if (this.filePath.equals(filePath)){
            return;
        }
        this.filePath = filePath;
        
        Image image = new Image(filePath);
        spriteSheet = new Image(filePath, image.getWidth() * frameScale, image.getHeight() * frameScale, true, false);
        imageView.setImage(spriteSheet);
        imageView.setViewport(new Rectangle2D(0, 0, frameWidth * frameScale, frameHeight * frameScale));
        
        loop = true;
        currentRow = 0;
        currentCol = 0;
        cols = columns;
        this.rows = rows;
        this.totalFrames = totalFrames;
        this.frameWidth = frameWidth * frameScale;
        this.frameHeight = frameHeight * frameScale;
        fps = framesPerSecond;

        lastFrame = System.nanoTime();
    }
    
    @Override
    public void handle(long now) {
        imageView.setImage(spriteSheet);
        
        imageView.setScaleX(flipped ? -1 : 1);
        imageView.setOpacity(hidden ? 0 : desiredOpacity);
        if(fps != 0){
            
            int frameJump = (int) Math.floor((now - lastFrame) / (1000000000 / fps)); //Determine how many frames we need to advance to maintain frame rate independence
            //Do a bunch of math to determine where the viewport needs to be positioned on the sprite sheet
            if (frameJump >= 1 && !(!loop && currentCol == totalFrames - 1)) {
                loopFinish = false;
                lastFrame = now;
                int addRows = (int) Math.floor((float) frameJump / (float) cols);
                int frameAdd = frameJump - (addRows * cols);

                if (currentCol + frameAdd >= cols) {
                    currentRow += addRows + 1;
                    currentCol = frameAdd - (cols - currentCol);
                } else {
                    currentRow += addRows;
                    currentCol += frameAdd;
                }
                currentRow = (currentRow >= rows) ? currentRow - ((int) Math.floor((float) currentRow / rows) * rows) : currentRow;

                //The last row may or may not contain the full number of columns
                if ((currentRow * cols) + currentCol >= totalFrames) {
                    currentRow = 0;
                    currentCol = Math.abs(currentCol - (totalFrames - (int) (Math.floor((float) totalFrames / cols) * cols)));
                }

                imageView.setViewport(new Rectangle2D(currentCol * frameWidth, currentRow * frameHeight, frameWidth, frameHeight));
            } else if((!loop && currentCol == totalFrames - 1)){
                loopFinish = true;
            }
        }
    }
}
