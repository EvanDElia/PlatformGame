

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.swing.ImageIcon;


/**
    The TileMapRenderer class draws a TileMap on the screen.
    It draws all tiles, sprites, and an optional background image
    centered around the position of the player.

    <p>If the width of background image is smaller the width of
    the tile map, the background image will appear to move
    slowly, creating a parallax background effect.

    <p>Also, three static methods are provided to convert pixels
    to tile positions, and vice-versa.

    <p>This TileMapRender uses a tile size of 64.
*/
public class TileMapRenderer {

    private static final int TILE_SIZE = 64;
    // the size in bits of the tile
    // Math.pow(2, TILE_SIZE_BITS) == TILE_SIZE
    private static final int TILE_SIZE_BITS = 6;
    private int score;
    public static long originalTime = System.currentTimeMillis() + 3500;
    private long additionalOffset = 0;
    
    private int index = 0;
    private int roundScore;
    private int time;
    
    private AudioClip beepNoise;
    
    private Image background;

    /**
        Converts a pixel position to a tile position.
    */
    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }


    /**
        Converts a pixel position to a tile position.
    */
    public static int pixelsToTiles(int pixels) {
        // use shifting to get correct values for negative pixels
        return pixels >> TILE_SIZE_BITS;

        // or, for tile sizes that aren't a power of two,
        // use the floor function:
        //return (int)Math.floor((float)pixels / TILE_SIZE);
    }


    /**
        Converts a tile position to a pixel position.
    */
    public static int tilesToPixels(int numTiles) {
        // no real reason to use shifting here.
        // it's slighty faster, but doesn't add up to much
        // on modern processors.
        return numTiles << TILE_SIZE_BITS;

        // use this if the tile size isn't a power of 2:
        //return numTiles * TILE_SIZE;
    }


    /**
        Sets the background to draw.
    */
    public void setBackground(Image background) {
        this.background = background;
        try {
			beepNoise = Applet.newAudioClip(new URL("file:/Users/Evan/workspace/csc329Game/assets/sounds/Beep.wav"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    }


    /**
        Draws the specified TileMap.
    */
    public void draw(Graphics2D g, TileMap map,
        int screenWidth, int screenHeight, boolean paused)
    {
    	
    	if(GameManager2.credits || GameManager2.instructions){ //Must change GameManager2 to GameManager2Old if you wish to use the wav version
    		if (GameManager2.credits){
    			drawCredits(g, screenWidth, screenHeight);
    		}
    		else
    			drawInstructions(g, screenWidth, screenHeight);
    	}
    	else{
        Player player = (Player) map.getPlayer();
        int mapWidth = tilesToPixels(map.getWidth());
        int mapHeight = tilesToPixels(map.getHeight());

        // get the scrolling position of the map
        // based on player's position
        int offsetX = screenWidth / 2 -
            Math.round(player.getX()) - TILE_SIZE;
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, screenWidth - mapWidth);

        // get the y offset to draw all sprites and tiles
        // also based on players position
        int offsetY = screenHeight / 2 -
                Math.round(map.getHeight() * 64) - TILE_SIZE;
        offsetY = Math.min(offsetY, 0);
        offsetY = Math.max(offsetY, screenHeight - mapHeight);
        
        if (System.currentTimeMillis() - originalTime > 0 && !paused && ResourceManager.currentMap == 1){
        	if (-player.getY() - 12 > offsetY + additionalOffset) additionalOffset += 12;
        	additionalOffset += 2;
        	
        	if (player.getState() == Mob.STATE_DEAD){
        		additionalOffset -= 4;   		
        	}
        }
       
    	offsetY += additionalOffset;
        /*
        if (System.currentTimeMillis() - originalTime > 0)
        offsetY += (int) (System.currentTimeMillis() - originalTime) / 13;
        */

        // draw black background, if needed
        if (background == null ||
            screenHeight > background.getHeight(null))
        {
            g.setColor(Color.black);
            g.fillRect(0, 0, screenWidth, screenHeight);
        }

        // draw parallax background image
        if (background != null) {
            int x = offsetX *
                (screenWidth - background.getWidth(null)) /
                (screenWidth - mapWidth);
            int y = offsetY *
                    (screenHeight - background.getHeight(null));

            g.drawImage(background, x, y, null);
        }
        
        if (ResourceManager.currentMap == 0){
        	g.drawString("Start Game", tilesToPixels(13) + offsetX, tilesToPixels(8) + offsetY);
        	g.drawString("Credits", tilesToPixels(5) + offsetX, tilesToPixels(8) + offsetY);
        	g.drawString("Music", tilesToPixels(9) + offsetX, tilesToPixels(8) + offsetY);
        	g.drawString("Instructions", tilesToPixels(17) + offsetX, tilesToPixels(8) + offsetY);
        	g.drawString("Character", tilesToPixels(21) + offsetX, tilesToPixels(8) + offsetY);
        	
        	g.setFont(new Font("Times New Roman", Font.PLAIN, 38));
        	int red = (int) (Math.sin(.3 * index + 0) * 127 + 128);
        	int green = (int) (Math.sin(.3 * index + 2) * 127 + 128);
        	int blue = (int) (Math.sin(.3 * index + 4) * 127 + 128);
        	index++;
        	if (index == 32) index = 0;
        	g.setColor(new Color(red, green, blue));
        	g.drawString("Main Menu", tilesToPixels(11) + offsetX, tilesToPixels(5) + offsetY);
        	g.setFont(new Font("Century", Font.PLAIN, 42));
        	g.drawString("Platform Your Face Off", tilesToPixels(9) + offsetX, tilesToPixels(4) + offsetY);
        }
        
        if (player.getState() == Mob.STATE_DEAD){
        	if (offsetY < screenHeight - mapHeight){
        		g.drawImage(loadImage("assets/images/Hell.png"), 0, -(screenHeight - mapHeight - 704) + offsetY, null);
        		g.drawImage(loadImage("assets/images/Hell.png"), 384, -(screenHeight - mapHeight - 704) + offsetY, null);
        	}
        	g.drawImage(loadImage("assets/images/GameOver.png"), 250, 90, null);
        	int red = (int) (Math.sin(.3 * index + 0) * 127 + 128);
        	int green = (int) (Math.sin(.3 * index + 2) * 127 + 128);
        	int blue = (int) (Math.sin(.3 * index + 4) * 127 + 128);
        	index++;
        	if (index == 32) index = 0;
        	g.setColor(new Color(red, green, blue));
        	g.drawString("You lasted " + time + " seconds and scored " + roundScore, 200, 460);
        	g.drawString("Your best so far is " + score + " points!", 200, 490);
        }

        // draw the visible tiles
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX +
            pixelsToTiles(screenWidth) + 1;
        for (int y=0; y<map.getHeight(); y++) {
            for (int x=firstTileX; x <= lastTileX; x++) {
                Image image = map.getTile(x, y);
                if (image != null) {
                    g.drawImage(image,
                        tilesToPixels(x) + offsetX,
                        tilesToPixels(y) + offsetY,
                        null);
                	if(player.getY() + 64 < 0){
                		int red = (int) (Math.sin(.3 * index + 0) * 127 + 128);
                    	int green = (int) (Math.sin(.3 * index + 2) * 127 + 128);
                    	int blue = (int) (Math.sin(.3 * index + 4) * 127 + 128);
                    	index++;
                    	if (index == 32) index = 0;
                    	g.setColor(new Color(red, green, blue));
                        g.setFont(new Font("Century", Font.PLAIN, 30));
                		g.drawString("YOU REACHED THE TOP!!", 250, 50);
                		g.drawString("THERE IS NO PRIZE!!", 270, 100);
                		g.drawString("YOUR PRINCESS IS IN ANOTHER CASTLE", 200, 150);
                	}
                }
            }
        }
        
        // draw player
        if (player.getVelocityX() >= 0)
        g.drawImage(player.getImage(player.moving()),
            Math.round(player.getX()) + offsetX,
            Math.round(player.getY()) + offsetY,
            player.getWidth(), player.getHeight(),
            null);
        else{
        	g.drawImage(player.getImage(player.moving()),
                    Math.round(player.getX()) + offsetX + player.getWidth(),
                    Math.round(player.getY()) + offsetY,
                    -player.getWidth(), player.getHeight(),
                    null);
        }
        
        //kill the player
        if (-player.getY() + 15 < offsetY - screenHeight && player.getState() != Mob.STATE_DEAD && ResourceManager.currentMap == 1){
        	player.setState(Mob.STATE_DEAD);
        	time = (int) ((System.currentTimeMillis() - originalTime) / 1000);
        }
        
        
        // draw sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            int x = Math.round(sprite.getX()) + offsetX;
            int y = Math.round(sprite.getY()) + offsetY;
            g.drawImage(sprite.getImage(true), x, y, null);

        }
        
        //draw the timer in the beginning
        if (ResourceManager.currentMap == 1){
        long timeDiff = System.currentTimeMillis() - originalTime;
                
    	if (timeDiff < -2000)
    		g.drawImage(loadImage("assets/images/menu/three.png"), 230, 100, null);
    	else if (timeDiff < -1000)
    		g.drawImage(loadImage("assets/images/menu/two.png"), 230, 100, null);
    	else if(timeDiff < 0)
    		g.drawImage(loadImage("assets/images/menu/one.png"), 230, 100, null);
    	if (timeDiff < -20 && (timeDiff % 1000) >= -27 ){
    		beepNoise.play();
    	}
        }

        //draw the pause menu
        if (paused){
        	g.drawImage(loadImage("assets/images/menu/pause.png"), 300, 300, null);
        }
        //draw the HUD
        int temp = (int) ((tilesToPixels(map.getHeight()) - player.getY()) / player.getHeight());
        if (temp > roundScore) roundScore = temp;
        if (temp > score) score = temp;
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.setColor(Color.WHITE);
        g.drawString("Current Score: " + temp, 100, 60);
        g.drawString("Best Score: " + score, 100, 80);
        g.drawString("Double Jumps: " + player.getJumps(), 100, 100);
        
        g.drawImage(loadImage("assets/images/Background3.png"), 0, 0, null);
		g.drawImage(loadImage("assets/images/Background3.png"), 800 - 80, 0, null);
    }
    }
    
    private void drawInstructions(Graphics g, int screenWidth, int screenHeight) {
    	g.setColor(Color.black);
    	g.fillRect(0, 0, screenWidth, screenHeight);
    	g.setColor(Color.yellow);
    	g.drawString("Instructions", 250, 50);
    	g.drawString("You begin as Megaman", 100, 100);
    	g.drawString("...But you may change your character", 160, 130);
    	g.drawString("By using the power up on the far right", 166, 160); 
    	g.drawString("The goal of the game is to jump as high up the tower as you can", 20, 190);
    	g.drawString("Some platforms are too high to jump to", 50, 220);
    	g.drawString("In order to reach them you must collect doubleJump power ups", 20, 250);
    	
    	g.drawString("Move using the arrow keys. Press Escape to exit the game", 20, 280);
    	g.drawString("Jump using the up arrow or left-click", 50, 310);
    	
    	g.drawString("The heart power up allows you to see the credits", 40, 340);
    	g.drawString("And the music power up allows you to change the music", 20, 370);
    	g.drawString("The music changes from edm to metal.", 50, 400);
    	g.drawString("Collect the star power up when you're ready to play!", 30, 430);
    	g.drawString("Press escape to go to main menu then again to exit completely.", 20, 460);
    	
    	g.drawString("Press escape now to return to the main menu and good luck!", 40, 490);
	}


	private void drawCredits(Graphics g, int screenWidth, int screenHeight){
    	g.setColor(Color.black);
    	g.fillRect(0, 0, screenWidth, screenHeight);
    	g.setColor(Color.white);
    	g.drawString("Credits", 250, 50);
    	g.drawString("Thank you for playing and (hopefully) enjoying this game!", 80, 100);
    	g.drawString("This game was created by Evan D'Elia", 20, 130);
    	g.drawString("Throughout the course of CSC329 at the University of Miami", 20, 160);
    	g.drawString("And with the help of Developing Games in Java by David Brackeen", 20, 190);
    	g.drawString("The game contains 23 different classes and over 3,000 lines of code", 20, 220);
    	g.drawString("All sprites and tiles have been taken from the public domain", 20, 250);
    	g.drawString("Or were created by myself using the free Paint.NET software", 20, 280);
    	g.drawString("In no way is this game meant to copy or steal, it is merely", 20, 310);
    	g.drawString("A learning experience and a form of tribute toward other games", 20, 340);
    	g.drawString("Music is taken from Anamanaguchi, Buskerdroid, MisfitChris,", 20, 370);
    	g.drawString("Animals as Leaders, and The Algorithm.", 20, 400);
    	g.drawString("If you find any bugs you can report them at e.delia@umiami.edu", 20, 430);
    	g.drawString("Press escape when you are ready to return to the interactive main menu", 20, 490);
    }
    
    public Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }
    
    public void reloadMap(){
    	originalTime= System.currentTimeMillis() + 3500;
    	additionalOffset = 0;
    	roundScore = 1;
    }

}
