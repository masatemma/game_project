import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.Random;

/**
 * Abstract class of Demons and Navec
 * Have shared attributes and methods of demons and Navec
 */
public abstract class Enemy {
    protected String ENEMY_TYPE = "ENEMY";
    protected final static int UP = 1;
    protected final static int DOWN = 2;
    protected final static int RIGHT = 3;
    protected final static int LEFT = 4;
    private final static int INV_DURATION = 3000;
    private final static double MAX_SPEED = 0.7;
    private final static double MIN_SPEED = 0.2;
    private final static double ONE_SECOND = 1000;
    private final static double REFRESH_RATE = 60;
    protected final DrawOptions ROTATION = new DrawOptions();

    protected boolean invincible;
    protected boolean facingRight;
    protected boolean attackState;
    protected int invincibleFrame;
    protected int direction;
    protected double speed;
    protected double ori_speed;
    protected Point position;
    protected Point prevPosition;
    protected Point centre;
    protected Point firePosition;
    protected Image currentImage;
    protected Health health;
    protected Image fire;
    private Random rand = new Random();

    public Enemy(int startX, int startY){
        this.position = new Point(startX, startY);
        this.direction = rand.nextInt((LEFT - UP) + 1) + UP;
        this.speed= MIN_SPEED + (MAX_SPEED - MIN_SPEED)* rand.nextDouble();
        ori_speed = this.speed;
        this.facingRight = rand.nextBoolean();
        this.attackState = false;

    }

    public Point getPosition(){ return position;}
    public Image getCurrentImage(){ return currentImage;}
    public Point getCentre(){ return centre;}
    public double getOriginalSpeed(){return ori_speed;}
    public Rectangle getFireBoundingBox() { return new Rectangle(firePosition, fire.getWidth(), fire.getHeight());}
    public Health getHealth(){return health;}
    public boolean isInvincible(){return invincible;}

    /**
     * Method that sets the new speed of demons and Navec as timescale being changed
     * @param speed new speed of the Enemy
     */
    public void setNewSpeed(double speed){
        if(speed >= 0){
            this.speed = speed;
        }
        else{
            this.speed = 0;
        }
    }

    /**
     * Method that updates demons' or Navec's positions and centres
     * @param xMove the amount pixels to be moved horizontally
     * @param yMove the amount pixels to be moved vertically
     */
    protected void move(double xMove, double yMove){
        double newX = position.x + xMove;
        double newY = position.y + yMove;
        setNewPosition(newX, newY);
        setNewCentre(newX, newY);
    }

    /**
     * Method that moves the Player back to previous position
     */
    protected void moveBack(){
        position = prevPosition;
        if(direction == UP){
            this.direction = DOWN;
        }
        else if (direction == DOWN){
            this.direction = UP;
        }
        else if(direction == RIGHT){
            this.direction = LEFT;
        }
        else if(direction == LEFT){
            this.direction = RIGHT;
        }
    }
    /**
     * Method that stores the character's previous position
     */
    protected void setPrevPosition(){
        prevPosition = new Point(position.x, position.y);
    }
    /**
     * Method that stores the character's new position
     */
    protected void setNewPosition(double newX, double newY){
        this.position = new Point(newX, newY);
    }

    /**
     * Method that sets the new centre of demons and Navec as they move
     * @param newX x value of the new position
     * @param newY y value of the new position
     */
    protected void setNewCentre(double newX, double newY){
        this.centre =  new Point(newX + (this.currentImage.getWidth()/2),
                newY + (this.currentImage.getHeight()/2));
    }

    /**
     * Method that checks if Fae has entered the attack range of demons or Navec
     * If Fae enters the attack range, it draws the fire image depends on where Fae is.
     * @param playerCentre the centre of the player
     */
    public void shootFire(Point playerCentre){

        if(playerCentre.x <= this.position.x && playerCentre.y <= this.position.y){
            ROTATION.setRotation(0);
            fire.drawFromTopLeft(this.position.x - fire.getWidth(), this.position.y - fire.getHeight(), ROTATION);
            firePosition = new Point(this.position.x - fire.getWidth(), this.position.y - fire.getHeight());
        }
        else if(playerCentre.x <= this.position.x && playerCentre.y > this.position.y){
            ROTATION.setRotation(- Math.PI/2);
            fire.drawFromTopLeft(this.position.x  - fire.getWidth(),
                    this.position.y + currentImage.getHeight(), ROTATION);
            firePosition = new Point(this.position.x  - fire.getWidth(),
                    this.position.y + currentImage.getHeight());
        }
        else if(playerCentre.x > position.x && playerCentre.y <= position.y){
            ROTATION.setRotation(Math.PI/2);
            fire.drawFromTopLeft(this.position.x + currentImage.getWidth(),
                    this.position.y - fire.getHeight(), ROTATION);
            firePosition = new Point(this.position.x + currentImage.getWidth(),
                    this.position.y - fire.getHeight());
        }
        else if(playerCentre.x > position.x && playerCentre.y > position.y){
            ROTATION.setRotation(Math.PI);
            fire.drawFromTopLeft(this.position.x + currentImage.getWidth(),
                    this.position.y + currentImage.getHeight(), ROTATION);
            firePosition = new Point(this.position.x + currentImage.getWidth(),
                    this.position.y + currentImage.getHeight());
        }

    }

    /**
     * Method that checks if the demon or Navec is dead
     */
    public boolean isDead() {
        return health.getHealthPoints() <= 0;
    }

    /**
     * Method that checks if a demon or Navec is still invincible
     * @param invincibleFrame that stores the number of frames passed after an Enemy becomes invincible
     * @return true if the Enemy is still invincible and false otherwise
     */
    public boolean inInvincibleDuration(int invincibleFrame){
        if(invincibleFrame / (REFRESH_RATE / ONE_SECOND) < INV_DURATION){
            return true;
        }
        return false;
    }

    /**
     * abstract methods
     */
    public abstract void update(ShadowDimension gameObject);
    public abstract void assignImage();
    public abstract int getFireDamage();
    public abstract void setInvincible();


}
