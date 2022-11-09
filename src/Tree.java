/**
 * Codes are partially from project 1 solution by Tharun Dharmawickrema
 */

import bagel.Image;
import bagel.util.Rectangle;

/**
 * Tree class that inherits from ObjectEntity
 * Has attributes and methods that only belong trees
 */
public class Tree extends ObjectEntity{
    private final Image TREE = new Image("res/tree.png");
    public Tree(int startX, int startY){
        super(startX, startY);
    }

    /**
     * Method that performs state update
     */
    public void update() {
        TREE.drawFromTopLeft(getPosition().x, getPosition().y);
    }
    public Rectangle getBoundingBox(){
        return new Rectangle(getPosition(), TREE.getWidth(), TREE.getHeight());
    }

}
