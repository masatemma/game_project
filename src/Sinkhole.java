/**
 * Code from project 1 solution
 */


import bagel.Image;
import bagel.util.Rectangle;

/**
 * Sinkhole class that inherits from ObjectEntity
 * Has attributes and methods that only belong to sinkholes
 */
public class Sinkhole extends ObjectEntity{
    private final Image SINKHOLE = new Image("res/sinkhole.png");
    private final static int DAMAGE_POINTS = 30;
    private boolean isActive;

    public Sinkhole(int startX, int startY){
        super(startX, startY);
        this.isActive = true;
    }

    /**
     * Method that performs state update
     */
    public void update() {
        if (isActive){
            SINKHOLE.drawFromTopLeft(getPosition().x, getPosition().y);
        }
    }

    /**
     * Method that returns the rectangle that represents the sinkhole image
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(getPosition(), SINKHOLE.getWidth(), SINKHOLE.getHeight());
    }

    public int getDamagePoints(){
        return DAMAGE_POINTS;
    }

    /**
     * Method that checks if the sinkhole is still active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Method that deactivate the sinkhole
     */
    public void setActive(boolean active) {
        isActive = active;
    }

}