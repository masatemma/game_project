import bagel.Image;

/**
 * Navec class that inherits from Enemy class
 * Has attributes and methods that belong to Navec
 */
public class Navec extends Enemy {
    private final static String NAVEC_LEFT = "res/navec/navecLeft.png";
    private final static String NAVEC_RIGHT = "res/navec/navecRight.png";
    private final static String NAVEC_INV_RIGHT = "res/navec/navecInvincibleRight.png";
    private final static String NAVEC_INV_LEFT = "res/navec/navecInvincibleLeft.png";
    private final static String NAVEC_FIRE = "res/navec/navecFire.png";
    private final static double NAVEC_ATTACK_RANGE = 200;
    private final static int NAVEC_MAX_HP = 80;
    private final static int NAVEC_DAMAGE = 20;

    public Navec(int startX, int startY){
        super(startX, startY);
        this.health = new Health(NAVEC_MAX_HP);
        assignImage();
        setNewCentre(this.position.x, this.position.y);
        this.fire = new Image(NAVEC_FIRE);
    }

    /**
     * Method that performs the state update of Navec
     * @param gameObject that allows this method to call methods from ShadowDimension class
     */
    public void update(ShadowDimension gameObject){

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
                this.currentImage = new Image(NAVEC_LEFT);
                facingRight = !facingRight;
            }
            else if(facingRight && invincible){
                this.currentImage = new Image(NAVEC_INV_LEFT);
                facingRight = !facingRight;
            }
        } else if (direction == RIGHT){
            setPrevPosition();
            move(speed,0);
            if (!facingRight && !invincible) {
                this.currentImage = new Image(NAVEC_RIGHT);
                facingRight = !facingRight;
            }
            else if(!facingRight && invincible){
                this.currentImage = new Image(NAVEC_INV_RIGHT);
                facingRight = !facingRight;
            }
        }

        //If the demon is invincible, calculates the time it's been invincible
        //and acts correspondingly
        if(invincible){
            invincibleFrame += 1;
            if(!inInvincibleDuration(invincibleFrame)){
                invincible = false;
                if(facingRight){
                    this.currentImage = new Image(NAVEC_RIGHT);

                }
                else{
                    this.currentImage = new Image(NAVEC_LEFT);
                }
            }
        }
        getCurrentImage().drawFromTopLeft(position.x, position.y);
        gameObject.checkNavecCollisions(this);
        gameObject.checkNavecOutOfBounds(this);
        health.renderHealthPoints(ENEMY_TYPE, position);

    }
    /**
     * Method that assign the appropriate image of Demon
     */
    public void assignImage(){
        if(facingRight){
            this.currentImage = new Image(NAVEC_RIGHT);
        }else{
            this.currentImage = new Image(NAVEC_LEFT);
        }
    }
    public double getNavecAttackRange(){ return NAVEC_ATTACK_RANGE;}
    public int getFireDamage(){return NAVEC_DAMAGE;}

    /**
     * Method that makes Navec invincible when it's attacked by Fae.
     * Starts calculating the invincible duration and sets the appropriate image for Navec
     */
    public void setInvincible(){
        this.invincible = true;
        invincibleFrame = 0;
        if(facingRight){
            this.currentImage = new Image(NAVEC_INV_RIGHT);
        }
        else{
            this.currentImage = new Image(NAVEC_INV_LEFT);
        }
    }
}

