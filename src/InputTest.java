
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.brackeen.javagamebook.input.*;



/**
    InputManagerTest tests the InputManager with a simple
    run-and-jump mechanism. The player moves and jumps using
    the arrow keys and the space bar.
    <p>Also, InputManagerTest demonstrates pausing a game
    by not updating the game elements if the game is paused.
*/
public class InputTest extends GameCore {

    protected GameAction jump;
    protected GameAction exit;
    protected GameAction moveLeft;
    protected GameAction moveRight;
    protected GameAction pause;
    protected GameAction restart;
    
    protected Megaman megaman;
    protected Link link;
    protected Contra contra;
    protected Ninja ninja;
    
    protected InputManager inputManager;
    protected Player player;
    protected boolean paused;  
    

    public void init() {
        super.init();
        Window window = screen.getFullScreenWindow();
        inputManager = new InputManager(window);

        // use these lines for relative mouse mode
        //inputManager.setRelativeMouseMode(true);
        inputManager.setCursor(InputManager.INVISIBLE_CURSOR);
        loadCreatureSprites();
        createGameActions();
        player = megaman;
        
        paused = false;
       
    }


    /**
        Tests whether the game is paused or not.
    */
    public boolean isPaused() {
        return paused;
    }


    /**
        Sets the paused state.
    */
    public void setPaused(boolean p) {
        if (paused != p) {
            this.paused = p;
            inputManager.resetAllGameActions();
        }
    }


    public void update(long elapsedTime) {
        // check input that can happen whether paused or not
        checkSystemInput();

        if (!isPaused()) {
            // check game input
        	inputManager.setCursor(InputManager.INVISIBLE_CURSOR);
            checkGameInput();

            // update sprite
            player.update(elapsedTime);
        }
    }


    /**
        Checks input from GameActions that can be pressed
        regardless of whether the game is paused or not.
    */
    public void checkSystemInput() {
        if (pause.isPressed()) {
        	inputManager.setCursor(Cursor.getDefaultCursor());
            setPaused(!isPaused());
        }
        if (exit.isPressed()) {
            stop();
        }
    }



    /**
        Checks input from GameActions that can be pressed
        only when the game is not paused.
    */
    public void checkGameInput() {
        float velocityX = 0;
        if (moveLeft.isPressed()) {
            velocityX-=player.getSpeed();
        }
        if (moveRight.isPressed()) {
            velocityX+=player.getSpeed();
        }
        player.setVelocityX(velocityX);

        if (jump.isPressed())
        {
            player.jump();
        }
    }

    
    public void draw(Graphics2D g) {
    	/*
        // draw background
        g.drawImage(bgImage, 80, 0, null);
        g.drawImage(loadImage("assets/images/Background.png"), 0, 0, null);
        g.drawImage(loadImage("assets/images/Background.png"), screen.getWidth() - 80, 0, null);

        // draw sprite
        if (player.getVelocityX() < 0){
        g.drawImage(player.getImage(player.moving()),
                Math.round(player.getX()) + player.getWidth(),
                Math.round(player.getY()),
                -player.getWidth(), player.getHeight(), null);
        }
        else{
        	g.drawImage(player.getImage(player.moving()),
                    Math.round(player.getX()),
                    Math.round(player.getY()),
                    player.getWidth(), player.getHeight(), null);
        }
        
        g.drawImage(scientist.getImage(true), 5, windowPosition, null);
        g.drawImage(scientist.getImage(true), 725, windowPosition2, null);
        
        float height = Math.round((screen.getHeight() - player.getY() - player.getHeight()) / 4);
        g.setColor(Color.WHITE);
        g.drawString("Use Arrows to move.Press up or click to jump.", 103, 200);
        g.drawString("Feet: " + height, 103, 220);
        g.drawString("Use the numbers (1 - 4) to switch characters", 103, 240);
        g.drawString("Press p to pause and see the menu", 103, 260);
        */
    }
    


    /**
        Creates GameActions and maps them to keys.
    */
    public void createGameActions() {
        jump = new GameAction("jump", GameAction.DETECT_INITAL_PRESS_ONLY);
        exit = new GameAction("exit", GameAction.DETECT_INITAL_PRESS_ONLY);
        moveLeft = new GameAction("moveLeft");
        moveRight = new GameAction("moveRight");
        pause = new GameAction("pause", GameAction.DETECT_INITAL_PRESS_ONLY);
        restart = new GameAction("restart", GameAction.DETECT_INITAL_PRESS_ONLY);

        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
        inputManager.mapToKey(pause, KeyEvent.VK_P);
        inputManager.mapToKey(restart, KeyEvent.VK_ENTER);

        // jump with spacebar or mouse button
        inputManager.mapToKey(jump, KeyEvent.VK_UP);
        inputManager.mapToMouse(jump, InputManager.MOUSE_BUTTON_1);

        // move with the arrow keys...
        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);

        // use these lines to map player movement to the mouse
        //inputManager.mapToMouse(moveLeft,
        //  InputManager.MOUSE_MOVE_LEFT);
        //inputManager.mapToMouse(moveRight,
        //  InputManager.MOUSE_MOVE_RIGHT);

    }
    
    private void loadCreatureSprites(){
    	//MEGAMAN -----------------------------------------------------------------------------
        Image megaman1 = loadImage("assets/images/MegamanSprite/Megaman1.png");
    	Image megaman2 = loadImage("assets/images/MegamanSprite/Megaman2.png");
    	Image megaman3 = loadImage("assets/images/MegamanSprite/Megaman3.png");

        // create animation
        Animation anim1 = new Animation();
        anim1.addFrame(megaman1, 150);
        anim1.addFrame(megaman2, 150);
        anim1.addFrame(megaman3, 150);
        anim1.addFrame(megaman2, 150);
        
        megaman = new Megaman(anim1);
        Image dead = loadImage("assets/images/MegamanSprite/Megaman4.png");
        megaman.setImage(loadImage("assets/images/MegamanSprite/Megaman2.png"), dead);
        //--------------------------------------------------------------------------------------
        //CONTRA -------------------------------------------------------------------------------
        Image contra2 = loadImage("assets/images/ContraSprite/Contra2.png");
    	Image contra3 = loadImage("assets/images/ContraSprite/Contra3.png");
    	Image contra4 = loadImage("assets/images/ContraSprite/Contra4.png");
    	Image contra5 = loadImage("assets/images/ContraSprite/Contra5.png");
    	Image contra6 = loadImage("assets/images/ContraSprite/Contra6.png");
        
        Animation anim2 = new Animation();
    	anim2.addFrame(contra6, 75);
    	anim2.addFrame(contra2, 150);
    	anim2.addFrame(contra5, 150);
    	anim2.addFrame(contra4, 150);
    	anim2.addFrame(contra3, 75);
    	
    	contra = new Contra(anim2);
    	dead = loadImage("assets/images/ContraSprite/Contra7.png");
    	contra.setImage(loadImage("assets/images/ContraSprite/Contra1.png"), dead);
        //---------------------------------------------------------------------------------------
    	//NINJA GAIDEN --------------------------------------------------------------------------
    	Image ninja1 = loadImage("assets/images/NinjaSprite/Ninja1.png");
    	Image ninja2 = loadImage("assets/images/NinjaSprite/Ninja2.png");
    	Image ninja3 = loadImage("assets/images/NinjaSprite/Ninja3.png");
    	
    	Animation anim3 = new Animation();
    	anim3.addFrame(ninja1, 200);
    	anim3.addFrame(ninja2, 200);
    	anim3.addFrame(ninja3, 200);
    	anim3.addFrame(ninja2, 200);
    	
    	ninja = new Ninja(anim3);
    	dead = loadImage("assets/images/NinjaSprite/Ninja5.png");
        ninja.setImage(loadImage("assets/images/NinjaSprite/Ninja4.png"), dead);
    	//----------------------------------------------------------------------------------------
    	//LINK -----------------------------------------------------------------------------------
    	Image link1 = loadImage("assets/images/LinkSprite/link1.png");
    	Image link2 = loadImage("assets/images/LinkSprite/link2.png");
    	Image link3 = loadImage("assets/images/LinkSprite/link3.png");
    	Image link4 = loadImage("assets/images/LinkSprite/link4.png");
    	Image link5 = loadImage("assets/images/LinkSprite/link5.png");
    	
    	Animation anim4 = new Animation();
    	anim4.addFrame(link1, 200);
    	anim4.addFrame(link2, 200);
    	anim4.addFrame(link3, 200);
    	anim4.addFrame(link4, 200);
    	anim4.addFrame(link5, 200);
    	
    	link = new Link(anim4);
    	dead = loadImage("assets/images/LinkSprite/Link6.png");
    	link.setImage(loadImage("assets/images/LinkSprite/Link7.png"), dead);
        //----------------------------------------------------------------------------------------
               
        //----------------------------------------------------------------------------------------
    }

}
