import bagel.Font;
import bagel.Image;

/**
 * Abstract class that has common attributes and methods for level 0 and level 1
 */
public abstract class Level {
    private final static int INSTRUCTION_FONT_SIZE = 40;
    protected final Font INSTRUCTION_FONT = new Font("res/frostbite.ttf", INSTRUCTION_FONT_SIZE);

    public Level(){
    }

    /**
     * Abstract methods
     */
    public abstract String getFilename();
    public abstract Image getBackground();
    public abstract void drawStartMessage();


}
