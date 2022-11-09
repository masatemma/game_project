/**
 * Codes are partially from project 1 solution by Tharun Dharmawickrema
 */

import bagel.Image;
import bagel.util.Rectangle;

/**
 * Wall class that inherits from ObjectEntity
 * Has attributes and methods that only belong to walls
 */
public class Wall extends ObjectEntity {
    private final Image WALL = new Image("res/wall.png");

    public Wall(int startX, int startY){
        super(startX, startY);
    }

    /**
     * Method that performs state update
     */
    public void update() {
        WALL.drawFromTopLeft(getPosition().x, getPosition().y);
    }

    public Rectangle getBoundingBox(){
        return new Rectangle(getPosition(), WALL.getWidth(), WALL.getHeight());
    }
}