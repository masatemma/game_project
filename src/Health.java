import bagel.*;
import bagel.util.Point;
import bagel.util.Colour;

/**
 * Class for health used by Player Demons and Navec
 */
public class Health {
    private final static int ORANGE_BOUNDARY = 65;
    private final static int RED_BOUNDARY = 35;
    private final static int PLAYER_HEALTH_X = 20;
    private final static int PLAYER_HEALTH_Y = 25;
    private final static int PLAYER_HP_FONT_SIZE = 30;
    private final static int ENEMY_HP_FONT_SIZE = 15;
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);
    private final static Font PLAYER_FONT = new Font("res/frostbite.ttf", PLAYER_HP_FONT_SIZE);
    private final static Font ENEMY_FONT = new Font("res/frostbite.ttf", ENEMY_HP_FONT_SIZE);
    private final DrawOptions COLOUR = new DrawOptions();
    private int maxHealthPoints;
    private int healthPoints;
    private Point healthCord;

    public Health(int maxHealthPoints){
        this.healthPoints = maxHealthPoints;
        this.maxHealthPoints = maxHealthPoints;
        COLOUR.setBlendColour(GREEN);
    }

    public int getHealthPoints(){
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints){
        this.healthPoints = healthPoints;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    /**
     * Method that renders the current health as a percentage on top left of the window or demon's and Navec's images
     * @param type that indicates if it's Player or Enemy
     * @param position the position of Player or Enemy that is used to draw the health bar
     */
    public void renderHealthPoints(String type, Point position){
        double percentageHP = ((double) healthPoints/maxHealthPoints) * 100;

        if (percentageHP <= RED_BOUNDARY){
            COLOUR.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_BOUNDARY){
            COLOUR.setBlendColour(ORANGE);
        }

        if(type.equals("Player")){
            PLAYER_FONT.drawString(Math.round(percentageHP) + "%", PLAYER_HEALTH_X, PLAYER_HEALTH_Y, COLOUR);
        } else{
            healthCord = new Point(position.x, position.y - 6);
            ENEMY_FONT.drawString(Math.round(percentageHP) + "%", healthCord.x, healthCord.y, COLOUR);
        }

    }
}
