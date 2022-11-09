/**
 * Codes are partially from project 1 solution by Tharun Dharmawickrema
 */

import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ShadowDimension extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static int TITLE_FONT_SIZE = 75;
    private final static int TIMESCALE_MAX = 3;
    private final static int TIMESCALE_MIN = -3;
    private final static int TIMESCALE_INCREASE = 1;
    private final static int TIMESCALE_DECREASE = -1;
    private final static int WALL_ARRAY_SIZE = 52;
    private final static int S_HOLE_ARRAY_SIZE = 5;
    private final static int TREE_ARRAY_SIZE = 15;
    private final static int DEMON_ARRAY_SIZE = 5;
    private final static double THREE_SECOND = 3000;
    private final static double ONE_SECOND = 1000;
    private final static double REFRESH_RATE = 60;
    private final static String END_MESSAGE = "GAME OVER!";
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final Font TITLE_FONT = new Font("res/frostbite.ttf", TITLE_FONT_SIZE);
    private final  Level0 LEVEL_0 = new Level0();
    private final  Level1 LEVEL_1 =  new Level1();
    private final static Wall[] walls = new Wall[WALL_ARRAY_SIZE];
    private final static Sinkhole[] sinkholes = new Sinkhole[S_HOLE_ARRAY_SIZE];
    private final static Tree[] trees =  new Tree[TREE_ARRAY_SIZE];
    private final static Demon[] demons = new Demon[DEMON_ARRAY_SIZE];
    private Point topLeft;
    private Point bottomRight;
    private Player player;
    private Navec navec;
    private boolean hasStarted;
    private boolean gameOver;
    private boolean playerWin;
    private int frameNum = 0;
    private int currentLevel = 0;
    private int timescale = 0;

    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        hasStarted = false;
        gameOver = false;
        playerWin = false;
    }

    /**
     * Method used to read file and create objects
     */
    private void readCSV(String filename){
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            int currentWallCount = 0;
            int currentSinkholeCount = 0;
            int currentTreeCount = 0;
            int currentDemonCount = 0;

            while((line = reader.readLine()) != null){
                String[] sections = line.split(",");
                switch (sections[0]) {
                    case "Fae":
                        player = new Player(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                    case "Wall":
                        walls[currentWallCount] = new Wall(Integer.parseInt(sections[1]),Integer.parseInt(sections[2]));
                        currentWallCount++;
                        break;
                    case "Sinkhole":
                        sinkholes[currentSinkholeCount] = new Sinkhole(Integer.parseInt(sections[1]),
                                Integer.parseInt(sections[2]));
                        currentSinkholeCount++;
                        break;
                    case "Tree":
                        trees[currentTreeCount] = new Tree(Integer.parseInt(sections[1]),
                                Integer.parseInt(sections[2]));
                        currentTreeCount++;
                        break;
                    case "Demon":
                        demons[currentDemonCount] =  new Demon(Integer.parseInt(sections[1]),
                                Integer.parseInt(sections[2]));
                        currentDemonCount++;
                    case "Navec":
                        navec = new Navec(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));

                    case "TopLeft":
                        topLeft = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                    case "BottomRight":
                        bottomRight = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;

                }
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }


    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        //Check if level 0 is complete. If complete, readCSV from level 1, if not readCSV from level 0
        if(!hasStarted && currentLevel == 0){
            LEVEL_0.drawStartMessage();
            if (input.wasPressed(Keys.SPACE)){
                hasStarted = true;
                readCSV(LEVEL_0.getFilename());
            }
        }else if(!hasStarted && currentLevel == 1){
            if(!timePassed(frameNum)){
                frameNum++;
                LEVEL_0.drawCompleteMsg();
            }
            else{
                LEVEL_1.drawStartMessage();
                if (input.wasPressed(Keys.SPACE)){
                    hasStarted = true;
                    readCSV(LEVEL_1.getFilename());
                }
            }
        }

        //The game ends when Fae's dead or Fae completes level 1
        if (gameOver){
            drawMessage(END_MESSAGE);
        } else if (playerWin) {
            drawMessage(WIN_MESSAGE);
        }

        //level 0 game is running
        if (hasStarted && currentLevel == 0 && !gameOver && !playerWin){
            LEVEL_0.getBackground().draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

            for(Wall current: walls){
                current.update();
            }
            for(Sinkhole current: sinkholes){
                current.update();
            }
            player.update(input, this);
            if (player.isDead()){
                gameOver = true;
            }
            else if (player.reachedGate()){
                hasStarted = false;
                currentLevel = 1;
            }else if(input.wasPressed(Keys.W)){
                    hasStarted = false;
                    currentLevel = 1;
            }
        }
        //level 1 game is running
        else if (hasStarted && !gameOver && !playerWin && currentLevel == 1) {
            LEVEL_1.getBackground().draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
            for(Tree current: trees){
                current.update();
            }
            for(Sinkhole current: sinkholes){
                current.update();
            }
            for(Demon current: demons){
                current.update(this);
            }
            navec.update(this);
            player.update(input, this);

            if (player.isDead()){
                gameOver = true;
            }
            else if(navec.isDead()){
                playerWin = true;
            }
            if(input.wasPressed(Keys.L)){
                timescaleControl(TIMESCALE_INCREASE);
            }else if(input.wasPressed(Keys.K)){
                timescaleControl(TIMESCALE_DECREASE);
            }
        }
    }

    /**
     *  Method that checks for collisions between Fae and the other entities, and also checks
     *  if Fae is hit by demon's or Navec' fire and performs corresponding actions.
     * @param player an instance from Player class
     */
    public void checkCollisions(Player player){
        Rectangle faeBox = new Rectangle(player.getPosition(), player.getCurrentImage().getWidth(),
                player.getCurrentImage().getHeight());

        if(currentLevel == 0){
            for (Wall current : walls){
                Rectangle wallBox = current.getBoundingBox();
                if (faeBox.intersects(wallBox)){
                    player.moveBack();
                }
            }
        } else if(currentLevel == 1){
            for (Tree current : trees){
                Rectangle wallBox = current.getBoundingBox();
                if (faeBox.intersects(wallBox)){
                    player.moveBack();
                }
            }
            for(Demon demon: demons){
                if(!demon.isDead() && player.getCentre().distanceTo(demon.getCentre()) <= demon.getDemonAttackRange()){
                    demon.shootFire(player.getCentre());
                    Rectangle fireBox = demon.getFireBoundingBox();
                    if(faeBox.intersects(fireBox) && !player.isInvincible()){
                        player.getHealth().setHealthPoints(Math.max(player.getHealth().getHealthPoints() - demon.getFireDamage(), 0));
                        player.setInvincible();
                        System.out.println("Demon inflicts " + demon.getFireDamage() + " damage points on Fae. " +
                                "Fae's current health: " + player.getHealth().getHealthPoints() + "/" + player.getHealth().getMaxHealthPoints());
                    }
                }
            }
            if(!navec.isDead() && player.getCentre().distanceTo(navec.getCentre()) <= navec.getNavecAttackRange()){
                navec.shootFire(player.getCentre());
                Rectangle fireBox = navec.getFireBoundingBox();
                if(faeBox.intersects(fireBox) && !player.isInvincible()){
                    player.getHealth().setHealthPoints(Math.max(player.getHealth().getHealthPoints() - navec.getFireDamage(), 0));
                    player.setInvincible();
                    System.out.println("Navec inflicts " + navec.getFireDamage() + " damage points on Fae. " +
                            "Fae's current health: " + player.getHealth().getHealthPoints() + "/" + player.getHealth().getMaxHealthPoints());
                }

            }
        }

        for (Sinkhole hole : sinkholes){
            Rectangle holeBox = hole.getBoundingBox();
            if (hole.isActive() && faeBox.intersects(holeBox)){
                player.getHealth().setHealthPoints(Math.max(player.getHealth().getHealthPoints() - hole.getDamagePoints(), 0));
                player.moveBack();
                hole.setActive(false);
                System.out.println("Sinkhole inflicts " + hole.getDamagePoints() + " damage points on Fae. " +
                        "Fae's current health: " + player.getHealth().getHealthPoints() + "/" + player.getHealth().getMaxHealthPoints());
            }
        }


    }

    /**
     * Method that checks for collisions between demons and the other entities, and also checks
     * if demon is attacked by Fae and performs corresponding actions.
     * @param demon an instance of Demon class
     */
    public void checkDemonCollisions(Demon demon){
        Rectangle demonBox = new Rectangle(demon.getPosition(), demon.getCurrentImage().getWidth(),
                demon.getCurrentImage().getHeight());
        Rectangle faeBox = new Rectangle(player.getPosition(), player.getCurrentImage().getWidth(),
                player.getCurrentImage().getHeight());


        for (Tree current : trees){
            Rectangle wallBox = current.getBoundingBox();
            if (demonBox.intersects(wallBox)){
                demon.moveBack();
            }
        }

        for (Sinkhole hole : sinkholes){
            Rectangle holeBox = hole.getBoundingBox();
            if (hole.isActive() && demonBox.intersects(holeBox)){
                demon.moveBack();
            }
        }

        if(demonBox.intersects(faeBox) && player.atAttackState() && !demon.isInvincible()){
            demon.getHealth().setHealthPoints(Math.max(demon.getHealth().getHealthPoints() - player.getAttackDamage(), 0));
            demon.setInvincible();
            System.out.println("Fae inflicts " + player.getAttackDamage() + " damage points on Demon. " +
                    "Demon's current health: " + demon.getHealth().getHealthPoints() + "/" + demon.getHealth().getMaxHealthPoints());
        }
    }

    /**
     * Method that checks for collisions between Navec and the other entities, and also checks if
     * Navec is attacked by Fae and performs corresponding actions.
     * @param navec an instance of Navec class
     */
    public void checkNavecCollisions(Navec navec){
        Rectangle navecBox = new Rectangle(navec.getPosition(), navec.getCurrentImage().getWidth(),
                navec.getCurrentImage().getHeight());
        Rectangle faeBox = new Rectangle(player.getPosition(), player.getCurrentImage().getWidth(),
                player.getCurrentImage().getHeight());

        for (Tree current : trees){
            Rectangle wallBox = current.getBoundingBox();
            if (navecBox.intersects(wallBox)){
                navec.moveBack();
            }
        }

        for (Sinkhole hole : sinkholes){
            Rectangle holeBox = hole.getBoundingBox();
            if (hole.isActive() && navecBox.intersects(holeBox)){
                navec.moveBack();
            }
        }

        if(navecBox.intersects(faeBox) && player.atAttackState() && !navec.isInvincible()){
            navec.getHealth().setHealthPoints(Math.max(navec.getHealth().getHealthPoints() - player.getAttackDamage(), 0));
            navec.setInvincible();
            System.out.println("Fae inflicts " + player.getAttackDamage() + " damage points on Navec. " +
                    "Navec's current health: " + navec.getHealth().getHealthPoints() + "/" + navec.getHealth().getMaxHealthPoints());
        }
    }
    /**
     * Method that checks if Fae has gone out-of-bounds and performs corresponding action
     * @param player an instance of Player class
     */
    public void checkPlayerOutOfBounds(Player player){
        Point currentPosition = player.getPosition();
        if ((currentPosition.y > bottomRight.y) || (currentPosition.y < topLeft.y) || (currentPosition.x < topLeft.x)
                || (currentPosition.x > bottomRight.x)){
            player.moveBack();
        }
    }

    /**
     * Method that checks if a demon has gone out-of-bounds and performs corresponding action
     * @param demon an instance of Demon class
     */
    public void checkDemonOutOfBounds(Demon demon){
        Point currentPosition = demon.getPosition();
        if ((currentPosition.y > bottomRight.y) || (currentPosition.y < topLeft.y) || (currentPosition.x < topLeft.x)
                || (currentPosition.x > bottomRight.x)){
            demon.moveBack();
        }
    }

    /**
     * Method that checks if Navec has gone out-of-bounds and performs corresponding action
     * @param navec an instance of Navec class
     */
    public void checkNavecOutOfBounds(Navec navec){
        Point currentPosition = navec.getPosition();
        if ((currentPosition.y > bottomRight.y) || (currentPosition.y < topLeft.y) || (currentPosition.x < topLeft.x)
                || (currentPosition.x > bottomRight.x)){
            navec.moveBack();
        }
    }

    /**
     * Method used to draw end screen messages
     */
    private void drawMessage(String message){
        TITLE_FONT.drawString(message, (Window.getWidth()/2.0 - (TITLE_FONT.getWidth(message)/2.0)),
                (Window.getHeight()/2.0 + (TITLE_FONT_SIZE/2.0)));
    }

    /**
     * Method used to calculate the time the "LEVEL COMPLETE!" is being displayed
     * @param frameNum a variable that stores the number of frames after the message is displayed
     * @return true if 3 seconds have passed and false if otherwise
     */
    private boolean timePassed(int frameNum) {
        if (frameNum / (REFRESH_RATE / ONE_SECOND) < THREE_SECOND){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method that controls the timescale and changes the speed of demons and Navec
     * Prints out corresponding messages into the console
     * @param n an integer that is either 1 or -1 to increase or decrease the timescale
     */
    private void timescaleControl(int n){
        if(n + timescale >= TIMESCALE_MIN && n + timescale <= TIMESCALE_MAX ){
            timescale += n;
            for(Demon demon: demons){
                demon.setNewSpeed(demon.getOriginalSpeed() + (demon.getOriginalSpeed()/2) * timescale);
            }
            navec.setNewSpeed(navec.getOriginalSpeed() + (navec.getOriginalSpeed()/2) * timescale);
            if(n > 0){
                System.out.print("Sped up, Speed: " + timescale + "\n");
            }
            else{
                System.out.print("Slowed down, Speed: " + timescale + "\n");
            }
        }
    }
}