import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import com.brackeen.javagamebook.input.InputManager;

public class GameManager2Old extends InputTest {
	
	public static void main(String args[]){
		new GameManager2Old().run();
	}
	
	private Playlist playlist;
    
    private Point pointCache = new Point();
    private TileMap map;
    private TileMapRenderer renderer;
    private ResourceManager manager;
    
    private Mob[] chars = new Mob[4];
    
    private boolean musicMode = false;
    
    public static boolean credits = false;
    public static boolean instructions = false;
	
	public void init(){
		super.init();
		chars[0] = megaman;
		chars[1] = link;
		chars[2] = contra;
		chars[3] = ninja;
		
		playlist = new Playlist();
		playlist.start();

		manager = new ResourceManager(screen.getFullScreenWindow().getGraphicsConfiguration(), player);
		
		renderer = new TileMapRenderer();
		renderer.setBackground(manager.loadImage("assets/images/background.png"));
		
		try {
			generateMap();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
				
		map = manager.loadNextMap();
	}
	
	public void draw(Graphics2D g) {
		renderer.draw(g, map, screen.getWidth(), screen.getHeight(), paused);
    }
	
	public Point getTileCollision(Sprite sprite,
	        float newX, float newY)
	    {
		
	        float fromY = Math.min(sprite.getY(), newY);
	        float toY = Math.max(sprite.getY(), newY);
	        /*
	        // get the tile locations
	        int fromTileX = TileMapRenderer.pixelsToTiles(fromX);
	        int fromTileY = TileMapRenderer.pixelsToTiles(fromY);
	        int toTileX = TileMapRenderer.pixelsToTiles(
	            toX + sprite.getWidth() - 1);
	        int toTileY = TileMapRenderer.pixelsToTiles(
	            toY + sprite.getHeight() - 1);
	        
	        if (newY < sprite.getY()) toTileY = TileMapRenderer.pixelsToTiles(toY);
	        
	        System.out.println(fromTileX + ": " + toTileX + " : " + fromTileY + ": " + toTileY);
	        
	        // check each tile for a collision
	        for (int x=fromTileX; x<=toTileX; x++) {
	            for (int y=fromTileY; y<=toTileY; y++) {
	                if (x < 1 || x >= map.getWidth() - 1 || y > map.getHeight() - 1 || (fromY - toY < 0 && map.getTile(x, y) != null))
	                {
	                    // collision found, return the tile
	                	System.out.println(x + " : " + y);
	                    pointCache.setLocation(x, y);
	                    return pointCache;
	                }
	            }
	        }
	        */
	     
		int x = TileMapRenderer.pixelsToTiles(newX > sprite.getX() ? newX : newX + 26);
		int y = TileMapRenderer.pixelsToTiles(newY + player.getHeight() - 1);
		
		if (newX < 66 || newX > TileMapRenderer.tilesToPixels(map.getWidth()) - 130 || y > map.getHeight() - 1 || (fromY - toY < 0 && map.getTile(x, y) != null)){
			pointCache.setLocation(x,y);
			return pointCache;
		}
		
	        // no collision found
	        return null;
	    }
	
	public boolean isCollision(Sprite s1, Sprite s2) {
        // if the Sprites are the same, return false
        if (s1 == s2) {
            return false;
        }

        // get the pixel location of the Sprites
        int s1x = Math.round(s1.getX());
        int s1y = Math.round(s1.getY());
        int s2x = Math.round(s2.getX());
        int s2y = Math.round(s2.getY());

        // check if the two sprites' boundaries intersect
        return (s1x < s2x + s2.getWidth() &&
            s2x < s1x + s1.getWidth() &&
            s1y < s2y + s2.getHeight() &&
            s2y < s1y + s1.getHeight());
    }
	
    /**
    Gets the Sprite that collides with the specified Sprite,
    or null if no Sprite collides with the specified Sprite.
*/
public Sprite getSpriteCollision(Sprite sprite) {

    // run through the list of Sprites
    Iterator i = map.getSprites();
    while (i.hasNext()) {
        Sprite otherSprite = (Sprite)i.next();
        if (isCollision(sprite, otherSprite)) {
            // collision found, return the Sprite
            return otherSprite;
        }
    }

    // no collision found
    return null;
}


	public void checkSystemInput() {
		if (pause.isPressed()) {
			setPaused(!isPaused());
			if (isPaused()){
				playlist.stopPlaying();
				inputManager.setCursor(Cursor.getDefaultCursor());
			}
			else{
				playlist.continuePlaying();
				inputManager.setCursor(InputManager.INVISIBLE_CURSOR);
			}
		}
		if (exit.isPressed()) {
			if (credits || instructions){
				credits = false;
				instructions = false;
			}
			else if(ResourceManager.currentMap == 1){
				ResourceManager.currentMap = -1;
				player.reset();
				renderer.reloadMap();
				map = manager.loadNextMap();
				player.setState(Mob.STATE_NORMAL);
			}
			else{
				playlist.stopPlaying();
				playlist.killIt();
				stop();
			}
		}
		if (restart.isPressed() && player.getState() == Mob.STATE_DEAD){
			try {
				generateMap();
			} catch (Exception e) {
				e.printStackTrace();
			}
			player.reset();
			renderer.reloadMap();
			map = manager.loadNextMap();
			
		}
	}
	
    public void checkGameInput() {
        float velocityX = 0;
        if (ResourceManager.currentMap == 0 || TileMapRenderer.originalTime - System.currentTimeMillis() < 0){
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
        if (player.getState() == Mob.STATE_DEAD){
			player.setY(TileMapRenderer.tilesToPixels(map.getHeight() - 2));
        }
    }

/**
    Updates Animation, position, and velocity of all Sprites
    in the current map.
*/
public void update(long elapsedTime) {
    // get keyboard/mouse input
    checkSystemInput();
    checkGameInput();
    
    //System.out.println(elapsedTime);
    
    if (!paused && player.getState() != Mob.STATE_DEAD && elapsedTime < 33){
    	// update player
    	updateCreature(player, elapsedTime);
    	player.update(elapsedTime);
    
    	// update other sprites
    	Iterator i = map.getSprites();
    	while (i.hasNext()) {
        	Sprite sprite = (Sprite)i.next();
        	// normal update
        	sprite.update(elapsedTime);
    	}
    }
}

private void updateCreature(Mob creature,
        long elapsedTime)
    {

        // apply gravity
		creature.setVelocityY(creature.getVelocityY() + .002f * elapsedTime);

        //get x and y
        float dx = creature.getVelocityX();
        float oldX = creature.getX();
        float newX = oldX + dx * elapsedTime;
        float dy = creature.getVelocityY();
        float oldY = creature.getY();
        float newY = oldY + dy * elapsedTime;
        
        
        // change x
        Point tile =
            getTileCollision(creature, newX, creature.getY());
        if (tile == null ) {
            creature.setX(newX);
        }
        else {
            // line up with the tile boundary
        	if (TileMapRenderer.tilesToPixels(tile.x) < 66) creature.setX(67);
        	else if (TileMapRenderer.tilesToPixels(tile.x) > TileMapRenderer.tilesToPixels(map.getWidth()) - 139)
        		creature.setX(TileMapRenderer.tilesToPixels(map.getWidth()) - 138);	
        	else if (dx > 0)creature.setX(creature.getX());
            else if (dx < 0) creature.setX(creature.getX());
            creature.collideHorizontal();
        }
        if (creature instanceof Player) {
            checkPlayerCollision((Player)creature);
        }

        // change y
        tile = getTileCollision(creature, creature.getX(), newY);
        if (tile == null) {
        	if (newY < TileMapRenderer.tilesToPixels(map.getHeight()) && newY < player.getY() + 128)
        		creature.setY(newY);
        }
        else if (dy < 0){
        	//do nothing
        	//lets player pass through tiles from under them
        }
        else {
            // line up with the tile boundary
            if (dy > 0) {
                creature.setY(
                    TileMapRenderer.tilesToPixels(tile.y) -
                    creature.getHeight());
            }
            creature.collideVertical();
        }

    }

public void checkPlayerCollision(Player player)
    {
        // check for player collision with other sprites
        Sprite collisionSprite = getSpriteCollision(player);
        if (collisionSprite instanceof PowerUp) {
            acquirePowerUp((PowerUp)collisionSprite);
        }
    }


public void acquirePowerUp(PowerUp powerUp) {
    // remove it from the map
    map.removeSprite(powerUp);
    if (!(powerUp instanceof PowerUp.Jump)){
    	player.setY(TileMapRenderer.tilesToPixels(map.getHeight()) - 130);
    	if (player.getY() < TileMapRenderer.tilesToPixels(map.getHeight()) - 128)
    		player.setX(player.getX() - 65);
    	player.setVelocityY(0);
    	
    	map.addSprite(powerUp);
    }
    
    if (powerUp instanceof PowerUp.Jump){
    	player.addJump();
    }
    else if (powerUp instanceof PowerUp.Goal) {
        // advance to next map
    	player.setState(Mob.STATE_DEAD);
    	restart.tap();
    }
    else if (powerUp instanceof PowerUp.Instructions) {
    	instructions = true;
    }
    else if (powerUp instanceof PowerUp.MusicChange) {
    	playlist.stopPlaying();
    	playlist.killIt();
    	musicMode = !musicMode;    	
    	playlist = new Playlist(musicMode);
		playlist.start();
	}
    else if (powerUp instanceof PowerUp.Credits) {
    	credits = true;
    }
    else if (powerUp instanceof PowerUp.CharChange){
    	int next = 0;
    	for (int i = 0; i < chars.length; i++){
    		if (chars[i].equals(player)) next = i + 1;
    	}
    	if (next > 3) next = 0;
    	player = (Player) chars[next];
    	manager.setPlayer(player);
    	map.setPlayer(player);
    }

}
    
    
    // -----------------------------------------------------------
    // code for loading sprites and images
    // -----------------------------------------------------------
    
    public void generateMap() throws FileNotFoundException, UnsupportedEncodingException{
    	PrintWriter writer = new PrintWriter("assets/maps/map1.txt", "UTF-8");
    	boolean previous = false;
    	double randomness = 50.00;
    	int possibleJump = 0;
    	for (int levels = 0; levels < 600; levels++){
    		writer.print("D");
    		for (int i = 0; i < 40; i++){
    			int rand = (int) (Math.random() * ((int) randomness));
    			if (possibleJump - levels == i + 1 && rand < 10){
    				writer.print("o");
    			}
    			else if (previous){
    				if (rand < 10){
    					writer.print("E");
    					possibleJump = i + levels;
    				}
    				else{
    					writer.print(" ");
    					previous = false;
    				}
    			}
    			else if (rand < 2){
    				writer.print("E");
    				previous = true;
    				possibleJump = i + levels;
    			}
    			else writer.print(" ");
    		}
    		randomness -= 0.05;
    		writer.println("C");
    	}
    	writer.print("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
    	writer.close();
    }


}
