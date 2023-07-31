package engine;

public class SpritesheetInformation {
    
    public String filePath;
    public int totalCols;
    public int totalRows;
    public int totalFrames;
    public int frameWidth;
    public int frameHeight;
    public int spriteScale;
    public int framesPerSecond;
    
    public SpritesheetInformation(String filePath, int cols, int rows, int totalFrames, int frameWidth, int frameHeight, int scale, int fps){
        this.filePath = filePath;
        totalCols = cols;
        totalRows = rows;
        this.totalFrames = totalFrames;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        spriteScale = scale;
        framesPerSecond = fps;
    }
    
}
