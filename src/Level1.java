import bagel.Image;

/**
 * Class that inherits from Level class and has attributes and methods only for Level 1
 */
public class Level1 extends Level{
    private final static String WORLD_FILE_1 = "res/level1.csv";
    private final static String INSTRUCTION_MESSAGE = "PRESS SPACE TO START\nPRESS A TO ATTACK\nDEFEAT NAVEC TO WIN";
    private final static Image BACKGROUND_1_IMAGE = new Image("res/background1.png");
    private final static int INS_X = 350;
    private final static int INS_Y = 350;
    public Level1(){
        super();
    }

    public String getFilename(){
        return WORLD_FILE_1;
    }
    public Image getBackground(){
        return BACKGROUND_1_IMAGE;
    }

    /**
     * Method that renders the instruction messages before level 1
     */
    public void drawStartMessage(){
        INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE,INS_X, INS_Y);
    }
}
