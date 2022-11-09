import bagel.Font;
import bagel.Image;

/**
 * Class that inherits from Level class and has attributes and methods only for Level 0
 */
public class Level0 extends Level{
    private final static String WORLD_FILE_0 = "res/level0.csv";
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String INSTRUCTION_MESSAGE = "PRESS SPACE TO START\nUSE ARROW KEYS TO FIND GATE";
    private final static String LEVEL_0_FINISH = "LEVEL COMPLETE!";
    private final static int TITLE_X = 260;
    private final static int TITLE_Y = 250;
    private final static int COMPLETE_MSG_X = 300;
    private final static int COMPLETE_MSG_Y = 400;
    private final static int INS_X_OFFSET = 90;
    private final static int INS_Y_OFFSET = 190;
    private final static int TITLE_FONT_SIZE = 75;
    private final Font TITLE_FONT = new Font("res/frostbite.ttf", TITLE_FONT_SIZE);
    private final static Image BACKGROUND_0_IMAGE = new Image("res/background0.png");

    public Level0(){
        super();
    }
    public String getFilename(){
        return WORLD_FILE_0;
    }
    public Image getBackground(){
        return BACKGROUND_0_IMAGE;
    }

    /**
     * Method used to draw the start screen title and instructions
     */
    public void drawStartMessage(){
        TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
        INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE,TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);
    }

    /**
     * Method that draws the message when Fae completes level 0
     */
    public void drawCompleteMsg(){
        TITLE_FONT.drawString(LEVEL_0_FINISH,COMPLETE_MSG_X, COMPLETE_MSG_Y);
    }


}
