import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;


public class ResourceManager {
	
    private ArrayList<Image> tiles;
	
    private GraphicsConfiguration gc;
    
    private Sprite jumpSprite;
    private Sprite musicChangeSprite;
    private Sprite startSprite;
    private Sprite creditSprite;
    private Sprite instructSprite;
    private Sprite charChangeSprite;
    
    private Player player;
    
    public static int currentMap = -1;
    	
	/**
    	Creates a new ResourceManager with the specified
    	GraphicsConfiguration.
	 */
	public ResourceManager(GraphicsConfiguration gc, Player p) {
    	this.gc = gc;
    	loadTileImages();
    	loadPowerUpSprites();
    	player = p;
	}
	
	public void setPlayer(Player p){
		player = p;
	}


    /**
        Gets an image from the images/ directory.
    */
    public Image loadImage(String name) {
        return new ImageIcon(name).getImage();
    }


    public Image getMirrorImage(Image image) {
        return getScaledImage(image, -1, 1);
    }


    public Image getFlippedImage(Image image) {
        return getScaledImage(image, 1, -1);
    }


    private Image getScaledImage(Image image, float x, float y) {

        // set up the transform
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
        transform.translate(
            (x-1) * image.getWidth(null) / 2,
            (y-1) * image.getHeight(null) / 2);

        // create a transparent (not translucent) image
        Image newImage = gc.createCompatibleImage(
            image.getWidth(null),
            image.getHeight(null),
            Transparency.BITMASK);

        // draw the transformed image
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }
    
    public void loadTileImages() {
        // keep looking for tile A,B,C, etc. this makes it
        // easy to drop new tiles in the images/ directory
        tiles = new ArrayList<Image>();
        char ch = 'A';
        int i = 0;
        while (true) {
            String name = "assets/images/tile_" + ch + ".png";
            File file = new File(name);
            if (!file.exists()) {
                break;
            }
            tiles.add(loadImage(name));
            tiles.set(i, loadImage(name));
            ch++;
            i++;
        }
        //tiles.set(0, loadImage("assets/images/tile_A.png"));
    }
    
	public TileMap loadNextMap() {
        TileMap map = null;
        currentMap++;
        while (map == null) {
            try {
            	if (currentMap > 1) currentMap = 1;
                map = loadMap("assets/maps/map" + currentMap + ".txt");
            }
            catch (IOException ex) {
                map = null;
            }
        }
        return map;
    }


    private TileMap loadMap(String filename)
        throws IOException
    {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        // read every line in the text file into the list
        BufferedReader reader = new BufferedReader(
            new FileReader(filename));
        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }

        // parse the lines to create a TileEngine
        height = lines.size();
        TileMap newMap = new TileMap(width, height);
        for (int y=0; y<height; y++) {
            String line = (String)lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, (Image)tiles.get(tile));
                }

                // check if the char represents a sprite
                else if (ch == 'o') {
                    addSprite(newMap, jumpSprite, x, y);
                }
                else if (ch == '&') {
                	addSprite(newMap, creditSprite, x, y);
                }
                else if (ch == '*') {
                	addSprite(newMap, startSprite, x, y);
                }
                else if (ch == '?') {
                	addSprite(newMap, instructSprite,x, y);
                }
                else if (ch == '<') {
                	addSprite(newMap, musicChangeSprite, x, y);
                }
                else if (ch == '+') {
                	addSprite(newMap, charChangeSprite, x, y);
                }
            }
        }
        
        // add the player to the map
        player.setX(TileMapRenderer.tilesToPixels(width / 2 - 1) - 10);
        player.setY(TileMapRenderer.tilesToPixels(height) - 132);
        newMap.setPlayer(player);

        return newMap;
    }
    
    private void addSprite(TileMap map,
            Sprite hostSprite, int tileX, int tileY)
        {
            if (hostSprite != null) {
                // clone the sprite from the "host"
                Sprite sprite = (Sprite)hostSprite.clone();

                // center the sprite
                sprite.setX(
                    TileMapRenderer.tilesToPixels(tileX) +
                    (TileMapRenderer.tilesToPixels(1) -
                    sprite.getWidth()) / 2);

                // bottom-justify the sprite
                sprite.setY(
                    TileMapRenderer.tilesToPixels(tileY + 1) -
                    sprite.getHeight());

                // add it to the map
                map.addSprite(sprite);
            }
        }
    

    
    private void loadPowerUpSprites() {
        Animation anim = new Animation();
        
        //create MusicChange Sprite
        anim.addFrame(loadImage("assets/images/music1.png"), 200);
        anim.addFrame(loadImage("assets/images/music2.png"), 200);
        anim.addFrame(loadImage("assets/images/music3.png"), 200);
        musicChangeSprite = new PowerUp.MusicChange(anim);
        
        //create Instructions Sprite
        anim = new Animation();
        anim.addFrame(loadImage("assets/images/quest1.png"), 200);
        anim.addFrame(loadImage("assets/images/quest2.png"), 200);
        anim.addFrame(loadImage("assets/images/quest3.png"), 200);
        anim.addFrame(loadImage("assets/images/quest2.png"), 100);
        instructSprite = new PowerUp.Instructions(anim);
        
        //create credits Sprite
        anim = new Animation();
        anim.addFrame(loadImage("assets/images/heart1.png"), 200);
        anim.addFrame(loadImage("assets/images/heart2.png"), 200);
        anim.addFrame(loadImage("assets/images/heart3.png"), 200);
        anim.addFrame(loadImage("assets/images/heart2.png"), 200);
        creditSprite = new PowerUp.Credits(anim);
        
        //create "Goal" sprite
        anim = new Animation();
        anim.addFrame(loadImage("assets/images/star1.png"), 200);
        anim.addFrame(loadImage("assets/images/star2.png"), 200);
        anim.addFrame(loadImage("assets/images/star3.png"), 200);
        anim.addFrame(loadImage("assets/images/star4.png"), 200);
        startSprite = new PowerUp.Goal(anim);
        
        // create "doubleJump" sprite
        anim = new Animation();
        anim.addFrame(loadImage("assets/images/jump1.png"), 200);
        anim.addFrame(loadImage("assets/images/jump2.png"), 200);
        anim.addFrame(loadImage("assets/images/jump3.png"), 200);
        anim.addFrame(loadImage("assets/images/jump2.png"), 200);
        jumpSprite = new PowerUp.Jump(anim);
        
        anim = new Animation();
        anim.addFrame(loadImage("assets/images/powerup1.png"), 200);
        anim.addFrame(loadImage("assets/images/powerup2.png"), 200);
        anim.addFrame(loadImage("assets/images/powerup3.png"), 200);
        anim.addFrame(loadImage("assets/images/powerup4.png"), 200);
        charChangeSprite = new PowerUp.CharChange(anim);
    }
}
