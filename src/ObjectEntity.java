import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Abstract class that has common attributes and methods for Sinkhole, Wall, and Tree
 */
public abstract class ObjectEntity {
    private final Point position;
    public ObjectEntity(int startX, int startY){
        this.position = new Point(startX, startY);
    }
    public Point getPosition(){
        return position;
    }
    public abstract Rectangle getBoundingBox();
    public abstract void update();
}

