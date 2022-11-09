/**
 * Codes are partially from project 1 solution by Tharun Dharmawickrema
 */

import bagel.*;
import bagel.util.Point;

/**
 * Player class that has attributes and methods that belong to Fae
 */
public class Player{
    private final static String FAE_LEFT = "res/fae/faeLeft.png";
    private final static String FAE_RIGHT = "res/fae/faeRight.png";
    private final static String FAE_ATTACK_LEFT = "res/fae/faeAttackLeft.png";
    private final static String FAE_ATTACK_RIGHT = "res/fae/faeAttackRight.png";
    private final static String PLAYER_TYPE = "Player";
    private final static double MOVE_SIZE = 2;
    private final static double ONE_SECOND = 1000;
    private final static double REFRESH_RATE = 60;
    private final static int WIN_X = 950;
    private final static int WIN_Y = 670;
    private final static int INV_DURATION = 3000;
    private final static int ATTACK_DURATION = 1000;
    private final static int ATTACK_COOLDOWN = 2000;
    private final static int ATTACK_DAMAGE = 20;
    private final static int PLAYER_MAX_HP = 100;
    private Point position;
    private Point prevPosition;
    private Point centre;
    private Image currentImage;
    private boolean facingRight;
    private boolean attackState;
    private boolean invincible;
    private int invincibleFrame;
    private int attackFrameNum;
    private int attackCoolDownFrame;
    private Health health;

    public Player(int startX, int startY){
        this.position = new Point(startX, startY);
        facingRight = true;
        attackState = false;
        invincible = false;
        this.health = new Health(PLAYER_MAX_HP);
        this.currentImage = new Image(FAE_RIGHT);
        this.attackFrameNum = 0;
        this.attackCoolDownFrame = 0;
        setNewCentre(position.x, position.y);
    }

    /**
     * Method that performs state update
     */
    public void update(Input input, ShadowDimension gameObject){
        if (input.isDown(Keys.UP)){
            setPrevPosition();
            move(0, -MOVE_SIZE);
        } else if (input.isDown(Keys.DOWN)){
            setPrevPosition();
            move(0, MOVE_SIZE);
        } else if (input.isDown(Keys.LEFT)){
            setPrevPosition();
            move(-MOVE_SIZE,0);
            if (facingRight && !attackState) {
                this.currentImage = new Image(FAE_LEFT);
                facingRight = !facingRight;
            }
            else if(facingRight && attackState){
                this.currentImage = new Image(FAE_ATTACK_LEFT);
                facingRight = !facingRight;
            }
        } else if (input.isDown(Keys.RIGHT)){
            setPrevPosition();
            move(MOVE_SIZE,0);
            if (!facingRight && !attackState) {
                this.currentImage = new Image(FAE_RIGHT);
                facingRight = !facingRight;
            }
            else if(!facingRight && attackState){
                this.currentImage = new Image(FAE_ATTACK_RIGHT);
                facingRight = !facingRight;
            }
        }
        if (input.wasPressed(Keys.A) && !isOnCooldown(attackCoolDownFrame)){
            if(!attackState){
                attackState = true;
                attackFrameNum = 0;
            }
            if(facingRight){
                this.currentImage = new Image(FAE_ATTACK_RIGHT);
            }else{
                this.currentImage = new Image(FAE_ATTACK_LEFT);
            }
        }
        /**
         * if on attack state, calculate the duration the player has been in the attack state
         */
        if(attackState){
            attackFrameNum += 1;
            if(!inAttackDuration(attackFrameNum)){
                attackState = false;
                if(facingRight){
                    this.currentImage = new Image(FAE_RIGHT);

                }
                else{
                    this.currentImage = new Image(FAE_LEFT);
                }
                attackCoolDownFrame = 0;
            }
        }
        /**
         * If Fae is invincible, calculates the invincible duration
         */
        if(invincible){
            invincibleFrame += 1;
            if(!inInvincibleDuration(invincibleFrame)){
                invincible = false;
            }
        }

        attackCoolDownFrame += 1;
        getCurrentImage().drawFromTopLeft(position.x, position.y);
        gameObject.checkCollisions(this);
        health.renderHealthPoints(PLAYER_TYPE, position);
        gameObject.checkPlayerOutOfBounds(this);
    }

    public Point getPosition(){
        return position;
    }
    public Image getCurrentImage(){return currentImage;}
    public Point getCentre(){return centre;}
    public Health getHealth(){return health;}
    public int getAttackDamage(){return ATTACK_DAMAGE;}
    public boolean isInvincible(){return invincible;}
    public boolean atAttackState(){return attackState;}

    /**
     * Method that makes Fae invincible and starts calculating the invincible duration
     */
    public void setInvincible(){
        this.invincible = true;
        invincibleFrame = 0;
    }

    public boolean inInvincibleDuration(int invincibleFrame){
        if(invincibleFrame / (REFRESH_RATE / ONE_SECOND) < INV_DURATION){
            return true;
        }
        return false;
    }
    /**
     * Method that moves the Player back to previous position
     */
    public void moveBack(){
        position = prevPosition;
    }
    /**
     * Method that stores the character's previous position
     */
    private void setPrevPosition(){
        prevPosition = new Point(position.x, position.y);
    }
    /**
     * Method that stores the character's new position
     */
    private void setNewPosition(double newX, double newY){
        this.position = new Point(newX, newY);
    }

    /**
     * Method that updates the centre of Fae
     */
    private void setNewCentre(double newX, double newY){
        this.centre =  new Point(newX + (this.currentImage.getWidth() / 2),
                newY + (this.currentImage.getHeight() / 2));
    }

    /**
     * Method that updates Fae's positions and centres
     */
    private void move(double xMove, double yMove){
        double newX = position.x + xMove;
        double newY = position.y + yMove;
        setNewPosition(newX, newY);
        setNewCentre(newX, newY);
    }

    /**
     * Method that checks if the character's health has depleted
     */
    public boolean isDead() {
        return health.getHealthPoints() <= 0;
    }

    /**
     * Method that checks if Fae has found the gate
     */
    public boolean reachedGate(){
        return (position.x >= WIN_X) && (position.y >= WIN_Y);
    }

    /**
     * Method that checks if the attack is still on cooldown of 3 seconds
     */
    public boolean isOnCooldown(int attackCoolDownFrame){
        if(attackCoolDownFrame / (REFRESH_RATE / ONE_SECOND) < ATTACK_COOLDOWN){
            return true;
        }
        return false;
    }

    /**
     * Method that checks if Fae is still in the attack state
     */
    public boolean inAttackDuration(int attackFrameNum){
        if(attackFrameNum/ (REFRESH_RATE / ONE_SECOND) < ATTACK_DURATION){
            return true;
        }
        return false;
    }

}