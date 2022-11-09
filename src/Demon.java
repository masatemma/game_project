import bagel.*;
import java.util.Random;

/**
 * Demon class that inherits from Enemy class
 * Has attributes and methods that belong to demons
 */
public class Demon extends Enemy{

    private final static String DEMON_LEFT = "res/demon/demonLeft.png";
    private final static String DEMON_RIGHT = "res/demon/demonRight.png";
    private final static String DEMON_INV_LEFT = "res/demon/demonInvincibleLeft.PNG";
    private final static String DEMON_INV_RIGHT = "res/demon/demonInvincibleRight.PNG";
    private final static String DEMON_FIRE = "res/demon/demonFire.png";
    private final static double DEMON_ATTACK_RANGE = 150;
    private final static int DEMON_MAX_HP = 40;
    private final static int DEMON_DAMAGE = 10;
    protected boolean aggressive;
    private Random rand = new Random();

    /**
     * construtor of Demon class
     * a demon is randomly assigned as aggressive or passive
     * @param startX
     * @param startY
     */
    public Demon(int startX, int startY){
        super(startX, startY);
        this.health = new Health(DEMON_MAX_HP);
        this.fire = new Image(DEMON_FIRE);
        this.aggressive = rand.nextBoolean();
        assignImage();
        setNewCentre(this.position.x, this.position.y);
    }

    /**
     * Method that performs the state update of demons
     * @param gameObject it helps the method to call the method in the ShadowDimension class
     */
    public void update(ShadowDimension gameObject){
        if(!isDead()){
            if(aggressive){
                if (direction == UP){
                    setPrevPosition();
                    move(0, -speed);
                } else if (direction == DOWN){
                    setPrevPosition();
                    move(0, speed);
                } else if (direction == LEFT){
                    setPrevPosition();
                    move(-speed,0);
                    if (facingRight && !invincible) {
                        this.currentImage = new Image(DEMON_LEFT);
                        facingRight = !facingRight;
                    }
                    else if(facingRight && invincible){
                        this.currentImage = new Image(DEMON_INV_LEFT);
                        facingRight = !facingRight;
                    }
                } else if (direction == RIGHT){
                    setPrevPosition();
                    move(speed,0);
                    if (!facingRight && !invincible) {
                        this.currentImage = new Image(DEMON_RIGHT);
                        facingRight = !facingRight;
                    }
                    else if(!facingRight && invincible){
                        this.currentImage = new Image(DEMON_INV_RIGHT);
                        facingRight = !facingRight;
                    }
                }
            }

            //If the demon is invincible, calculates the time it's been invincible
            //and acts correspondingly
            if(invincible){
                invincibleFrame += 1;
                if(!inInvincibleDuration(invincibleFrame)){
                    invincible = false;
                    if(facingRight){
                        this.currentImage = new Image(DEMON_RIGHT);

                    }
                    else{
                        this.currentImage = new Image(DEMON_LEFT);
                    }
                }
            }
            getCurrentImage().drawFromTopLeft(position.x, position.y);
            gameObject.checkDemonCollisions(this);
            gameObject.checkDemonOutOfBounds(this);
            health.renderHealthPoints(ENEMY_TYPE, position);
        }
    }

    /**
     * Method that assign the appropriate image of Demon
     */
    public void assignImage(){
        if(facingRight){
            this.currentImage = new Image(DEMON_RIGHT);
        }else{
            this.currentImage = new Image(DEMON_LEFT);
        }
    }

    /**
     * Method that makes Demon invincible when it's attacked by Fae.
     * Starts calculating the invincible duration and sets the new image for the dmeon
     */
    public void setInvincible(){
        this.invincible = true;
        invincibleFrame = 0;
        if(facingRight){
            this.currentImage = new Image(DEMON_INV_RIGHT);
        }
        else{
            this.currentImage = new Image(DEMON_INV_LEFT);
        }
    }
    public int getFireDamage(){return DEMON_DAMAGE;}
    public double getDemonAttackRange(){return DEMON_ATTACK_RANGE;}




}
